package com.ruoyi.system.service;

import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.system.domain.bo.MessageAnalysisReportBo;
import com.ruoyi.system.domain.entity.MessageAnalysisReport;
import com.ruoyi.system.domain.vo.MessageAnalysisReportVo;

import java.util.Collection;
import java.util.List;

/**
 * 消息分析报告Service接口
 *
 * @author Lion Li
 * @date 2024-11-04
 */
public interface IMessageAnalysisReportService {

    /**
     * 查询消息分析报告
     */
    MessageAnalysisReportVo queryById(Long id);

    /**
     * 查询消息分析报告列表
     */
    TableDataInfo<MessageAnalysisReportVo> queryPageList(MessageAnalysisReportBo bo, PageQuery pageQuery);

    /**
     * 查询消息分析报告列表
     */
    List<MessageAnalysisReportVo> queryList(MessageAnalysisReportBo bo);

    /**
     * 新增消息分析报告
     */
    Boolean insertByBo(MessageAnalysisReportBo bo);

    /**
     * 修改消息分析报告
     */
    Boolean updateByBo(MessageAnalysisReportBo bo);

    /**
     * 校验并批量删除消息分析报告信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据消息ID查询报告
     */
    MessageAnalysisReportVo queryByMessageId(Long messageId);

    /**
     * 查询待审核报告列表
     */
    TableDataInfo<MessageAnalysisReportVo> queryPendingList(PageQuery pageQuery);

    /**
     * AI接收分析报告
     */
    Boolean receiveAiReport(MessageAnalysisReport report);

    /**
     * 审核报告
     */
    Boolean auditReport(Long id, Integer auditStatus, String auditor, String comments);

    /**
     * 执行调整方案
     */
    Boolean executePlan(Long id);

}