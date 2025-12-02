package com.ruoyi.wms.domain.bo;

import com.ruoyi.wms.domain.entity.Coin;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 硬币业务对象 coin
 *
 * @author zcc
 * @date 2024-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Coin.class, reverseConvertGenerate = false)
public class CoinBo extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 硬币名称
     */
    @NotBlank(message = "硬币名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String coinName;

    /**
     * 硬币代码
     */
    @NotBlank(message = "硬币代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String coinCode;

    /**
     * 价格
     */
    @NotNull(message = "价格不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal price;

    /**
     * 同步状态
     */
    private Integer syncStatus;

    /**
     * 同步时间
     */
    private LocalDateTime syncTime;

    /**
     * 同步来源
     */
    private String syncSource;

    /**
     * 删除标志
     */
    private Integer isDeleted;
}