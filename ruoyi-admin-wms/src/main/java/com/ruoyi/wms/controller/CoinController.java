package com.ruoyi.wms.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.domain.bo.CoinBo;
import com.ruoyi.wms.domain.entity.Coin;
import com.ruoyi.wms.domain.vo.CoinVo;
import com.ruoyi.wms.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 硬币信息控制器
 *
 * @author zcc
 * @date 2024-12-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/coins")
public class CoinController extends BaseController {

    private final CoinService coinService;

    /**
     * 同步硬币数据
     */
    @Log(title = "同步硬币数据", businessType = BusinessType.OTHER)
    @GetMapping("/sync")
    public R<Integer> syncFromDify() {
        try {
            int syncCount = coinService.syncFromDify();
            return R.ok("同步成功", syncCount);
        } catch (Exception e) {
            return R.fail("同步失败: " + e.getMessage());
        }
    }

    /**
     * 批量新增硬币数据
     */
    @Log(title = "批量新增硬币数据", businessType = BusinessType.INSERT)
    @PostMapping("/postDifyApi")
    public R<Void> postDifyApi(@RequestBody List<Coin> coins) {
        try {
            boolean result = coinService.saveBatch(coins);
            return toAjax(result);
        } catch (Exception e) {
            return R.fail("批量新增失败: " + e.getMessage());
        }
    }

    /**
     * 查询硬币列表
     */
    @GetMapping("/list")
    public TableDataInfo<CoinVo> list(CoinBo bo, PageQuery pageQuery) {
        return coinService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取硬币详细信息
     */
    @GetMapping(value = "/{id}")
    public R<CoinVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(coinService.queryById(id));
    }

    /**
     * 新增硬币
     */
    @Log(title = "硬币信息", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody CoinBo bo) {
        return toAjax(coinService.insertByBo(bo));
    }

    /**
     * 修改硬币
     */
    @Log(title = "硬币信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody CoinBo bo) {
        return toAjax(coinService.updateByBo(bo));
    }

    /**
     * 删除硬币
     */
    @Log(title = "硬币信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(coinService.deleteWithValidByIds(List.of(ids), true));
    }
}