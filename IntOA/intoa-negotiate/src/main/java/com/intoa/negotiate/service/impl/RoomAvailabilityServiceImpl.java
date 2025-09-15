package com.intoa.negotiate.service.impl;

import com.intoa.negotiate.domain.NegRoom;
import com.intoa.negotiate.domain.dto.RoomAvailabilityDTO;
import com.intoa.negotiate.mapper.NegLogMapper;
import com.intoa.negotiate.mapper.NegRoomMapper;
import com.intoa.negotiate.service.IRoomAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * </p>
     *
     * @param roomId    房间ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 房间可用性DTO
     */
    @Override
    public RoomAvailabilityDTO checkRoomAvailability(Long roomId, Date startTime, Date endTime) {
        // 查询房间信息
        NegRoom room = roomMapper.selectNegRoomByRoomId(roomId);
        if (room == null) {
            return new RoomAvailabilityDTO(null, false, "房间不存在");
        }

        // 检查房间状态是否为可用（status=0）
        if (!"0".equals(room.getStatus())) {
            return new RoomAvailabilityDTO(room, false, "房间状态不可用");
        }

        // 检查时间冲突
        int conflictCount = logMapper.countConflictingAppointments(room.getRoomName(), startTime, endTime);
        if (conflictCount > 0) {
            return new RoomAvailabilityDTO(room, false, "时间段内存在预约冲突");
        }

        // 房间可用
        return new RoomAvailabilityDTO(room, true, null);
    }
    
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

        // 检查时间冲突
        int conflictCount = logMapper.countConflictingAppointments(roomName, startTime, endTime);
        if (conflictCount > 0) {
            return new RoomAvailabilityDTO(room, false, "时间段内存在预约冲突");
        }

        // 房间可用
        return new RoomAvailabilityDTO(room, true, null);
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
                .map(room -> checkRoomAvailability(room.getRoomId(), startTime, endTime))
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