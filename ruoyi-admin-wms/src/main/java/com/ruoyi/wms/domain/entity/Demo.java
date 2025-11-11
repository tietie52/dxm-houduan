package com.ruoyi.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("demo")
public class Demo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    private Long id;

    private String remark;

    @TableField("del_flag")
    private Integer delFlag;


    @TableField("demo_name")
    private String demoName;


    @TableField("demo_age")
    private Integer demoAge;


    @TableField("demo_gender")
    private Integer demoGender;
}
