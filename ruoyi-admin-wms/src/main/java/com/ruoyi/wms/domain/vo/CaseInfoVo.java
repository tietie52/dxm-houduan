package com.ruoyi.wms.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.wms.domain.entity.CaseInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 案例信息Vo
 * 
 * @author ruoyi
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CaseInfo.class)
public class CaseInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 案例编码
     */
    @ExcelProperty(value = "案例编码")
    private String caseCode;

    /**
     * 案例名称
     */
    @ExcelProperty(value = "案例名称")
    private String caseName;

    /**
     * 承担单位
     */
    @ExcelProperty(value = "承担单位")
    private String undertakingUnit;

    /**
     * 案例类型
     */
    @ExcelProperty(value = "案例类型")
    private String caseType;

    /**
     * 案例来源
     */
    @ExcelProperty(value = "案例来源")
    private String caseSource;

    /**
     * 案例备注
     */
    @ExcelProperty(value = "案例备注")
    private String caseRemark;

    /**
     * 单位状态
     */
    @ExcelProperty(value = "单位状态")
    private Integer unitStatus;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @ExcelProperty(value = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}