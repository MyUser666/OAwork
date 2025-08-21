package com.intoa.negotiate.service.impl;

import java.util.List;
import com.intoa.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intoa.negotiate.mapper.NegRoomMapper;
import com.intoa.negotiate.domain.NegRoom;
import com.intoa.negotiate.service.INegRoomService;

/**
 * 洽谈室管理Service业务层处理
 * 
 * @author beihai
 * @date 2025-08-21
 */
@Service
public class NegRoomServiceImpl implements INegRoomService 
{
    @Autowired
    private NegRoomMapper negRoomMapper;

    /**
     * 查询洽谈室管理
     * 
     * @param roomId 洽谈室管理主键
     * @return 洽谈室管理
     */
    @Override
    public NegRoom selectNegRoomByRoomId(Long roomId)
    {
        return negRoomMapper.selectNegRoomByRoomId(roomId);
    }

    /**
     * 查询洽谈室管理列表
     * 
     * @param negRoom 洽谈室管理
     * @return 洽谈室管理
     */
    @Override
    public List<NegRoom> selectNegRoomList(NegRoom negRoom)
    {
        return negRoomMapper.selectNegRoomList(negRoom);
    }

    /**
     * 新增洽谈室管理
     * 
     * @param negRoom 洽谈室管理
     * @return 结果
     */
    @Override
    public int insertNegRoom(NegRoom negRoom)
    {
        negRoom.setCreateTime(DateUtils.getNowDate());
        return negRoomMapper.insertNegRoom(negRoom);
    }

    /**
     * 修改洽谈室管理
     * 
     * @param negRoom 洽谈室管理
     * @return 结果
     */
    @Override
    public int updateNegRoom(NegRoom negRoom)
    {
        negRoom.setUpdateTime(DateUtils.getNowDate());
        return negRoomMapper.updateNegRoom(negRoom);
    }

    /**
     * 批量删除洽谈室管理
     * 
     * @param roomIds 需要删除的洽谈室管理主键
     * @return 结果
     */
    @Override
    public int deleteNegRoomByRoomIds(Long[] roomIds)
    {
        return negRoomMapper.deleteNegRoomByRoomIds(roomIds);
    }

    /**
     * 删除洽谈室管理信息
     * 
     * @param roomId 洽谈室管理主键
     * @return 结果
     */
    @Override
    public int deleteNegRoomByRoomId(Long roomId)
    {
        return negRoomMapper.deleteNegRoomByRoomId(roomId);
    }
}
