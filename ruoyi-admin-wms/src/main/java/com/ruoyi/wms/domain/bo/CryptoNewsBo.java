package com.ruoyi.wms.domain.bo;

import com.ruoyi.wms.domain.entity.CryptoNews;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.time.LocalDateTime;

/**
 * 加密货币资讯业务对象
 *
 * @author zcc
 * @date 2025-12-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CryptoNews.class, reverseConvertGenerate = false)
public class CryptoNewsBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 资讯标题
     */
    @NotBlank(message = "资讯标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 资讯内容
     */
    @NotBlank(message = "资讯内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 数字货币类型（如BTC/ETH/SOL等）
     */
    @NotBlank(message = "数字货币类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cryptoType;

    /**
     * 情绪标签（如正面/负面/中性）
     */
    @NotBlank(message = "情绪标签不能为空", groups = { AddGroup.class, EditGroup.class })
    private String emotion;

    /**
     * 资讯发布时间
     */
    @NotNull(message = "资讯发布时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private LocalDateTime publishTime;

    /**
     * 资讯来源（如媒体/平台名称）
     */
    @NotBlank(message = "资讯来源不能为空", groups = { AddGroup.class, EditGroup.class })
    private String source;

    /**
     * 软删除标识 0-未删除 1-已删除
     */
    private Integer isDeleted;
}
