package com.intoa.negotiate.service;

import java.util.List;
import com.intoa.negotiate.domain.NegRoom;

/**
 * 洽谈室管理Service接口
 *
 * @author beihai
 * @date 2025-08-29
 */
public interface INegRoomService
{
    /**
     * 查询洽谈室管理
     *
     * @param roomId 洽谈室管理主键
     * @return 洽谈室管理
     */
    public NegRoom selectNegRoomByRoomId(Long roomId);

    /**
     * 查询洽谈室管理列表
     *
     * @param negRoom 洽谈室管理
     * @return 洽谈室管理集合
     */
    public List<NegRoom> selectNegRoomList(NegRoom negRoom);

    /**
     * 新增洽谈室管理
     *
     * @param negRoom 洽谈室管理
     * @return 结果
     */
    public int insertNegRoom(NegRoom negRoom);

    /**
     * 修改洽谈室管理
     *
     * @param negRoom 洽谈室管理
     * @return 结果
     */
    public int updateNegRoom(NegRoom negRoom);

    /**
     * 批量删除洽谈室管理
     *
     * @param roomIds 需要删除的洽谈室管理主键集合
     * @return 结果
     */
    public int deleteNegRoomByRoomIds(Long[] roomIds);

    /**
     * 删除洽谈室管理信息
     *
     * @param roomId 洽谈室管理主键
     * @return 结果
     */
    public int deleteNegRoomByRoomId(Long roomId);
}
