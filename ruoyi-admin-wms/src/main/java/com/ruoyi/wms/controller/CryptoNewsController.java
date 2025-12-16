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
@RequestMapping("/crypto-news")
public class CryptoNewsController extends BaseController {

    private final CryptoNewsService cryptoNewsService;

    /**
     * 查询加密货币资讯列表
     */
    @GetMapping("/list")
    public TableDataInfo<CryptoNewsVo> list(CryptoNewsBo bo, PageQuery pageQuery) {
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
}
