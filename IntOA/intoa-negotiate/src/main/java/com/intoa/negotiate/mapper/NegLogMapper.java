package com.intoa.negotiate.mapper;

import java.util.List;
import com.intoa.negotiate.domain.NegLog;
import com.intoa.negotiate.domain.NegLogTea;

/**
 * 预约管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public interface NegLogMapper 
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
     * 删除预约管理
     * 
     * @param logId 预约管理主键
     * @return 结果
     */
    public int deleteNegLogByLogId(Long logId);

    /**
     * 批量删除预约管理
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNegLogByLogIds(Long[] logIds);

    /**
     * 批量删除预约茶水关联
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNegLogTeaByLogIds(Long[] logIds);
    
    /**
     * 批量新增预约茶水关联
     * 
     * @param negLogTeaList 预约茶水关联列表
     * @return 结果
     */
    public int batchNegLogTea(List<NegLogTea> negLogTeaList);
    

    /**
     * 通过预约管理主键删除预约茶水关联信息
     * 
     * @param logId 预约管理ID
     * @return 结果
     */
    public int deleteNegLogTeaByLogId(Long logId);
}
