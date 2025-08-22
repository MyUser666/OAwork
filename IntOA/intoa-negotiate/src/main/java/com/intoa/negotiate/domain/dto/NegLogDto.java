package com.intoa.negotiate.domain.dto;



import com.intoa.common.annotation.Excel;
import java.util.Date;

/**
 * 预约信息查询数据传输对象
 */
public class NegLogDto {
    private static final long serialVersionUID = 1L;

    /** 预约ID */
    private Long logId;

    /** 预约标题 */
    @Excel(name = "预约标题")
    private String title;

    /** 房间ID */
    private Long roomId;

    /** 房间名称 (来自 oa_neg_room 表) */
    @Excel(name = "洽谈室")
    private String roomName; // 核心：添加关联表字段

    /** 律师用户ID */
    private Long userId;

    /** 律师姓名 (来自 sys_user 表) - 未来可以轻松扩展 */
    @Excel(name = "预约律师")
    private String userName;

    /** 当事人姓名 */
    @Excel(name = "当事人")
    private String clientName;

    /** 开始时间 */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 状态（0待确认 1已确认 2已签到 3已完成 4已取消） */
    @Excel(name = "状态", readConverterExp = "0=待确认,1=已确认,2=已签到,3=已完成,4=已取消")
    private String status;

    // --- 标准的 Getter 和 Setter 方法 ---
    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; } // Getter for roomName
    public void setRoomName(String roomName) { this.roomName = roomName; } // Setter for roomName

    //  Getter 和 Setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}