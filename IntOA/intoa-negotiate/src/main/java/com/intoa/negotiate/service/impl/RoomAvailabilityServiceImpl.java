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
 * 实现房间可用性检查和查询功能。
 * </p>
 *
 * @author lingma
 * @date 2025-09-15
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
     * @param roomName  房间名称
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 房间可用性DTO
     */
    @Override
    public RoomAvailabilityDTO checkRoomAvailability(String roomName, Date startTime, Date endTime) {
        //获取所有可用房间列表,执行getNegRoomStatus方法
        List<NegRoom> availableRooms = getNegRoomStatus();
        if (availableRooms.isEmpty()) {
            return new RoomAvailabilityDTO(null, false, "没有可用房间");
        }
        NegRoom room = availableRooms.get(0);
        
        // 检查房间是否在可用房间列表中
        boolean isRoomAvailable = availableRooms.stream()
                .anyMatch(availableRoom -> availableRoom.getRoomName().equals(roomName));
        
        if (!isRoomAvailable) {
            return new RoomAvailabilityDTO(room, false, "房间不在可用房间列表中");
        }

        // 检查时间冲突
        int conflictCount = logMapper.countConflictingAppointments(roomName, startTime, endTime);
        if (conflictCount > 0) {
            return new RoomAvailabilityDTO(room, false, "时间段内存在预约冲突");
        }

        // 房间可用
        return new RoomAvailabilityDTO(room, true, null);
    }
//    逐行解释：
//    List<NegRoom> availableRooms = getNegRoomStatus();
//    调用 getNegRoomStatus() 方法获取所有状态为"0"（可用）的房间列表
//if (availableRooms.isEmpty()) { return new RoomAvailabilityDTO(null, false, "没有可用房间"); }
//    检查可用房间列表是否为空
//    如果没有可用房间，直接返回一个房间不可用的DTO对象，错误信息为"没有可用房间"
//    NegRoom room = availableRooms.get(0);
//    从可用房间列表中获取第一个房间对象
//    注意：这里存在一个问题，获取的第一个房间并不一定是用户要查询的房间
//    boolean isRoomAvailable = availableRooms.stream().anyMatch(availableRoom -> availableRoom.getRoomName().equals(roomName));
//    使用Java 8 Stream API检查参数中指定的房间名称是否在可用房间列表中
//    anyMatch 方法会检查列表中是否有任何一个房间的名称与传入的 roomName 参数匹配
//if (!isRoomAvailable) { return new RoomAvailabilityDTO(room, false, "房间不在可用房间列表中"); }
//    如果指定的房间不在可用房间列表中，返回房间不可用的DTO对象
//    注意：这里返回的是第3步中获取的第一个房间，而不是用户要查询的房间，这可能会导致混淆
//    int conflictCount = logMapper.countConflictingAppointments(roomName, startTime, endTime);
//    调用Mapper方法检查指定房间在指定时间段内是否存在时间冲突的预约
//    传入参数为房间名称、开始时间和结束时间
//if (conflictCount > 0) { return new RoomAvailabilityDTO(room, false, "时间段内存在预约冲突"); }
//    如果存在时间冲突（冲突数量大于0），返回房间不可用的DTO对象
//    注意：同样返回的是第3步中获取的第一个房间，而不是用户要查询的房间
//return new RoomAvailabilityDTO(room, true, null);
//    如果通过了所有检查，返回房间可用的DTO对象
//    注意：依然返回的是第3步中获取的第一个房间，而不是用户要查询的房间
//    代码问题分析：
//    逻辑错误：
//    代码获取的是可用房间列表中的第一个房间，而不是用户指定的房间
//    在返回结果时，无论检查的是哪个房间，都返回第一个房间的信息，这会导致结果不准确
//    缺少房间查询：
//    代码没有根据 roomName 参数查询具体的房间信息
//    应该先通过房间名称查询到具体的房间对象，然后再进行后续检查
//    错误信息混淆：
//    即使用户指定的房间不存在，也会返回第一个可用房间的信息和错误原因，这会误导用户
//    正确的实现方式应该是：
//    首先根据 roomName 查询具体的房间信息
//            检查房间是否存在
//    检查房间状态是否为可用
//            检查时间冲突
//    根据检查结果返回正确的房间信息和状态
//    这样可以确保返回的结果与用户查询的房间一致，避免混淆。

    
    /**
     * 获取所有可用房间列表
     * <p>
     * 获取所有状态为可用（status=0）的房间列表。
     * </p>
     *
     * @return 可用房间列表
     */
    @Override
    public List<NegRoom> getNegRoomStatus() {
        NegRoom room = new NegRoom();
        room.setStatus("0");
        return roomMapper.selectNegRoomList(room);
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