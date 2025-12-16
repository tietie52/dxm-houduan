package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.CryptoNews;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 加密货币资讯视图对象
 *
 * @author zcc
 * @date 2025-12-16
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CryptoNews.class)
public class CryptoNewsVo extends BaseVo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 资讯标题
     */
    @ExcelProperty(value = "资讯标题")
    private String title;

    /**
     * 资讯内容
     */
    @ExcelProperty(value = "资讯内容")
    private String content;

    /**
     * 数字货币类型（如BTC/ETH/SOL等）
     */
    @ExcelProperty(value = "数字货币类型")
    private String cryptoType;

    /**
     * 情绪标签（如正面/负面/中性）
     */
    @ExcelProperty(value = "情绪标签")
    private String emotion;

    /**
     * 资讯发布时间
     */
    @ExcelProperty(value = "资讯发布时间")
    private LocalDateTime publishTime;

    /**
     * 资讯来源（如媒体/平台名称）
     */
    @ExcelProperty(value = "资讯来源")
    private String source;

    /**
     * 记录创建时间
     */
    @ExcelProperty(value = "记录创建时间")
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    @ExcelProperty(value = "记录更新时间")
    private LocalDateTime updateTime;

    /**
     * 软删除标识 0-未删除 1-已删除
     */
    @ExcelProperty(value = "软删除标识")
    private Integer isDeleted;
}
