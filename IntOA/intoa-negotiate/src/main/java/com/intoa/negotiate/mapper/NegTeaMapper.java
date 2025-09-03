package com.intoa.negotiate.mapper;

import java.util.List;
import com.intoa.negotiate.domain.NegTea;
import org.apache.ibatis.annotations.Mapper;

/**
 * 茶水管理Mapper接口
 *
 * @author beihai
 * @date 2025-08-22
 */
@Mapper
public interface NegTeaMapper
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
     * 删除茶水管理
     *
     * @param teaId 茶水管理主键
     * @return 结果
     */
    public int deleteNegTeaByTeaId(Long teaId);

    /**
     * 批量删除茶水管理
     *
     * @param teaIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNegTeaByTeaIds(Long[] teaIds);
}