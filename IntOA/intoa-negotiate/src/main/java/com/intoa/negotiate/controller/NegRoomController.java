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
import com.intoa.negotiate.domain.NegRoom;
import com.intoa.negotiate.service.INegRoomService;
import com.intoa.common.utils.poi.ExcelUtil;
import com.intoa.common.core.page.TableDataInfo;

/**
 * 洽谈室管理Controller
 *
 * @author beihai
 * @date 2025-08-29
 */
@RestController
@RequestMapping("/negotiateroom/negroom")
public class NegRoomController extends BaseController
{
    @Autowired
    private INegRoomService negRoomService;

    /**
     * 查询洽谈室管理列表
     */
    @PreAuthorize("@ss.hasPermi('negotiateroom:negroom:list')")
    @GetMapping("/list")
    public TableDataInfo list(NegRoom negRoom)
    {
        startPage();
        List<NegRoom> list = negRoomService.selectNegRoomList(negRoom);
        return getDataTable(list);
    }

    /**
     * 导出洽谈室管理列表
     */
    @PreAuthorize("@ss.hasPermi('negotiateroom:negroom:export')")
    @Log(title = "洽谈室管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NegRoom negRoom)
    {
        List<NegRoom> list = negRoomService.selectNegRoomList(negRoom);
        ExcelUtil<NegRoom> util = new ExcelUtil<NegRoom>(NegRoom.class);
        util.exportExcel(response, list, "洽谈室管理数据");
    }

    /**
     * 获取洽谈室管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('negotiateroom:negroom:query')")
    @GetMapping(value = "/{roomId}")
    public AjaxResult getInfo(@PathVariable("roomId") Long roomId)
    {
        return success(negRoomService.selectNegRoomByRoomId(roomId));
    }

    /**
     * 新增洽谈室管理
     */
    @PreAuthorize("@ss.hasPermi('negotiateroom:negroom:add')")
    @Log(title = "洽谈室管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NegRoom negRoom)
    {
        return toAjax(negRoomService.insertNegRoom(negRoom));
    }

    /**
     * 修改洽谈室管理
     */
    @PreAuthorize("@ss.hasPermi('negotiateroom:negroom:edit')")
    @Log(title = "洽谈室管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NegRoom negRoom)
    {
        return toAjax(negRoomService.updateNegRoom(negRoom));
    }

    /**
     * 删除洽谈室管理
     */
    @PreAuthorize("@ss.hasPermi('negotiateroom:negroom:remove')")
    @Log(title = "洽谈室管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roomIds}")
    public AjaxResult remove(@PathVariable Long[] roomIds)
    {
        return toAjax(negRoomService.deleteNegRoomByRoomIds(roomIds));
    }
}
