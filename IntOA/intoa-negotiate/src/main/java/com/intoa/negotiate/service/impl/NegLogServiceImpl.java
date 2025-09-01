package com.intoa.negotiate.service.impl;

import java.util.List;
import com.intoa.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.intoa.negotiate.mapper.NegLogMapper;
import com.intoa.negotiate.domain.NegLog;
import com.intoa.negotiate.service.INegLogService;

/**
 * 预约管理Service业务层处理
 * 
 * @author beihai
 * @date 2025-08-28
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
        /**
     * 获取当前登录用户名
     * @return 当前登录用户名
     */
//    private String getCurrentUserName() {
//        // 实现获取当前登录用户名的逻辑，具体实现取决于你的安全框架
//        // 这里只是一个示例，实际需要根据项目情况调整
//        return SecurityContextHolder.getContext().getAuthentication().getName();
//    }
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
        // 自动填充当前登录用户的昵称
        if (negLog.getNickName() == null || negLog.getNickName().isEmpty()) {
            String currentUserName = getCurrentUserName(); // 需要实现获取当前登录用户名的方法
            negLog.setNickName(currentUserName);
        }

        negLog.setCreateTime(DateUtils.getNowDate());
        return negLogMapper.insertNegLog(negLog);
    }

    /**
     * 获取当前登录用户名
     * @return 当前登录用户名
     */
    private String getCurrentUserName() {
        // 实现获取当前登录用户名的逻辑，具体实现取决于你的安全框架
        // 这里只是一个示例，实际需要根据项目情况调整
        return SecurityContextHolder.getContext().getAuthentication().getName();
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