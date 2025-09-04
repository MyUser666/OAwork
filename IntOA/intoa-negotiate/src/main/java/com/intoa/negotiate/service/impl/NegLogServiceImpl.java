package com.intoa.negotiate.service.impl;

import java.util.List;
import com.intoa.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intoa.negotiate.mapper.NegLogMapper;
import com.intoa.negotiate.domain.NegLog;
import com.intoa.negotiate.service.INegLogService;

/**
 * 预约管理Service业务层处理
 *
 * @author beihai
 * @date 2025-09-03
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
    @Override
    public int insertNegLog(NegLog negLog)
    {
        negLog.setCreateTime(DateUtils.getNowDate());
        return negLogMapper.insertNegLog(negLog);
    }

    /**
     * 修改预约管理
     *
     * @param negLog 预约管理
     * @return 结果
     */
    @Override
    public int updateNegLog(NegLog negLog)
    {
        negLog.setUpdateTime(DateUtils.getNowDate());
        return negLogMapper.updateNegLog(negLog);
    }

    /**
     * 批量删除预约管理
     *
     * @param logIds 需要删除的预约管理主键
     * @return 结果
     */
    @Override
    public int deleteNegLogByLogIds(Long[] logIds)
    {
        return negLogMapper.deleteNegLogByLogIds(logIds);
    }

    /**
     * 删除预约管理信息
     *
     * @param logId 预约管理主键
     * @return 结果
     */
    @Override
    public int deleteNegLogByLogId(Long logId)
    {
        return negLogMapper.deleteNegLogByLogId(logId);
    }
}
