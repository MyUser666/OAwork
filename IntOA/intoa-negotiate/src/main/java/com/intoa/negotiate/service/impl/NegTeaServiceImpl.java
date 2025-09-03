package com.intoa.negotiate.service.impl;

import java.util.List;
// 导入日期工具类，用于获取当前时间
import com.intoa.common.utils.DateUtils;
// 导入安全工具类，用于获取当前用户信息
import com.intoa.common.utils.SecurityUtils;
// 导入Spring的注解，用于自动装配依赖
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring的注解，用于标记服务层组件
import org.springframework.stereotype.Service;
// 导入茶水Mapper接口，用于数据库操作
import com.intoa.negotiate.mapper.NegTeaMapper;
// 导入茶水实体类
import com.intoa.negotiate.domain.NegTea;
// 导入茶水服务接口
import com.intoa.negotiate.service.INegTeaService;
// 导入系统字典数据服务接口，用于操作数据字典
import com.intoa.system.service.ISysDictDataService;
// 导入系统字典数据实体类
import com.intoa.common.core.domain.entity.SysDictData;
// 导入用户常量类，用于获取字典状态常量
import com.intoa.common.constant.UserConstants;
// 导入事务注解
import org.springframework.transaction.annotation.Transactional;

/**
 * 茶水管理Service业务层处理
 *
 * @author beihai
 * @date 2025-08-22
 */
@Service
public class NegTeaServiceImpl implements INegTeaService
{
    // 自动装配茶水Mapper，用于执行数据库操作
    @Autowired
    private NegTeaMapper negTeaMapper;
    
    // 自动装配系统字典数据服务，用于同步茶水信息到数据字典
    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询茶水管理
     *
     * @param teaId 茶水管理主键
     * @return 茶水管理
     */
    @Override
    public NegTea selectNegTeaByTeaId(Long teaId)
    {
        // 调用Mapper接口查询茶水信息
        return negTeaMapper.selectNegTeaByTeaId(teaId);
    }

    /**
     * 查询茶水管理列表
     *
     * @param negTea 茶水管理
     * @return 茶水管理
     */
    @Override
    public List<NegTea> selectNegTeaList(NegTea negTea)
    {
        // 调用Mapper接口查询茶水列表
        return negTeaMapper.selectNegTeaList(negTea);
    }

    /**
     * 新增茶水管理
     *
     * @param negTea 茶水管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertNegTea(NegTea negTea)
    {
        // 查询是否已存在相同名称的茶水
        NegTea queryTea = new NegTea();
        queryTea.setTeaName(negTea.getTeaName());
        List<NegTea> existingTeas = negTeaMapper.selectNegTeaList(queryTea);
        
        // 如果已存在相同名称的茶水，则更新库存数量
        if (!existingTeas.isEmpty()) {
            NegTea existingTea = existingTeas.get(0);
            // 将原有库存数量与添加数量相加
            Long newStockQuantity = (existingTea.getStockQuantity() != null ? existingTea.getStockQuantity() : 0L) 
                                  + (negTea.getStockQuantity() != null ? negTea.getStockQuantity() : 0L);
            existingTea.setStockQuantity(newStockQuantity);
            // 设置更新者为当前用户的昵称
            existingTea.setUpdateBy(SecurityUtils.getLoginUser().getUser().getNickName());
            // 设置更新时间为当前时间
            existingTea.setUpdateTime(DateUtils.getNowDate());
            // 更新茶水信息
//            int result = negTeaMapper.updateNegTea(existingTea);
//            // 如果更新成功，则同步更新数据字典中的茶水信息
//            if (result > 0) {
//                // 根据字典类型和字典键值精确查找记录
//                SysDictData dictData = dictDataService.selectDictDataByTypeAndValue("on_negtea_name", String.valueOf(existingTea.getTeaId()));
//                // 如果找到记录，则更新字典数据
//                if (dictData != null) {
//                    // 更新字典标签为新的茶水名称
//                    dictData.setDictLabel(existingTea.getTeaName());
//                    // 调用字典数据服务更新字典数据
//                    dictDataService.updateDictData(dictData);
//                }
//            }
//            // 返回操作结果
            return negTeaMapper.updateNegTea(existingTea);
        } else {
            // 不存在相同名称的茶水，执行新增操作
            // 设置创建者为当前用户的昵称
            negTea.setCreateBy(SecurityUtils.getLoginUser().getUser().getNickName());
            // 设置更新者为当前用户的昵称
            negTea.setUpdateBy(SecurityUtils.getLoginUser().getUser().getNickName());
            // 设置创建时间为当前时间
            negTea.setCreateTime(DateUtils.getNowDate());
            // 设置更新时间为当前时间
            negTea.setUpdateTime(DateUtils.getNowDate());
            // 调用Mapper接口插入茶水信息
            int result = negTeaMapper.insertNegTea(negTea);
            // 如果插入成功，则同步添加到数据字典
            if (result > 0) {
                // 创建字典数据对象
                SysDictData dictData = new SysDictData();
                // 设置字典标签为茶水名称
                dictData.setDictLabel(negTea.getTeaName());
                // 设置字典类型为"on_negtea_name"
                dictData.setDictType("on_negtea_name");
                // 设置字典键值为茶水ID
                dictData.setDictValue(String.valueOf(negTea.getTeaId()));
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
    }

    /**
     * 修改茶水管理
     *
     * @param negTea 茶水管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateNegTea(NegTea negTea)
    {
        // 设置更新者为当前用户的昵称
        negTea.setUpdateBy(SecurityUtils.getLoginUser().getUser().getNickName());
        // 设置更新时间为当前时间
        negTea.setUpdateTime(DateUtils.getNowDate());
        // 调用Mapper接口更新茶水信息
        int result = negTeaMapper.updateNegTea(negTea);
        // 如果更新成功，则同步更新数据字典中的茶水信息
        if (result > 0) {
            // 根据字典类型和字典键值精确查找记录
            SysDictData dictData = dictDataService.selectDictDataByTypeAndValue("on_negtea_name", String.valueOf(negTea.getTeaId()));
            // 如果找到记录，则更新字典数据
            if (dictData != null) {
                // 更新字典标签为新的茶水名称
                dictData.setDictLabel(negTea.getTeaName());
                // 调用字典数据服务更新字典数据
                dictDataService.updateDictData(dictData);
            }
        }
        // 返回操作结果
        return result;
    }

    /**
     * 批量删除茶水管理
     *
     * @param teaIds 需要删除的茶水管理主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteNegTeaByTeaIds(Long[] teaIds)
    {
        // 遍历需要删除的茶水ID
        for (Long teaId : teaIds) {
            // 根据字典类型和字典键值精确查找记录
            SysDictData dictData = dictDataService.selectDictDataByTypeAndValue("on_negtea_name", String.valueOf(teaId));
            // 如果找到记录，则删除数据字典中的记录
            if (dictData != null) {
                // 调用字典数据服务删除字典数据
                dictDataService.deleteDictDataByIds(new Long[]{dictData.getDictCode()});
            }
        }
        // 调用Mapper接口批量删除茶水信息
        return negTeaMapper.deleteNegTeaByTeaIds(teaIds);
    }

    /**
     * 删除茶水管理信息
     *
     * @param teaId 茶水管理主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteNegTeaByTeaId(Long teaId)
    {
        // 根据字典类型和字典键值精确查找记录
        SysDictData dictData = dictDataService.selectDictDataByTypeAndValue("on_negtea_name", String.valueOf(teaId));
        // 如果找到记录，则删除数据字典中的记录
        if (dictData != null) {
            // 调用字典数据服务删除字典数据
            dictDataService.deleteDictDataByIds(new Long[]{dictData.getDictCode()});
        }
        // 调用Mapper接口删除茶水信息
        return negTeaMapper.deleteNegTeaByTeaId(teaId);
    }
}