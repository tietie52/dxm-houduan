package com.ruoyi.system.controller.wms;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.system.domain.bo.MessageAnalysisReportBo;
import com.ruoyi.system.domain.entity.MessageAnalysisReport;
import com.ruoyi.system.domain.vo.MessageAnalysisReportVo;
import com.ruoyi.system.service.IMessageAnalysisReportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息分析报告Controller
 *
 * @author Lion Li
 * @date 2024-11-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/messageAnalysisReport")
public class MessageAnalysisReportController extends BaseController {

    private final IMessageAnalysisReportService messageAnalysisReportService;

    /**
     * 查询消息分析报告列表
     */
    @SaCheckPermission("wms:messageAnalysisReport:list")
    @GetMapping("/list")
    public TableDataInfo<MessageAnalysisReportVo> list(MessageAnalysisReportBo bo, PageQuery pageQuery) {
        return messageAnalysisReportService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询待审核报告列表
     */
    @SaCheckPermission("wms:messageAnalysisReport:pendingList")
    @GetMapping("/pending")
    public TableDataInfo<MessageAnalysisReportVo> pendingList(PageQuery pageQuery) {
        return messageAnalysisReportService.queryPendingList(pageQuery);
    }

    /**
     * 根据消息ID查询报告
     */
    @GetMapping("/message/{messageId}")
    public R<MessageAnalysisReportVo> getByMessageId(@PathVariable Long messageId) {
        MessageAnalysisReportVo vo = messageAnalysisReportService.queryByMessageId(messageId);
        return R.ok(vo);
    }

    /**
     * 获取报告详情
     */
    @SaCheckPermission("wms:messageAnalysisReport:query")
    @GetMapping("/{id}")
    public R<MessageAnalysisReportVo> getInfo(@PathVariable Long id) {
        return R.ok(messageAnalysisReportService.queryById(id));
    }

    /**
     * AI接收分析报告
     */
    @PostMapping("/ai/receive")
    public R<Void> receiveAiReport(@RequestBody MessageAnalysisReport report) {
        boolean success = messageAnalysisReportService.receiveAiReport(report);
        return success ? R.ok() : R.fail("接收报告失败");
    }

    /**
     * 新增消息分析报告
     */
    @SaCheckPermission("wms:messageAnalysisReport:add")
    @Log(title = "消息分析报告", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody MessageAnalysisReportBo bo) {
        return toAjax(messageAnalysisReportService.insertByBo(bo));
    }

    /**
     * 修改消息分析报告
     */
    @SaCheckPermission("wms:messageAnalysisReport:edit")
    @Log(title = "消息分析报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody MessageAnalysisReportBo bo) {
        return toAjax(messageAnalysisReportService.updateByBo(bo));
    }

    /**
     * 审核报告
     */
    @SaCheckPermission("wms:messageAnalysisReport:audit")
    @Log(title = "消息分析报告", businessType = BusinessType.UPDATE)
    @PutMapping("/audit/{id}")
    public R<Void> audit(@PathVariable Long id, @RequestBody Map<String, Object> auditInfo) {
        Integer auditStatus = (Integer) auditInfo.get("auditStatus");
        String auditor = (String) auditInfo.get("auditor");
        String comments = (String) auditInfo.get("comments");
        
        boolean success = messageAnalysisReportService.auditReport(id, auditStatus, auditor, comments);
        return success ? R.ok() : R.fail("审核失败");
    }

    /**
     * 执行调整方案
     */
    @SaCheckPermission("wms:messageAnalysisReport:execute")
    @Log(title = "消息分析报告", businessType = BusinessType.UPDATE)
    @PutMapping("/execute/{id}")
    public R<Void> execute(@PathVariable Long id) {
        boolean success = messageAnalysisReportService.executePlan(id);
        return success ? R.ok() : R.fail("执行失败，请确认报告已审核通过");
    }

    /**
     * 删除消息分析报告
     */
    @SaCheckPermission("wms:messageAnalysisReport:remove")
    @Log(title = "消息分析报告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(messageAnalysisReportService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 导出消息分析报告列表
     */
    @SaCheckPermission("wms:messageAnalysisReport:export")
    @Log(title = "消息分析报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MessageAnalysisReportBo bo, HttpServletResponse response) {
        List<MessageAnalysisReportVo> list = messageAnalysisReportService.queryList(bo);
        ExcelUtil.exportExcel(list, "消息分析报告数据", MessageAnalysisReportVo.class, response);
    }

}