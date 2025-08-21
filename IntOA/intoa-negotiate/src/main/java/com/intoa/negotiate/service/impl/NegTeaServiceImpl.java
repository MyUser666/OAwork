package com.intoa.negotiate.service.impl;

import java.util.List;
import com.intoa.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intoa.negotiate.mapper.NegTeaMapper;
import com.intoa.negotiate.domain.NegTea;
import com.intoa.negotiate.service.INegTeaService;

/**
 * 茶水管理Service业务层处理
 * 
 * @author beihai
 * @date 2025-08-21
 */
@Service
public class NegTeaServiceImpl implements INegTeaService 
{
    @Autowired
    private NegTeaMapper negTeaMapper;

    /**
     * 查询茶水管理
     * 
     * @param teaId 茶水管理主键
     * @return 茶水管理
     */
    @Override
    public NegTea selectNegTeaByTeaId(Long teaId)
    {
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
        return negTeaMapper.selectNegTeaList(negTea);
    }

    /**
     * 新增茶水管理
     * 
     * @param negTea 茶水管理
     * @return 结果
     */
    @Override
    public int insertNegTea(NegTea negTea)
    {
        negTea.setCreateTime(DateUtils.getNowDate());
        return negTeaMapper.insertNegTea(negTea);
    }

    /**
     * 修改茶水管理
     * 
     * @param negTea 茶水管理
     * @return 结果
     */
    @Override
    public int updateNegTea(NegTea negTea)
    {
        negTea.setUpdateTime(DateUtils.getNowDate());
        return negTeaMapper.updateNegTea(negTea);
    }

    /**
     * 批量删除茶水管理
     * 
     * @param teaIds 需要删除的茶水管理主键
     * @return 结果
     */
    @Override
    public int deleteNegTeaByTeaIds(Long[] teaIds)
    {
        return negTeaMapper.deleteNegTeaByTeaIds(teaIds);
    }

    /**
     * 删除茶水管理信息
     * 
     * @param teaId 茶水管理主键
     * @return 结果
     */
    @Override
    public int deleteNegTeaByTeaId(Long teaId)
    {
        return negTeaMapper.deleteNegTeaByTeaId(teaId);
    }
}
