package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 加密货币资讯表实体对象
 *
 * @author zcc
 * @date 2025-12-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("crypto_news")
public class CryptoNews extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建者（重写父类字段，不映射到数据库）
     */
    @TableField(exist = false)
    private String createBy;

    /**
     * 更新者（重写父类字段，不映射到数据库）
     */
    @TableField(exist = false)
    private String updateBy;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 资讯标题
     */
    @TableField("title")
    private String title;

    /**
     * 资讯内容
     */
    @TableField("content")
    private String content;

    /**
     * 数字货币类型（如BTC/ETH/SOL等）
     */
    @TableField("crypto_type")
    private String cryptoType;

    /**
     * 情绪标签（如正面/负面/中性）
     */
    @TableField("emotion")
    private String emotion;

    /**
     * 资讯发布时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /**
     * 资讯来源（如媒体/平台名称）
     */
    @TableField("source")
    private String source;

    /**
     * 记录创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 软删除标识 0-未删除 1-已删除
     */
    @TableField("is_deleted")
    private Integer isDeleted;
}
