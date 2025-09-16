package com.intoa.negotiate.service.impl;

import com.intoa.negotiate.domain.NegLog;
import com.intoa.negotiate.domain.NegRoom;
import com.intoa.negotiate.domain.dto.RoomAvailabilityDTO;
import com.intoa.negotiate.mapper.NegLogMapper;
import com.intoa.negotiate.mapper.NegRoomMapper;
import com.intoa.negotiate.service.IRoomAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 房间可用性服务实现类
 * <p>
 * 实现房间在指定时间段内的可用性检查逻辑，包括房间状态检查和时间冲突检查。
 * </p>
 * 
 * @author intoa
 * @date 2025-09-12
 */
@Service
public class RoomAvailabilityServiceImpl implements IRoomAvailabilityService {

    @Autowired
    private NegRoomMapper roomMapper;

    @Autowired
    private NegLogMapper logMapper;

    /**
     * 检查房间在指定时间段内的可用性
     * <p>
     * 综合考虑房间状态（status=0表示可用）和时间冲突情况，判断房间是否可用。
     * 时间冲突检测考虑了房间的缓冲时间设置。
     * </p>
     *
     * @param roomName  房间名称
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 房间可用性DTO
     */
    @Override
    public RoomAvailabilityDTO checkRoomAvailability(String roomName, Date startTime, Date endTime) {
        // 查询房间信息
        NegRoom room = roomMapper.selectNegRoomByRoomName(roomName);
        if (room == null) {
            return new RoomAvailabilityDTO(null, false, "房间不存在");
        }

        // 检查房间状态是否为可用（status=0）
        if (!"0".equals(room.getStatus())) {
            return new RoomAvailabilityDTO(room, false, "房间状态不可用");
        }

        // 检查时间冲突（考虑缓冲时间）
        boolean hasConflict = hasTimeConflict(room, startTime, endTime);
        if (hasConflict) {
            return new RoomAvailabilityDTO(room, false, "时间段内存在预约冲突");
        }

        // 房间可用
        return new RoomAvailabilityDTO(room, true, null);
    }

    /**
     * 检查指定房间在指定时间段是否存在时间冲突（考虑缓冲时间）
     * <p>
     * 优化后的时间冲突检测逻辑，使用代码实现而非SQL查询，并考虑房间缓冲时间。
     * 冲突检测算法：
     * 新预约开始时间 < 已有预约结束时间 + 已有预约缓冲时间
     * 新预约结束时间 + 新预约缓冲时间 > 已有预约开始时间
     * </p>
     *
     * @param room 房间对象
     * @param startTime 新预约开始时间
     * @param endTime 新预约结束时间
     * @return 是否存在时间冲突
     */
    private boolean hasTimeConflict(NegRoom room, Date startTime, Date endTime) {
        // 获取房间缓冲时间，默认为0（表示无缓冲时间）
        LocalTime bufferTime = room.getBufferTime() != null ? room.getBufferTime() : LocalTime.of(0, 0);
        
        // 查询该房间在指定时间范围内的所有预约记录
        List<NegLog> existingAppointments = logMapper.selectNegLogListByRoomAndTime(
                room.getRoomName(), startTime, endTime);
        
        // 遍历所有预约记录，检查是否存在时间冲突
        for (NegLog appointment : existingAppointments) {
            // 检查时间是否冲突（考虑缓冲时间）
            if (isTimeOverlapping(startTime, endTime, bufferTime, 
                    appointment.getStartTime(), appointment.getEndTime(), bufferTime)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 检查两个时间段是否重叠（考虑缓冲时间）
     * <p>
     * 时间重叠检测算法：
     * startTime1 < endTime2 + bufferTime2
     * endTime1 + bufferTime1 > startTime2
     * </p>
     *
     * @param startTime1 第一个时间段开始时间
     * @param endTime1 第一个时间段结束时间
     * @param bufferTime1 第一个时间段的缓冲时间
     * @param startTime2 第二个时间段开始时间
     * @param endTime2 第二个时间段结束时间
     * @param bufferTime2 第二个时间段的缓冲时间
     * @return 是否重叠
     */
    private boolean isTimeOverlapping(Date startTime1, Date endTime1, LocalTime bufferTime1,
                                      Date startTime2, Date endTime2, LocalTime bufferTime2) {
        // 将缓冲时间转换为毫秒数
        long bufferMillis1 = bufferTime1.toSecondOfDay() * 1000L;
        long bufferMillis2 = bufferTime2.toSecondOfDay() * 1000L;
        
        // 检查时间是否重叠
        // 条件1: 第一个时间段开始时间 < 第二个时间段结束时间 + 第二个时间段缓冲时间
        boolean condition1 = startTime1.getTime() < endTime2.getTime() + bufferMillis2;
        // 条件2: 第一个时间段结束时间 + 第一个时间段缓冲时间 > 第二个时间段开始时间
        boolean condition2 = endTime1.getTime() + bufferMillis1 > startTime2.getTime();
        
        return condition1 && condition2;
    }

    /**
     * 获取指定时间段内所有房间的可用性列表
     * <p>
     * 遍历所有房间，检查每个房间在指定时间段内的可用性。
     * </p>
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 房间可用性DTO列表
     */
    @Override
    public List<RoomAvailabilityDTO> getAllRoomsAvailability(Date startTime, Date endTime) {
        // 查询所有房间
        List<NegRoom> allRooms = roomMapper.selectNegRoomList(new NegRoom());

        // 检查每个房间的可用性
        return allRooms.stream()
                .map(room -> checkRoomAvailability(room.getRoomName(), startTime, endTime))
                .collect(Collectors.toList());
    }

    /**
     * 获取指定时间段内可用的房间列表
     * <p>
     * 仅返回在指定时间段内可用的房间列表。
     * </p>
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 可用房间的可用性DTO列表
     */
    @Override
    public List<RoomAvailabilityDTO> getAvailableRooms(Date startTime, Date endTime) {
        // 获取所有房间的可用性
        List<RoomAvailabilityDTO> allRoomsAvailability = getAllRoomsAvailability(startTime, endTime);

        // 过滤出可用的房间
        return allRoomsAvailability.stream()
                .filter(RoomAvailabilityDTO::getAvailable)
                .collect(Collectors.toList());
    }
}