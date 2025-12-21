package com.ruoyi.wms.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.CryptoNewsBo;
import com.ruoyi.wms.domain.vo.CryptoNewsVo;
import com.ruoyi.wms.service.CryptoNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * 加密货币资讯控制器
 *
 * @author zcc
 * @date 2025-12-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/api/crypto-news", "/crypto-news"})
@CrossOrigin(origins = "*", maxAge = 3600) // 解决前端跨域问题
public class CryptoNewsController extends BaseController {

    private final CryptoNewsService cryptoNewsService;

    /**
     * 查询加密货币资讯列表
     */
    @GetMapping("/list")
    // 修正：使用无参构造+setter方法初始化PageQuery，适配若依框架
    public TableDataInfo<CryptoNewsVo> list(
            CryptoNewsBo bo,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        // 先创建无参PageQuery对象，再通过setter赋值（若依框架标准用法）
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        
        return cryptoNewsService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取加密货币资讯详细信息
     */
    @GetMapping("/{id}")
    public R<CryptoNewsVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(cryptoNewsService.queryById(id));
    }

    /**
     * 新增加密货币资讯
     */
    @Log(title = "加密货币资讯", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody CryptoNewsBo bo) {
        return toAjax(cryptoNewsService.insertByBo(bo));
    }

    /**
     * 修改加密货币资讯
     */
    @Log(title = "加密货币资讯", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody CryptoNewsBo bo) {
        return toAjax(cryptoNewsService.updateByBo(bo));
    }

    /**
     * 删除加密货币资讯
     */
    @Log(title = "加密货币资讯", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Collection<Long> ids) {
        return toAjax(cryptoNewsService.deleteWithValidByIds(ids, true));
    }

    /**
     * 从Dify同步加密货币资讯数据
     */
    @Log(title = "加密货币资讯", businessType = BusinessType.OTHER)
    @PostMapping("/syncFromDify")
    public R<Integer> syncFromDify() {
        try {
            Integer syncCount = cryptoNewsService.syncFromDify();
            return R.ok("同步成功，共获取" + syncCount + "条资讯", syncCount);
        } catch (Exception e) {
            return R.fail("同步失败: " + e.getMessage());
        }
    }
}