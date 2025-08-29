package com.intoa.negotiate.service.impl;

import java.util.List;
import com.intoa.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intoa.negotiate.mapper.NegRoomMapper;
import com.intoa.negotiate.domain.NegRoom;
import com.intoa.negotiate.service.INegRoomService;
import com.intoa.system.service.ISysDictDataService;
import com.intoa.common.core.domain.entity.SysDictData;
import com.intoa.common.constant.UserConstants;

/**
 * 洽谈室管理Service业务层处理
 *
 * @author beihai
 * @date 2025-08-29
 */
@Service
public class NegRoomServiceImpl implements INegRoomService
{
    @Autowired
    private NegRoomMapper negRoomMapper;

    @Autowired
    private ISysDictDataService dictDataService;

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
        int result = negRoomMapper.insertNegRoom(negRoom);
        if (result > 0) {
            // 同步添加到数据字典，roomId=dict_value，roomName=dict_label
            SysDictData dictData = new SysDictData();
            dictData.setDictLabel(negRoom.getRoomName()); // roomName作为dict_label
            dictData.setDictType("oa_negroom_name");
            dictData.setDictValue(String.valueOf(negRoom.getRoomId())); // roomId作为dict_value
            dictData.setStatus(UserConstants.DICT_NORMAL);
            dictData.setDictSort(0L);
            dictData.setIsDefault(UserConstants.YES);
            dictDataService.insertDictData(dictData);
        }
        return result;
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
        NegRoom oldRoom = negRoomMapper.selectNegRoomByRoomId(negRoom.getRoomId());
        negRoom.setUpdateTime(DateUtils.getNowDate());
        int result = negRoomMapper.updateNegRoom(negRoom);
        if (result > 0) {
            // 更新数据字典中的房间信息，保持roomId=dict_value，roomName=dict_label
            SysDictData dictData = new SysDictData();
            dictData.setDictType("oa_negroom_name");
            dictData.setDictValue(String.valueOf(negRoom.getRoomId())); // 使用roomId查找记录
            // 查找并更新数据字典中的记录
            List<SysDictData> dictList = dictDataService.selectDictDataList(dictData);
            if (!dictList.isEmpty()) {
                SysDictData updateDict = dictList.get(0);
                updateDict.setDictLabel(negRoom.getRoomName()); // roomName作为dict_label
                dictDataService.updateDictData(updateDict);
            }
        }
        return result;
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
        // 删除数据字典中的记录
        for (Long roomId : roomIds) {
            SysDictData dictData = new SysDictData();
            dictData.setDictType("oa_negroom_name");
            dictData.setDictValue(String.valueOf(roomId)); // 使用roomId作为dict_value查找
            List<SysDictData> dictList = dictDataService.selectDictDataList(dictData);
            if (!dictList.isEmpty()) {
                dictDataService.deleteDictDataByIds(new Long[]{dictList.get(0).getDictCode()});
            }
        }
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
        // 删除数据字典中的记录
        SysDictData dictData = new SysDictData();
        dictData.setDictType("oa_negroom_name");
        dictData.setDictValue(String.valueOf(roomId)); // 使用roomId作为dict_value查找
        List<SysDictData> dictList = dictDataService.selectDictDataList(dictData);
        if (!dictList.isEmpty()) {
            dictDataService.deleteDictDataByIds(new Long[]{dictList.get(0).getDictCode()});
        }
        return negRoomMapper.deleteNegRoomByRoomId(roomId);
    }
}