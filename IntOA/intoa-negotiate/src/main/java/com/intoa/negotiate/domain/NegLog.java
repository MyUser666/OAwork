package com.intoa.negotiate.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.intoa.common.annotation.Excel;
import com.intoa.common.core.domain.BaseEntity;

/**
 * 预约管理对象 oa_neg_log
 * 
 * @author beihai
 * @date 2025-09-03
 */
public class NegLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long logId;

    /** 预约房间（创建预约时的快照）
     */
    @Excel(name = "预约房间", readConverterExp = "创=建预约时的快照")
    private String logRoomName;

    /** 预约标题 */
    @Excel(name = "预约标题")
    private String title;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String logNickName;

    /** 预约茶水（创建预约时的快照） */
    @Excel(name = "预约茶水", readConverterExp = "创=建预约时的快照")
    private String logTeaName;

    /** 预约茶水数量（创建预约时的快照） */
    @Excel(name = "预约茶水数量", readConverterExp = "创=建预约时的快照")
    private Long logTeaNum;

    /** 当事人姓名 */
    @Excel(name = "当事人姓名")
    private String clientName;

    /** 当事人联系方式 */
    private String clientContact;

    /** 相关案号/案由 */
    @Excel(name = "相关案号/案由")
    private String caseReference;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 状态（0待确认 1已确认 2已签到 3已完成 4已取消） */
    @Excel(name = "状态", readConverterExp = "0=待确认,1=已确认,2=已签到,3=已完成,4=已取消")
    private String status;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }
    public void setLogRoomName(String logRoomName) 
    {
        this.logRoomName = logRoomName;
    }

    public String getLogRoomName() 
    {
        return logRoomName;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setLogNickName(String logNickName) 
    {
        this.logNickName = logNickName;
    }

    public String getLogNickName() 
    {
        return logNickName;
    }
    public void setLogTeaName(String logTeaName) 
    {
        this.logTeaName = logTeaName;
    }

    public String getLogTeaName() 
    {
        return logTeaName;
    }
    public void setLogTeaNum(Long logTeaNum) 
    {
        this.logTeaNum = logTeaNum;
    }

    public Long getLogTeaNum() 
    {
        return logTeaNum;
    }
    public void setClientName(String clientName) 
    {
        this.clientName = clientName;
    }

    public String getClientName() 
    {
        return clientName;
    }
    public void setClientContact(String clientContact) 
    {
        this.clientContact = clientContact;
    }

    public String getClientContact() 
    {
        return clientContact;
    }
    public void setCaseReference(String caseReference) 
    {
        this.caseReference = caseReference;
    }

    public String getCaseReference() 
    {
        return caseReference;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("logRoomName", getLogRoomName())
            .append("title", getTitle())
            .append("logNickName", getLogNickName())
            .append("logTeaName", getLogTeaName())
            .append("logTeaNum", getLogTeaNum())
            .append("clientName", getClientName())
            .append("clientContact", getClientContact())
            .append("caseReference", getCaseReference())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}