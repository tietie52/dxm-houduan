package com.ruoyi.wms.domain.bo;

import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.ruoyi.wms.domain.entity.Demo;
import lombok.EqualsAndHashCode;

/**
 * 演示数据查询和操作对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Demo.class, reverseConvertGenerate = false)
public class DemoBo extends BaseEntity {
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;
    private String demoName;
    private Integer demoAge;
    private Integer demoGender;
    private String remark;
}
