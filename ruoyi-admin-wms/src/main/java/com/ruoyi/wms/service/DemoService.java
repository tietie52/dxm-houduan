package com.ruoyi.wms.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.DemoBo;
import com.ruoyi.wms.domain.entity.Demo;
import com.ruoyi.wms.domain.vo.DemoVo;
import com.ruoyi.wms.mapper.DemoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DemoService {

    private final DemoMapper demoMapper;

    /**
     * 根据主键查询演示数据
     */
    public DemoVo queryById(Long id) {
        return demoMapper.selectVoById(id);
    }

    /**
     * 分页查询演示数据列表（支持模糊查询）
     */
    public TableDataInfo<DemoVo> queryPageList(DemoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Demo> lqw = buildQueryWrapper(bo);
        Page<DemoVo> result = demoMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询演示数据列表（支持模糊查询）
     */
    public List<DemoVo> queryList(DemoBo bo) {
        LambdaQueryWrapper<Demo> lqw = buildQueryWrapper(bo);
        return demoMapper.selectVoList(lqw);
    }

    /**
     * 构建查询条件（支持模糊查询）
     */
    private LambdaQueryWrapper<Demo> buildQueryWrapper(DemoBo bo) {
        LambdaQueryWrapper<Demo> lqw = Wrappers.lambdaQuery();
        // 支持根据id进行精准查询
        lqw.eq(bo.getId() != null, Demo::getId, bo.getId());
        // 支持根据名称进行模糊查询
        lqw.like(StrUtil.isNotBlank(bo.getDemoName()), Demo::getDemoName, bo.getDemoName());
        // 支持根据年龄进行精准查询
        lqw.eq(bo.getDemoAge() != null, Demo::getDemoAge, bo.getDemoAge());
        return lqw;
    }

    /**
     * 新增演示数据
     */
    public void insertByBo(DemoBo bo) {
        Demo add = MapstructUtils.convert(bo, Demo.class);
        demoMapper.insert(add);
    }

    /**
     * 修改演示数据
     */
    public void updateByBo(DemoBo bo) {
        Demo update = MapstructUtils.convert(bo, Demo.class);
        demoMapper.updateById(update);
    }

    /**
     * 根据主键删除演示数据
     */
    public void deleteById(Long id) {
        demoMapper.deleteById(id);
    }

    /**
     * 批量删除演示数据
     */
    public void deleteByIds(Collection<Long> ids) {
        demoMapper.deleteBatchIds(ids);
    }
}
