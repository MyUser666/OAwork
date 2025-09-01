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
 * @date 2025-08-28
 */
public class NegLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long logId;

    /** 预约标题 */
    @Excel(name = "预约标题")
    private String title;

    /** 房间ID */
    private Long roomId;

    /** 房间名称（创建时的快照） */
    @Excel(name = "房间名称", readConverterExp = "创=建时的快照")
    private String roomName;

    /** 律师用户ID（关联 sys_user.user_id） */
    private Long userId;

    /** 律师角色ID（关联 sys_role.role_id）- 用于数据权限 */
    private Long roleId;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 当事人姓名 */
    @Excel(name = "当事人姓名")
    private String clientName;

    /** 当事人联系方式 */
    @Excel(name = "当事人联系方式")
    private String clientContact;

    /** 相关案号/案由 */
    @Excel(name = "相关案号/案由")
    private String caseReference;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
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

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }

    public void setRoomName(String roomName) 
    {
        this.roomName = roomName;
    }

    public String getRoomName() 
    {
        return roomName;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setRoleId(Long roleId) 
    {
        this.roleId = roleId;
    }

    public Long getRoleId() 
    {
        return roleId;
    }

    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
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
            .append("title", getTitle())
            .append("roomId", getRoomId())
            .append("roomName", getRoomName())
            .append("userId", getUserId())
            .append("roleId", getRoleId())
            .append("nickName", getNickName())
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