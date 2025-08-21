package com.intoa.negotiate.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.intoa.common.annotation.Excel;
import com.intoa.common.core.domain.BaseEntity;

/**
 * 预约茶水关联对象 oa_neg_log_tea
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
public class NegLogTea extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long logTeaId;

    /** 预约ID */
    @Excel(name = "预约ID")
    private Long logId;

    /** 茶水ID */
    @Excel(name = "茶水ID")
    private Long teaId;

    /** 数量 */
    @Excel(name = "数量")
    private Long quantity;

    public void setLogTeaId(Long logTeaId) 
    {
        this.logTeaId = logTeaId;
    }

    public Long getLogTeaId() 
    {
        return logTeaId;
    }
    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }
    public void setTeaId(Long teaId) 
    {
        this.teaId = teaId;
    }

    public Long getTeaId() 
    {
        return teaId;
    }
    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logTeaId", getLogTeaId())
            .append("logId", getLogId())
            .append("teaId", getTeaId())
            .append("quantity", getQuantity())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
