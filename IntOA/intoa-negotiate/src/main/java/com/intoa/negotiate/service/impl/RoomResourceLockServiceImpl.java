package com.intoa.negotiate.service.impl;

import com.intoa.common.core.redis.RedisCache;
import com.intoa.negotiate.service.IRoomResourceLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 房间资源锁定服务实现类
 * <p>
 * 该服务负责处理房间资源锁定相关的业务逻辑，包括临时锁定、正式锁定、
 * 锁定状态检查、资源释放等。
 * </p>
 * 
 * @author intoa
 * @date 2025-09-12
 */
@Service
public class RoomResourceLockServiceImpl implements IRoomResourceLockService {

    /**
     * Redis工具类实例，用于操作Redis缓存
     */
    @Autowired
    private RedisCache redisCache;

    /**
     * Redis房间锁定键前缀
     * 格式为: room:lock:{roomName}:{date}
     */
    private static final String ROOM_LOCK_PREFIX = "room:lock:";
    
    /**
     * Redis临时锁定键前缀
     * 格式为: temp:lock:{userId}:{timestamp}
     */
    private static final String TEMP_LOCK_PREFIX = "temp:lock:";

    // 锁定状态定义
    private static final String TEMP_LOCK_STATUS = "0"; // 临时锁定（待确认状态）
    private static final String LOCK_STATUS = "1";      // 正式锁定（已确认状态）

    /**
     * 临时锁定房间资源
     * <p>
     * 在Redis中临时锁定房间在指定时间段的资源，用于用户选择房间后的临时占用。
     * </p>
     *
     * @param roomName  房间名称
     * @param date      日期
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param requestId 请求标识（用户ID等）
     * @param expireSeconds 过期时间（秒）
     * @return 锁定结果，true表示锁定成功，false表示锁定失败
     */
    @Override
    public boolean tempLockRoom(String roomName, Date date, Date startTime, Date endTime, String requestId, int expireSeconds) {
        try {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String startTimeStr = new SimpleDateFormat("HH:mm").format(startTime);
            String endTimeStr = new SimpleDateFormat("HH:mm").format(endTime);

            // 构造Redis键和字段
            String key = ROOM_LOCK_PREFIX + roomName + ":" + dateStr;
            String field = startTimeStr + "-" + endTimeStr;
            
            // 构造锁定信息
            String expireTime = String.valueOf(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expireSeconds));
            String lockInfo = requestId + ":" + TEMP_LOCK_STATUS + ":" + expireTime;

            // 设置哈希字段
            redisCache.setCacheMapValue(key, field, lockInfo);
            
            // 设置过期时间
            redisCache.expire(key, expireSeconds, TimeUnit.SECONDS);
            
            // 记录临时锁定信息，用于超时检查
            String tempLockKey = TEMP_LOCK_PREFIX + requestId;
            String tempLockValue = key + ":" + field;
            redisCache.setCacheObject(tempLockKey, tempLockValue);
            redisCache.expire(tempLockKey, expireSeconds, TimeUnit.SECONDS);
            
            // 更新房间状态到Redis
            updateRoomStatusInRedis(roomName, date);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 正式锁定房间资源
     * <p>
     * 在Redis中正式锁定房间在指定时间段的资源，用于用户提交预约后的正式占用。
     * </p>
     *
     * @param roomName  房间名称
     * @param date      日期
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param logId     预约ID
     * @return 锁定结果，true表示锁定成功，false表示锁定失败
     */
    @Override
    public boolean lockRoom(String roomName, Date date, Date startTime, Date endTime, Long logId) {
        try {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String startTimeStr = new SimpleDateFormat("HH:mm").format(startTime);
            String endTimeStr = new SimpleDateFormat("HH:mm").format(endTime);

            // 构造Redis键和字段
            String key = ROOM_LOCK_PREFIX + roomName + ":" + dateStr;
            String field = startTimeStr + "-" + endTimeStr;
            
            // 构造锁定信息
            String expireTime = String.valueOf(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)); // 24小时后过期
            String lockInfo = logId + ":" + LOCK_STATUS + ":" + expireTime;

            // 设置哈希字段
            redisCache.setCacheMapValue(key, field, lockInfo);
            
            // 设置过期时间
            redisCache.expire(key, 24, TimeUnit.HOURS);
            
            // 更新房间状态到Redis
            updateRoomStatusInRedis(roomName, date);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 释放房间资源
     * <p>
     * 释放Redis中房间在指定时间段的资源锁定，用于用户取消预约或预约完成后释放资源。
     * </p>
     *
     * @param roomName  房间名称
     * @param date      日期
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 释放结果，true表示释放成功，false表示释放失败
     */
    @Override
    public boolean releaseRoom(String roomName, Date date, Date startTime, Date endTime) {
        try {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String startTimeStr = new SimpleDateFormat("HH:mm").format(startTime);
            String endTimeStr = new SimpleDateFormat("HH:mm").format(endTime);

            // 构造Redis键和字段
            String key = ROOM_LOCK_PREFIX + roomName + ":" + dateStr;
            String field = startTimeStr + "-" + endTimeStr;

            // 删除哈希字段
            redisCache.deleteCacheMapValue(key, field);
            
            // 更新房间状态到Redis
            updateRoomStatusInRedis(roomName, date);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查房间在指定时间段是否被锁定
     * <p>
     * 通过查询Redis中的锁定状态，判断房间在指定时间段是否被临时或正式锁定。
     * </p>
     *
     * @param roomName  房间名称
     * @param date      日期
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否被锁定，true表示被锁定，false表示未被锁定
     */
    @Override
    public boolean isRoomLocked(String roomName, Date date, Date startTime, Date endTime) {
        try {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String startTimeStr = new SimpleDateFormat("HH:mm").format(startTime);
            String endTimeStr = new SimpleDateFormat("HH:mm").format(endTime);

            // 构造Redis键和字段
            String key = ROOM_LOCK_PREFIX + roomName + ":" + dateStr;
            String field = startTimeStr + "-" + endTimeStr;

            // 检查是否存在锁定信息
            return redisCache.getCacheMapValue(key, field) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查临时锁定是否过期
     * <p>
     * 检查用户的临时锁定是否过期。
     * </p>
     *
     * @param requestId 请求标识（用户ID等）
     * @return 检查结果，true表示有效，false表示已过期
     */
    @Override
    public boolean isTempLockValid(String requestId) {
        try {
            String tempLockKey = TEMP_LOCK_PREFIX + requestId;
            return redisCache.getCacheObject(tempLockKey) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新房间状态到Redis
     * <p>
     * 为指定房间和日期设置Redis缓存的过期时间，确保数据的时效性。
     * </p>
     *
     * @param roomName 房间名称
     * @param date     日期
     */
    @Override
    public void updateRoomStatusInRedis(String roomName, Date date) {
        try {
            // 格式化日期为yyyy-MM-dd格式的字符串
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);

            // 构造Redis键，格式为 room:status:{roomName}:{date}
            String key = "room:status:" + roomName + ":" + dateStr;

            // 设置过期时间，24小时后过期
            redisCache.expire(key, 24, TimeUnit.HOURS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}