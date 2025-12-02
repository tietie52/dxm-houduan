package com.ruoyi.system.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 消息分析报告业务对象 message_analysis_report
 *
 * @author Lion Li
 * @date 2024-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageAnalysisReportBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 消息ID
     */
    @NotNull(message = "消息ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long messageId;

    /**
     * 消息日期
     */
    @NotNull(message = "消息日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private LocalDateTime messageDate;

    /**
     * 分析结果(JSON)
     */
    @NotBlank(message = "分析结果不能为空", groups = { AddGroup.class, EditGroup.class })
    private String analysisResult;

    /**
     * 持仓快照(JSON)
     */
    @NotBlank(message = "持仓快照不能为空", groups = { AddGroup.class, EditGroup.class })
    private String holdingSnapshot;

    /**
     * 调整方案(JSON)
     */
    @NotBlank(message = "调整方案不能为空", groups = { AddGroup.class, EditGroup.class })
    private String adjustmentPlan;

    /**
     * 报告生成时间
     */
    @NotNull(message = "报告生成时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private LocalDateTime reportTime;

    /**
     * 审核状态(0-待审核,1-已通过,2-已拒绝)
     */
    @NotNull(message = "审核状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer auditStatus;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核意见
     */
    private String auditComments;

    /**
     * 执行状态(0-未执行,1-已执行)
     */
    @NotNull(message = "执行状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer executed;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

}