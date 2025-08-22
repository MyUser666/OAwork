package com.intoa.negotiate.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.intoa.common.annotation.Log;
import com.intoa.common.core.controller.BaseController;
import com.intoa.common.core.domain.AjaxResult;
import com.intoa.common.enums.BusinessType;
import com.intoa.negotiate.domain.NegTea;
import com.intoa.negotiate.service.INegTeaService;
import com.intoa.common.utils.poi.ExcelUtil;
import com.intoa.common.core.page.TableDataInfo;

/**
 * 茶水管理Controller
 *
 * @author beihai
 * @date 2025-08-22
 */
@RestController
@RequestMapping("/negotiatetea/negtea")
public class NegTeaController extends BaseController
{
    @Autowired
    private INegTeaService negTeaService;

    /**
     * 查询茶水管理列表
     */
    @PreAuthorize("@ss.hasPermi('negotiatetea:negtea:list')")
    @GetMapping("/list")
    public TableDataInfo list(NegTea negTea)
    {
        startPage();
        List<NegTea> list = negTeaService.selectNegTeaList(negTea);
        return getDataTable(list);
    }

    /**
     * 导出茶水管理列表
     */
    @PreAuthorize("@ss.hasPermi('negotiatetea:negtea:export')")
    @Log(title = "茶水管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NegTea negTea)
    {
        List<NegTea> list = negTeaService.selectNegTeaList(negTea);
        ExcelUtil<NegTea> util = new ExcelUtil<NegTea>(NegTea.class);
        util.exportExcel(response, list, "茶水管理数据");
    }

    /**
     * 获取茶水管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('negotiatetea:negtea:query')")
    @GetMapping(value = "/{teaId}")
    public AjaxResult getInfo(@PathVariable("teaId") Long teaId)
    {
        return success(negTeaService.selectNegTeaByTeaId(teaId));
    }

    /**
     * 新增茶水管理
     */
    @PreAuthorize("@ss.hasPermi('negotiatetea:negtea:add')")
    @Log(title = "茶水管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NegTea negTea)
    {
        return toAjax(negTeaService.insertNegTea(negTea));
    }

    /**
     * 修改茶水管理
     */
    @PreAuthorize("@ss.hasPermi('negotiatetea:negtea:edit')")
    @Log(title = "茶水管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NegTea negTea)
    {
        return toAjax(negTeaService.updateNegTea(negTea));
    }

    /**
     * 删除茶水管理
     */
    @PreAuthorize("@ss.hasPermi('negotiatetea:negtea:remove')")
    @Log(title = "茶水管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{teaIds}")
    public AjaxResult remove(@PathVariable Long[] teaIds)
    {
        return toAjax(negTeaService.deleteNegTeaByTeaIds(teaIds));
    }
}
