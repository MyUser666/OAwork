package com.intoa.negotiate.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.intoa.common.annotation.Excel;
import com.intoa.common.core.domain.BaseEntity;

/**
 * 茶水管理对象 oa_neg_tea
 *
 * @author beihai
 * @date 2025-08-21
 */
public class NegTea extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 茶水主键ID */
    private Long teaId;

    /** 茶水名称 */
    @Excel(name = "茶水名称")
    private String teaName;

    /** 分类（如茶类、咖啡、果汁） */
    private String category;

    /** 库存数量 */
    @Excel(name = "库存数量")
    private Long stockQuantity;

    /** 状态（0可用 1缺货） */
    @Excel(name = "状态", readConverterExp = "0=可用,1=缺货")
    private String status;

    public void setTeaId(Long teaId)
    {
        this.teaId = teaId;
    }

    public Long getTeaId()
    {
        return teaId;
    }

    public void setTeaName(String teaName)
    {
        this.teaName = teaName;
    }

    public String getTeaName()
    {
        return teaName;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }

    public void setStockQuantity(Long stockQuantity)
    {
        this.stockQuantity = stockQuantity;
    }

    public Long getStockQuantity()
    {
        return stockQuantity;
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
            .append("teaId", getTeaId())
            .append("teaName", getTeaName())
            .append("category", getCategory())
            .append("stockQuantity", getStockQuantity())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
