package com.intoa.negotiate.service.impl;

import java.util.List;
// 导入日期工具类，用于获取当前时间
import com.intoa.common.utils.DateUtils;
// 导入Spring的注解，用于自动装配依赖
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring的注解，用于标记服务层组件
import org.springframework.stereotype.Service;
// 导入洽谈室Mapper接口，用于数据库操作
import com.intoa.negotiate.mapper.NegRoomMapper;
// 导入洽谈室实体类
import com.intoa.negotiate.domain.NegRoom;
// 导入洽谈室服务接口
import com.intoa.negotiate.service.INegRoomService;
// 导入系统字典数据服务接口，用于操作数据字典
import com.intoa.system.service.ISysDictDataService;
// 导入系统字典数据实体类
import com.intoa.common.core.domain.entity.SysDictData;
// 导入用户常量类，用于获取字典状态常量
import com.intoa.common.constant.UserConstants;
// 导入事务注解
import org.springframework.transaction.annotation.Transactional;

/**
 * 洽谈室管理Service业务层处理
 *
 * @author beihai
 * @date 2025-08-29
 */
@Service
public class NegRoomServiceImpl implements INegRoomService
{
    // 自动装配洽谈室Mapper，用于执行数据库操作
    @Autowired
    private NegRoomMapper negRoomMapper;

    // 自动装配系统字典数据服务，用于同步洽谈室信息到数据字典
    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 根据主键查询洽谈室管理信息
     *
     * @param roomId 洽谈室管理主键
     * @return 洽谈室管理对象
     */
    @Override
    public NegRoom selectNegRoomByRoomId(Long roomId)
    {
        // 调用Mapper接口查询洽谈室信息
        return negRoomMapper.selectNegRoomByRoomId(roomId);
    }

    /**
     * 查询洽谈室管理列表
     *
     * @param negRoom 洽谈室管理查询条件
     * @return 洽谈室管理列表
     */
    @Override
    public List<NegRoom> selectNegRoomList(NegRoom negRoom)
    {
        // 调用Mapper接口查询洽谈室列表
        return negRoomMapper.selectNegRoomList(negRoom);
    }

    /**
     * 新增洽谈室管理
     *
     * @param negRoom 洽谈室管理对象
     * @return 操作结果，1表示成功，0表示失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertNegRoom(NegRoom negRoom)
    {
        // 设置创建时间为当前时间
        negRoom.setCreateTime(DateUtils.getNowDate());
        // 调用Mapper接口插入洽谈室信息
        int result = negRoomMapper.insertNegRoom(negRoom);
        // 如果插入成功，则同步添加到数据字典
        if (result > 0) {
            // 创建字典数据对象
            SysDictData dictData = new SysDictData();
            // 设置字典标签为房间名称
            dictData.setDictLabel(negRoom.getRoomName()); 
            // 设置字典类型为"oa_negroom_name"
            dictData.setDictType("oa_negroom_name");
            // 设置字典键值为房间ID
            dictData.setDictValue(String.valueOf(negRoom.getRoomId())); 
            // 设置字典状态为正常
            dictData.setStatus(UserConstants.DICT_NORMAL);
            // 设置字典排序为0
            dictData.setDictSort(0L);
            // 设置为默认值
            dictData.setIsDefault(UserConstants.YES);
            // 调用字典数据服务插入字典数据
            dictDataService.insertDictData(dictData);
        }
        // 返回操作结果
        return result;
    }

    /**
     * 修改洽谈室管理
     *
     * @param negRoom 洽谈室管理对象
     * @return 操作结果，1表示成功，0表示失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateNegRoom(NegRoom negRoom)
    {
        // 查询修改前的洽谈室信息
        NegRoom oldRoom = negRoomMapper.selectNegRoomByRoomId(negRoom.getRoomId());
        // 设置更新时间为当前时间
        negRoom.setUpdateTime(DateUtils.getNowDate());
        // 调用Mapper接口更新洽谈室信息
        int result = negRoomMapper.updateNegRoom(negRoom);
        // 如果更新成功，则同步更新数据字典中的房间信息
        if (result > 0) {
            // 根据字典类型和字典键值精确查找记录
            SysDictData dictData = dictDataService.selectDictDataByTypeAndValue("oa_negroom_name", String.valueOf(negRoom.getRoomId()));
            // 如果找到记录，则更新字典数据
            if (dictData != null) {
                // 更新字典标签为新的房间名称
                dictData.setDictLabel(negRoom.getRoomName()); 
                // 调用字典数据服务更新字典数据
                dictDataService.updateDictData(dictData);
            }
        }
        // 返回操作结果
        return result;
    }

    /**
     * 批量删除洽谈室管理
     *
     * @param roomIds 需要删除的洽谈室管理主键数组
     * @return 操作结果，成功删除的记录数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteNegRoomByRoomIds(Long[] roomIds)
    {
        // 遍历需要删除的房间ID
        for (Long roomId : roomIds) {
            // 根据字典类型和字典键值精确查找记录
            SysDictData dictData = dictDataService.selectDictDataByTypeAndValue("oa_negroom_name", String.valueOf(roomId));
            // 如果找到记录，则删除数据字典中的记录
            if (dictData != null) {
                // 调用字典数据服务删除字典数据
                dictDataService.deleteDictDataByIds(new Long[]{dictData.getDictCode()});
            }
        }
        // 调用Mapper接口批量删除洽谈室信息
        return negRoomMapper.deleteNegRoomByRoomIds(roomIds);
    }

    /**
     * 删除洽谈室管理信息
     *
     * @param roomId 洽谈室管理主键
     * @return 操作结果，1表示成功，0表示失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteNegRoomByRoomId(Long roomId)
    {
        // 根据字典类型和字典键值精确查找记录
        SysDictData dictData = dictDataService.selectDictDataByTypeAndValue("oa_negroom_name", String.valueOf(roomId));
        // 如果找到记录，则删除数据字典中的记录
        if (dictData != null) {
            // 调用字典数据服务删除字典数据
            dictDataService.deleteDictDataByIds(new Long[]{dictData.getDictCode()});
        }
        // 调用Mapper接口删除洽谈室信息
        return negRoomMapper.deleteNegRoomByRoomId(roomId);
    }
}