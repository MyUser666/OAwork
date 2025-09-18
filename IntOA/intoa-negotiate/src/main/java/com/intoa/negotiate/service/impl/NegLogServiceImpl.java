package com.intoa.negotiate.service.impl;

import java.util.Date;
import java.util.List;
import com.intoa.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.intoa.negotiate.mapper.NegLogMapper;
import com.intoa.negotiate.domain.NegLog;
import com.intoa.negotiate.service.INegLogService;
import com.intoa.negotiate.service.IRoomResourceLockService;

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
    
    @Autowired
    private IRoomResourceLockService roomResourceLockService;

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
    @Transactional(rollbackFor = Exception.class)
    public int insertNegLog(NegLog negLog)
    {
        // 设置创建时间
        negLog.setCreateTime(DateUtils.getNowDate());
        
        // 设置默认状态为已确认
        if (negLog.getStatus() == null || negLog.getStatus().isEmpty()) {
            negLog.setStatus("1"); // 已确认
        }
        
        // 插入预约记录以获取logId
        int result = negLogMapper.insertNegLog(negLog);
        
        if (result > 0) {
            // 获取预约日期
            Date appointmentDate = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd", negLog.getStartTime()));
            
            // 正式锁定房间资源
            boolean roomLocked = roomResourceLockService.lockRoom(
                    negLog.getLogRoomName(), 
                    appointmentDate, 
                    negLog.getStartTime(), 
                    negLog.getEndTime(), 
                    negLog.getLogId());
            
            if (!roomLocked) {
                throw new RuntimeException("房间资源锁定失败");
            }
            
            // 如果有茶水预约，也应锁定茶水库存（此处简化处理，实际项目中应实现茶水库存锁定逻辑）
            /*
            if (negLog.getLogTeaName() != null && !negLog.getLogTeaName().isEmpty() && negLog.getLogTeaNum() != null && negLog.getLogTeaNum() > 0) {
                boolean teaLocked = teaStockService.lockTea(negLog.getLogTeaName(), negLog.getLogTeaNum(), negLog.getLogId());
                if (!teaLocked) {
                    // 如果茶水锁定失败，需要释放房间资源
                    roomResourceLockService.releaseRoom(negLog.getLogRoomName(), appointmentDate, negLog.getStartTime(), negLog.getEndTime());
                    throw new RuntimeException("茶水库存锁定失败");
                }
            }
            */
        }
        
        return result;
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
