package com.intoa.negotiate.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.intoa.common.annotation.Excel;
import com.intoa.common.core.domain.BaseEntity;

/**
 * 洽谈室管理对象 oa_neg_room
 * 
 * @author beihai
 * @date 2025-08-21
 */
public class NegRoom extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 洽谈室主键ID */
    private Long roomId;

    /** 房间名称 */
    @Excel(name = "房间名称")
    private String roomName;

    /** 位置 */
    @Excel(name = "位置")
    private String location;

    /** 容纳人数 */
    @Excel(name = "容纳人数")
    private Long capacity;

    /** 设备信息（如投影仪、白板） */
    @Excel(name = "设备信息", readConverterExp = "如=投影仪、白板")
    private String equipment;

    /** 缓冲时间（分钟） */
    @Excel(name = "缓冲时间", readConverterExp = "分=钟")
    private Long bufferTime;

    /** 状态 （0=可用,1=禁用）*/
    @Excel(name = "状态",readConverterExp = "0=可用,1=禁用")
    private String status;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

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

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setCapacity(Long capacity) 
    {
        this.capacity = capacity;
    }

    public Long getCapacity() 
    {
        return capacity;
    }

    public void setEquipment(String equipment) 
    {
        this.equipment = equipment;
    }

    public String getEquipment() 
    {
        return equipment;
    }

    public void setBufferTime(Long bufferTime) 
    {
        this.bufferTime = bufferTime;
    }

    public Long getBufferTime() 
    {
        return bufferTime;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roomId", getRoomId())
            .append("roomName", getRoomName())
            .append("location", getLocation())
            .append("capacity", getCapacity())
            .append("equipment", getEquipment())
            .append("bufferTime", getBufferTime())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("orderNum", getOrderNum())
            .append("remark", getRemark())
            .toString();
    }
}
