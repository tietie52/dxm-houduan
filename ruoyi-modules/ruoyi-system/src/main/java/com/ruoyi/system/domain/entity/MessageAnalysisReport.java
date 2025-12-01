package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 消息分析报告表 message_analysis_report
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("message_analysis_report")
public class MessageAnalysisReport extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 消息日期
     */
    private LocalDateTime messageDate;

    /**
     * 分析结果(JSON)
     */
    private String analysisResult;

    /**
     * 持仓快照(JSON)
     */
    private String holdingSnapshot;

    /**
     * 调整方案(JSON)
     */
    private String adjustmentPlan;

    /**
     * 报告生成时间
     */
    private LocalDateTime reportTime;

    /**
     * 审核状态(0-待审核,1-已通过,2-已拒绝)
     */
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
    private Integer executed;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

}