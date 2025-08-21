package com.intoa.negotiate.service.impl;

import java.util.List;
import com.intoa.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.intoa.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.intoa.negotiate.domain.NegLogTea;
import com.intoa.negotiate.mapper.NegLogMapper;
import com.intoa.negotiate.domain.NegLog;
import com.intoa.negotiate.service.INegLogService;

/**
 * 预约管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class NegLogServiceImpl implements INegLogService 
{
    @Autowired
    private NegLogMapper negLogMapper;

    /**
     * 查询预约管理
     * 
     * @param logId 预约管理主键
     * @return 预约管理
     */
    @Override
    public NegLog selectNegLogByLogId(Long logId)
    {
        return negLogMapper.selectNegLogByLogId(logId);
    }

    /**
     * 查询预约管理列表
     * 
     * @param negLog 预约管理
     * @return 预约管理
     */
    @Override
    public List<NegLog> selectNegLogList(NegLog negLog)
    {
        return negLogMapper.selectNegLogList(negLog);
    }

    /**
     * 新增预约管理
     * 
     * @param negLog 预约管理
     * @return 结果
     */
    @Transactional
    @Override
    public int insertNegLog(NegLog negLog)
    {
        negLog.setCreateTime(DateUtils.getNowDate());
        int rows = negLogMapper.insertNegLog(negLog);
        insertNegLogTea(negLog);
        return rows;
    }

    /**
     * 修改预约管理
     * 
     * @param negLog 预约管理
     * @return 结果
     */
    @Transactional
    @Override
    public int updateNegLog(NegLog negLog)
    {
        negLog.setUpdateTime(DateUtils.getNowDate());
        negLogMapper.deleteNegLogTeaByLogId(negLog.getLogId());
        insertNegLogTea(negLog);
        return negLogMapper.updateNegLog(negLog);
    }

    /**
     * 批量删除预约管理
     * 
     * @param logIds 需要删除的预约管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteNegLogByLogIds(Long[] logIds)
    {
        negLogMapper.deleteNegLogTeaByLogIds(logIds);
        return negLogMapper.deleteNegLogByLogIds(logIds);
    }

    /**
     * 删除预约管理信息
     * 
     * @param logId 预约管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteNegLogByLogId(Long logId)
    {
        negLogMapper.deleteNegLogTeaByLogId(logId);
        return negLogMapper.deleteNegLogByLogId(logId);
    }

    /**
     * 新增预约茶水关联信息
     * 
     * @param negLog 预约管理对象
     */
    public void insertNegLogTea(NegLog negLog)
    {
        List<NegLogTea> negLogTeaList = negLog.getNegLogTeaList();
        Long logId = negLog.getLogId();
        if (StringUtils.isNotNull(negLogTeaList))
        {
            List<NegLogTea> list = new ArrayList<NegLogTea>();
            for (NegLogTea negLogTea : negLogTeaList)
            {
                negLogTea.setLogId(logId);
                list.add(negLogTea);
            }
            if (list.size() > 0)
            {
                negLogMapper.batchNegLogTea(list);
            }
        }
    }
}
