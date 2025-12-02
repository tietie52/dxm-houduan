package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 硬币对象 coin
 *
 * @author zcc
 * @date 2024-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("coin")
public class Coin extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 硬币名称
     */
    @TableField("coin_name")
    private String coinName;

    /**
     * 硬币代码
     */
    @TableField("coin_code")
    private String coinCode;

    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 同步状态
     */
    @TableField("sync_status")
    private Integer syncStatus;

    /**
     * 同步时间
     */
    @TableField("sync_time")
    private LocalDateTime syncTime;

    /**
     * 同步来源
     */
    @TableField("sync_source")
    private String syncSource;

    /**
     * 删除标志
     */
    @TableField("is_deleted")
    private Integer isDeleted;
}