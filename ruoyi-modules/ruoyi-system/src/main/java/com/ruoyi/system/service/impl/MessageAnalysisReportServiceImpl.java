package com.ruoyi.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.system.domain.bo.MessageAnalysisReportBo;
import com.ruoyi.system.domain.entity.MessageAnalysisReport;
import com.ruoyi.system.domain.vo.MessageAnalysisReportVo;
import com.ruoyi.system.mapper.MessageAnalysisReportMapper;
import com.ruoyi.system.service.IMessageAnalysisReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 消息分析报告Service业务层处理
 *
 * @author Lion Li
 * @date 2024-11-04
 */
@RequiredArgsConstructor
@Service
public class MessageAnalysisReportServiceImpl implements IMessageAnalysisReportService {

    private final MessageAnalysisReportMapper messageAnalysisReportMapper;

    /**
     * 查询消息分析报告
     */
    @Override
    public MessageAnalysisReportVo queryById(Long id) {
        return messageAnalysisReportMapper.selectVoById(id);
    }

    /**
     * 查询消息分析报告列表
     */
    @Override
    public TableDataInfo<MessageAnalysisReportVo> queryPageList(MessageAnalysisReportBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MessageAnalysisReport> lqw = buildQueryWrapper(bo);
        Page<MessageAnalysisReportVo> page = messageAnalysisReportMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询消息分析报告列表
     */
    @Override
    public List<MessageAnalysisReportVo> queryList(MessageAnalysisReportBo bo) {
        LambdaQueryWrapper<MessageAnalysisReport> lqw = buildQueryWrapper(bo);
        return messageAnalysisReportMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MessageAnalysisReport> buildQueryWrapper(MessageAnalysisReportBo bo) {
        LambdaQueryWrapper<MessageAnalysisReport> lqw = Wrappers.lambdaQuery();
        lqw.eq(ObjectUtil.isNotNull(bo.getMessageId()), MessageAnalysisReport::getMessageId, bo.getMessageId());
        lqw.eq(ObjectUtil.isNotNull(bo.getAuditStatus()), MessageAnalysisReport::getAuditStatus, bo.getAuditStatus());
        lqw.eq(ObjectUtil.isNotNull(bo.getExecuted()), MessageAnalysisReport::getExecuted, bo.getExecuted());
        lqw.like(StringUtils.isNotBlank(bo.getAuditor()), MessageAnalysisReport::getAuditor, bo.getAuditor());
        lqw.between(bo.getParams().get("beginMessageDate") != null && bo.getParams().get("endMessageDate") != null,
            MessageAnalysisReport::getMessageDate, bo.getParams().get("beginMessageDate"), bo.getParams().get("endMessageDate"));
        lqw.between(bo.getParams().get("beginReportTime") != null && bo.getParams().get("endReportTime") != null,
            MessageAnalysisReport::getReportTime, bo.getParams().get("beginReportTime"), bo.getParams().get("endReportTime"));
        lqw.orderByDesc(MessageAnalysisReport::getCreateTime);
        return lqw;
    }

    /**
     * 新增消息分析报告
     */
    @Override
    public Boolean insertByBo(MessageAnalysisReportBo bo) {
        MessageAnalysisReport add = MapstructUtils.convert(bo, MessageAnalysisReport.class);
        validEntityBeforeSave(add);
        boolean flag = messageAnalysisReportMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改消息分析报告
     */
    @Override
    public Boolean updateByBo(MessageAnalysisReportBo bo) {
        MessageAnalysisReport update = MapstructUtils.convert(bo, MessageAnalysisReport.class);
        validEntityBeforeSave(update);
        return messageAnalysisReportMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MessageAnalysisReport entity) {
        // TODO 做一些数据校验,如唯一性验证
    }

    /**
     * 校验并批量删除消息分析报告信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // TODO 做一些业务上的校验,判断是否需要校验
        }
        return messageAnalysisReportMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 根据消息ID查询报告
     */
    @Override
    public MessageAnalysisReportVo queryByMessageId(Long messageId) {
        LambdaQueryWrapper<MessageAnalysisReport> lqw = Wrappers.lambdaQuery();
        lqw.eq(MessageAnalysisReport::getMessageId, messageId);
        lqw.orderByDesc(MessageAnalysisReport::getCreateTime);
        lqw.last("LIMIT 1");
        return messageAnalysisReportMapper.selectVoOne(lqw);
    }

    /**
     * 查询待审核报告列表
     */
    @Override
    public TableDataInfo<MessageAnalysisReportVo> queryPendingList(PageQuery pageQuery) {
        LambdaQueryWrapper<MessageAnalysisReport> lqw = Wrappers.lambdaQuery();
        lqw.eq(MessageAnalysisReport::getAuditStatus, 0); // 0-待审核
        lqw.orderByDesc(MessageAnalysisReport::getCreateTime);
        Page<MessageAnalysisReportVo> page = messageAnalysisReportMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * AI接收分析报告
     */
    @Override
    public Boolean receiveAiReport(MessageAnalysisReport report) {
        // 设置默认状态
        report.setAuditStatus(0); // 待审核
        report.setExecuted(0); // 未执行
        report.setReportTime(LocalDateTime.now());
        
        return messageAnalysisReportMapper.insert(report) > 0;
    }

    /**
     * 审核报告
     */
    @Override
    public Boolean auditReport(Long id, Integer auditStatus, String auditor, String comments) {
        MessageAnalysisReport report = messageAnalysisReportMapper.selectById(id);
        if (report == null) {
            return false;
        }
        
        report.setAuditStatus(auditStatus);
        report.setAuditor(auditor);
        report.setAuditComments(comments);
        report.setAuditTime(LocalDateTime.now());
        
        return messageAnalysisReportMapper.updateById(report) > 0;
    }

    /**
     * 执行调整方案
     */
    @Override
    public Boolean executePlan(Long id) {
        MessageAnalysisReport report = messageAnalysisReportMapper.selectById(id);
        if (report == null || report.getAuditStatus() != 1) {
            return false;
        }
        
        report.setExecuted(1);
        report.setExecuteTime(LocalDateTime.now());
        
        // TODO: 这里需要实现实际的持仓调整逻辑
        // 根据调整方案更新持仓数据
        
        return messageAnalysisReportMapper.updateById(report) > 0;
    }

}