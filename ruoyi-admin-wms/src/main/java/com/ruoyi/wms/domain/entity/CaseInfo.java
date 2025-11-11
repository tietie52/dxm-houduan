package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 案例信息实体类
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_case_info")
public class CaseInfo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 案例编码
     */
    private String caseCode;

    /**
     * 案例名称
     */
    private String caseName;

    /**
     * 承担单位
     */
    private String undertakingUnit;

    /**
     * 案例类型
     */
    private String caseType;

    /**
     * 案例来源
     */
    private String caseSource;

    /**
     * 案例备注
     */
    private String caseRemark;

    /**
     * 单位状态
     */
    private Integer unitStatus;
}