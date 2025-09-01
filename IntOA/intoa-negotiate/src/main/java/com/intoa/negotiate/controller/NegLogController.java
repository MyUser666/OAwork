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
import com.intoa.negotiate.domain.NegRoom;
import com.intoa.negotiate.service.INegLogService;
import com.intoa.negotiate.mapper.NegRoomMapper;
import com.intoa.common.utils.poi.ExcelUtil;
import com.intoa.common.core.page.TableDataInfo;
import com.intoa.common.core.domain.entity.SysUser;
import com.intoa.common.utils.SecurityUtils;

/**
 * 预约管理Controller
 * 
 * @author beihai
 * @date 2025-08-28
 */
@RestController
@RequestMapping("/negotiatelog/neglog")
public class NegLogController extends BaseController
{
    @Autowired
    private INegLogService negLogService;
    
    @Autowired
    private NegRoomMapper negRoomMapper;

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
     * 获取所有可用房间列表
     */
    @PreAuthorize("@ss.hasPermi('negotiatelog:neglog:list')")
    @GetMapping("/availableRooms")
    public AjaxResult getAvailableRooms()
    {
        NegRoom room = new NegRoom();
        room.setStatus("0"); // 只查询可用状态的房间
        List<NegRoom> rooms = negRoomMapper.selectNegRoomList(room);
        return AjaxResult.success(rooms);
    }

    /**
     * 获取指定房间的详细信息
     */
    @PreAuthorize("@ss.hasPermi('negotiatelog:neglog:list')")
    @GetMapping("/room/{roomId}")
    public AjaxResult getRoomDetail(@PathVariable("roomId") Long roomId)
    {
        NegRoom room = negRoomMapper.selectNegRoomByRoomId(roomId);
        return AjaxResult.success(room);
    }

    /**
     * 新增预约管理
     */
    @PreAuthorize("@ss.hasPermi('negotiatelog:neglog:add')")
    @Log(title = "预约管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NegLog negLog)
    {
        // 自动填充当前登录用户的昵称
        SysUser user = SecurityUtils.getLoginUser().getUser();
        negLog.setNickName(user.getNickName());
        negLog.setUserId(user.getUserId());
        
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