package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.mybatis.core.domain.BaseVo;
import com.ruoyi.wms.domain.entity.Coin;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 硬币视图对象 coin
 *
 * @author zcc
 * @date 2024-12-01
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Coin.class)
public class CoinVo extends BaseVo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 硬币名称
     */
    @ExcelProperty(value = "硬币名称")
    private String coinName;

    /**
     * 硬币代码
     */
    @ExcelProperty(value = "硬币代码")
    private String coinCode;

    /**
     * 价格
     */
    @ExcelProperty(value = "价格")
    private BigDecimal price;

    /**
     * 同步状态
     */
    @ExcelProperty(value = "同步状态")
    private Integer syncStatus;

    /**
     * 同步时间
     */
    @ExcelProperty(value = "同步时间")
    private LocalDateTime syncTime;

    /**
     * 同步来源
     */
    @ExcelProperty(value = "同步来源")
    private String syncSource;

    /**
     * 删除标志
     */
    @ExcelProperty(value = "删除标志")
    private Integer isDeleted;
}