package com.intoa.negotiate.mapper;

import java.util.Date;
import java.util.List;
import com.intoa.negotiate.domain.NegLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 预约管理Mapper接口
 *
 * @author beihai
 * @date 2025-09-03
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
    
    /**
     * 统计指定房间在指定时间段内存在时间冲突的预约数量
     * 
     * @param roomName 房间名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时间冲突的预约数量
     */
    public int countConflictingAppointments(@Param("roomName") String roomName, 
                                           @Param("startTime") Date startTime, 
                                           @Param("endTime") Date endTime);
}