package com.intoa.negotiate.mapper;

import java.util.List;
import com.intoa.negotiate.domain.NegLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预约管理Mapper接口
 * 
 * @author beihai
 * @date 2025-08-28
 */
@Mapper
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
}