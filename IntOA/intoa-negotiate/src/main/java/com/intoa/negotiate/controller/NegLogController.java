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
import com.intoa.negotiate.domain.NegLog;
import com.intoa.negotiate.service.INegLogService;
import com.intoa.common.utils.poi.ExcelUtil;
import com.intoa.common.core.page.TableDataInfo;

/**
 * 预约管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/negotiatelog/neglog")
public class NegLogController extends BaseController
{
    @Autowired
    private INegLogService negLogService;

    /**
     * 查询预约管理列表
     */
    @PreAuthorize("@ss.hasPermi('negotiatelog:neglog:list')")
    @GetMapping("/list")
    public TableDataInfo list(NegLog negLog)
    {
        startPage();
        List<NegLog> list = negLogService.selectNegLogList(negLog);
        return getDataTable(list);
    }

    /**
     * 导出预约管理列表
     */
    @PreAuthorize("@ss.hasPermi('negotiatelog:neglog:export')")
    @Log(title = "预约管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NegLog negLog)
    {
        List<NegLog> list = negLogService.selectNegLogList(negLog);
        ExcelUtil<NegLog> util = new ExcelUtil<NegLog>(NegLog.class);
        util.exportExcel(response, list, "预约管理数据");
    }

    /**
     * 获取预约管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('negotiatelog:neglog:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return success(negLogService.selectNegLogByLogId(logId));
    }

    /**
     * 新增预约管理
     */
    @PreAuthorize("@ss.hasPermi('negotiatelog:neglog:add')")
    @Log(title = "预约管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NegLog negLog)
    {
        return toAjax(negLogService.insertNegLog(negLog));
    }

    /**
     * 修改预约管理
     */
    @PreAuthorize("@ss.hasPermi('negotiatelog:neglog:edit')")
    @Log(title = "预约管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NegLog negLog)
    {
        return toAjax(negLogService.updateNegLog(negLog));
    }

    /**
     * 删除预约管理
     */
    @PreAuthorize("@ss.hasPermi('negotiatelog:neglog:remove')")
    @Log(title = "预约管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(negLogService.deleteNegLogByLogIds(logIds));
    }
}
