package com.intoa.negotiate.service;

import java.util.List;
import com.intoa.negotiate.domain.NegLog;

/**
 * 预约管理Service接口
 *
 * @author beihai
 * @date 2025-09-03
 */
public interface INegLogService
{
    /**
     * 查询预约管理
     *
     * @param logId 预约管理主键
     * @return 预约管理
     */
    public NegLog selectNegLogByLogId(Long logId);

    /**
     * 查询预约管理列表
     *
     * @param negLog 预约管理
     * @return 预约管理集合
     */
    public List<NegLog> selectNegLogList(NegLog negLog);

    /**
     * 新增预约管理
     *
     * @param negLog 预约管理
     * @return 结果
     */
    public int insertNegLog(NegLog negLog);

    /**
     * 修改预约管理
     *
     * @param negLog 预约管理
     * @return 结果
     */
    public int updateNegLog(NegLog negLog);

    /**
     * 批量删除预约管理
     *
     * @param logIds 需要删除的预约管理主键集合
     * @return 结果
     */
    public int deleteNegLogByLogIds(Long[] logIds);

    /**
     * 删除预约管理信息
     *
     * @param logId 预约管理主键
     * @return 结果
     */
    public int deleteNegLogByLogId(Long logId);
}
