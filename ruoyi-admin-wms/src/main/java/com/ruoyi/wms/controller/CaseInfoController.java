package com.ruoyi.wms.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.idempotent.annotation.RepeatSubmit;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.CaseInfoBo;
import com.ruoyi.wms.domain.vo.CaseInfoVo;
import com.ruoyi.wms.service.CaseInfoService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/caseInfo")
public class CaseInfoController extends BaseController {

    private final CaseInfoService caseInfoService;

    /**
     * 查询案例列表（分页）
     */
    @GetMapping("/list")
    public TableDataInfo<CaseInfoVo> list(CaseInfoBo bo, PageQuery pageQuery) {
        return caseInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询案例列表（不分页）
     */
    @GetMapping("/listNoPage")
    public R<List<CaseInfoVo>> listNoPage(CaseInfoBo bo) {
        return R.ok(caseInfoService.queryList(bo));
    }

    /**
     * 根据ID获取案例详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<CaseInfoVo> getInfo(@NotNull(message = "主键不能为空")
                               @PathVariable Long id) {
        return R.ok(caseInfoService.queryById(id));
    }

    /**
     * 新增案例
     */
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CaseInfoBo bo) {
        caseInfoService.insertByBo(bo);
        return R.ok();
    }

    /**
     * 修改案例
     */
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CaseInfoBo bo) {
        caseInfoService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 根据ID删除案例
     *
     * @param id 主键
     */
    @DeleteMapping("/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空")
                        @PathVariable Long id) {
        caseInfoService.deleteById(id);
        return R.ok();
    }
}