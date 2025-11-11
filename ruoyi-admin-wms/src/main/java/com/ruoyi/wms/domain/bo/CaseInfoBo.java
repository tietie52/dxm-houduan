package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import com.ruoyi.wms.domain.entity.CaseInfo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CaseInfo.class, reverseConvertGenerate = false)
public class CaseInfoBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "ID不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 案例编码
     */
    private String caseCode;

    /**
     * 案例名称
     */
    @NotBlank(message = "案例名称不能为空", groups = {AddGroup.class, EditGroup.class})
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
