package com.ruoyi.wms.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.DemoBo;
import com.ruoyi.wms.domain.vo.DemoVo;
import com.ruoyi.wms.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;

/**
 * 演示数据控制器
 * 注：使用@SaIgnore绕过权限验证，适合教学环境使用
 */
@RestController
@RequestMapping("/wms/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    /**
     * 根据ID查询单个演示数据
     */
    @SaIgnore
    @GetMapping("/{id}")
    public R<DemoVo> getDemoById(@PathVariable("id") Long id) {
        return R.ok(demoService.queryById(id));
    }

    /**
     * 查询演示数据列表（支持模糊查询和分页）
     */
//    @SaIgnore
    @GetMapping("/list")
    public R<TableDataInfo<DemoVo>> listDemo(DemoBo bo, PageQuery pageQuery) {
        return R.ok(demoService.queryPageList(bo, pageQuery));
    }

    /**
     * 新增演示数据
     */
//    @SaIgnore
    @Log(title = "演示添加", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Void> addDemo(@RequestBody DemoBo bo) {
        demoService.insertByBo(bo);
        return R.ok();
    }

    /**
     * 修改演示数据
     */
//    @SaIgnore
    @Log(title = "演示数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> updateDemo(@RequestBody DemoBo bo) {
        demoService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 根据ID删除演示数据
     */
    @SaIgnore
    @Log(title = "演示数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> deleteDemo(@PathVariable("id") Long id) {
        demoService.deleteById(id);
        return R.ok();
    }

    /**
     * 批量删除演示数据
     */
    @SaIgnore
    @Log(title = "演示数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch")
    public R<Void> deleteDemoBatch(@RequestBody Collection<Long> ids) {
        demoService.deleteByIds(ids);
        return R.ok();
    }
}

