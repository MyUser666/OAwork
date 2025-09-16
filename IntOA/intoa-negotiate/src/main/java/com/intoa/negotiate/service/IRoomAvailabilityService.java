package com.intoa.negotiate.service;

import com.intoa.negotiate.domain.NegRoom;
import com.intoa.negotiate.domain.dto.RoomAvailabilityDTO;

import java.util.Date;
import java.util.List;

/**
 * 房间预约冲突计算接口
 * <p>
 * 提供计算房间预约时间冲突检查和可预约房间查询功能。
 * </p>
 *
 * @author lingma
 * @date 2025-09-15
 */
public interface IRoomAvailabilityService {
    
    /**
     * 检查房间在指定时间段内的可用性
     * <p>
     * 综合考虑房间状态（status=0表示可用）和时间冲突情况，判断房间是否可用。
     * </p>
     *
     * @param roomName  房间名称
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 房间可用性DTO
     */
    RoomAvailabilityDTO checkRoomAvailability(String roomName, Date startTime, Date endTime);

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
    List<RoomAvailabilityDTO> getAllRoomsAvailability(Date startTime, Date endTime);

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
    List<RoomAvailabilityDTO> getAvailableRooms(Date startTime, Date endTime);
}