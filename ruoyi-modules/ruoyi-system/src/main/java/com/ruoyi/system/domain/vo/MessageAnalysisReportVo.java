package com.ruoyi.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.system.domain.entity.MessageAnalysisReport;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 消息分析报告视图对象 message_analysis_report
 *
 * @author Lion Li
 * @date 2024-11-04
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MessageAnalysisReport.class)
public class MessageAnalysisReportVo extends BaseVo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 消息ID
     */
    @ExcelProperty(value = "消息ID")
    private Long messageId;

    /**
     * 消息日期
     */
    @ExcelProperty(value = "消息日期")
    private LocalDateTime messageDate;

    /**
     * 分析结果(JSON)
     */
    @ExcelProperty(value = "分析结果")
    private String analysisResult;

    /**
     * 持仓快照(JSON)
     */
    @ExcelProperty(value = "持仓快照")
    private String holdingSnapshot;

    /**
     * 调整方案(JSON)
     */
    @ExcelProperty(value = "调整方案")
    private String adjustmentPlan;

    /**
     * 报告生成时间
     */
    @ExcelProperty(value = "报告生成时间")
    private LocalDateTime reportTime;

    /**
     * 审核状态(0-待审核,1-已通过,2-已拒绝)
     */
    @ExcelProperty(value = "审核状态")
    private Integer auditStatus;

    /**
     * 审核人
     */
    @ExcelProperty(value = "审核人")
    private String auditor;

    /**
     * 审核时间
     */
    @ExcelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    /**
     * 审核意见
     */
    @ExcelProperty(value = "审核意见")
    private String auditComments;

    /**
     * 执行状态(0-未执行,1-已执行)
     */
    @ExcelProperty(value = "执行状态")
    private Integer executed;

    /**
     * 执行时间
     */
    @ExcelProperty(value = "执行时间")
    private LocalDateTime executeTime;

}