package com.intoa.negotiate.service;

import java.util.List;
import com.intoa.negotiate.domain.NegTea;

/**
 * 茶水管理Service接口
 *
 * @author beihai
 * @date 2025-08-22
 */
public interface INegTeaService
{
    /**
     * 查询茶水管理
     *
     * @param teaId 茶水管理主键
     * @return 茶水管理
     */
    public NegTea selectNegTeaByTeaId(Long teaId);

    /**
     * 查询茶水管理列表
     *
     * @param negTea 茶水管理
     * @return 茶水管理集合
     */
    public List<NegTea> selectNegTeaList(NegTea negTea);

    /**
     * 新增茶水管理
     *
     * @param negTea 茶水管理
     * @return 结果
     */
    public int insertNegTea(NegTea negTea);

    /**
     * 修改茶水管理
     *
     * @param negTea 茶水管理
     * @return 结果
     */
    public int updateNegTea(NegTea negTea);

    /**
     * 批量删除茶水管理
     *
     * @param teaIds 需要删除的茶水管理主键集合
     * @return 结果
     */
    public int deleteNegTeaByTeaIds(Long[] teaIds);

    /**
     * 删除茶水管理信息
     *
     * @param teaId 茶水管理主键
     * @return 结果
     */
    public int deleteNegTeaByTeaId(Long teaId);
}
