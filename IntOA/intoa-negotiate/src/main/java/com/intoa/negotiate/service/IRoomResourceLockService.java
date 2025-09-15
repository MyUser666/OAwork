package com.intoa.negotiate.service;

import java.util.Date;

/**
 * 房间资源锁定服务接口
 * <p>
 * 该接口定义了房间资源锁定相关的业务操作，包括临时锁定、正式锁定、
 * 锁定状态检查、资源释放等。
 * </p>
 * 
 * @author intoa
 * @date 2025-09-12
 */
public interface IRoomResourceLockService {

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
    boolean tempLockRoom(String roomName, Date date, Date startTime, Date endTime, String requestId, int expireSeconds);

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
    boolean lockRoom(String roomName, Date date, Date startTime, Date endTime, Long logId);

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
    boolean releaseRoom(String roomName, Date date, Date startTime, Date endTime);

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
    boolean isRoomLocked(String roomName, Date date, Date startTime, Date endTime);

    /**
     * 检查临时锁定是否过期
     * <p>
     * 检查用户的临时锁定是否过期。
     * </p>
     *
     * @param requestId 请求标识（用户ID等）
     * @return 检查结果，true表示有效，false表示已过期
     */
    boolean isTempLockValid(String requestId);

    /**
     * 更新房间状态到Redis
     * <p>
     * 为指定房间和日期设置Redis缓存的过期时间，确保数据的时效性。
     * </p>
     *
     * @param roomName 房间名称
     * @param date     日期
     */
    void updateRoomStatusInRedis(String roomName, Date date);
}