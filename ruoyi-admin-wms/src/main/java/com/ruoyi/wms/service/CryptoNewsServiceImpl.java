package com.ruoyi.wms.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.CryptoNewsBo;
import com.ruoyi.wms.domain.entity.CryptoNews;
import com.ruoyi.wms.domain.vo.CryptoNewsVo;
import com.ruoyi.wms.mapper.CryptoNewsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 加密货币资讯服务实现类
 *
 * @author zcc
 * @date 2025-12-16
 */
@RequiredArgsConstructor
@Service
@Log4j2
public class CryptoNewsServiceImpl implements CryptoNewsService {

    private final CryptoNewsMapper cryptoNewsMapper;

    /**
     * 查询加密货币资讯详情
     */
    @Override
    public CryptoNewsVo queryById(Long id) {
        return cryptoNewsMapper.selectVoById(id);
    }

    /**
     * 查询加密货币资讯列表（分页）
     */
    @Override
    public TableDataInfo<CryptoNewsVo> queryPageList(CryptoNewsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CryptoNews> lqw = buildQueryWrapper(bo);
        Page<CryptoNewsVo> result = cryptoNewsMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询加密货币资讯列表（不分页）
     */
    @Override
    public List<CryptoNewsVo> queryList(CryptoNewsBo bo) {
        LambdaQueryWrapper<CryptoNews> lqw = buildQueryWrapper(bo);
        return cryptoNewsMapper.selectVoList(lqw);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<CryptoNews> buildQueryWrapper(CryptoNewsBo bo) {
        LambdaQueryWrapper<CryptoNews> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getTitle()), CryptoNews::getTitle, bo.getTitle());
        lqw.eq(StrUtil.isNotBlank(bo.getCryptoType()), CryptoNews::getCryptoType, bo.getCryptoType());
        lqw.eq(StrUtil.isNotBlank(bo.getEmotion()), CryptoNews::getEmotion, bo.getEmotion());
        lqw.eq(StrUtil.isNotBlank(bo.getSource()), CryptoNews::getSource, bo.getSource());
        lqw.ge(bo.getPublishTime() != null, CryptoNews::getPublishTime, bo.getPublishTime());
        lqw.eq(bo.getIsDeleted() != null, CryptoNews::getIsDeleted, bo.getIsDeleted());
        lqw.orderByDesc(CryptoNews::getPublishTime);
        return lqw;
    }

    /**
     * 新增加密货币资讯
     */
    @Override
    @Transactional
    public Boolean insertByBo(CryptoNewsBo bo) {
        // 手动转换Bo到Entity
        CryptoNews entity = new CryptoNews();
        entity.setTitle(bo.getTitle());
        entity.setContent(bo.getContent());
        entity.setCryptoType(bo.getCryptoType());
        entity.setEmotion(bo.getEmotion());
        entity.setPublishTime(bo.getPublishTime());
        entity.setSource(bo.getSource());
        entity.setIsDeleted(bo.getIsDeleted());
        entity.setCreateTime(bo.getCreateTime());
        entity.setUpdateTime(bo.getUpdateTime());
        
        validEntityBeforeSave(entity);
        return cryptoNewsMapper.insert(entity) > 0;
    }

    /**
     * 修改加密货币资讯
     */
    @Override
    @Transactional
    public Boolean updateByBo(CryptoNewsBo bo) {
        // 手动转换Bo到Entity
        CryptoNews entity = new CryptoNews();
        entity.setId(bo.getId());
        entity.setTitle(bo.getTitle());
        entity.setContent(bo.getContent());
        entity.setCryptoType(bo.getCryptoType());
        entity.setEmotion(bo.getEmotion());
        entity.setPublishTime(bo.getPublishTime());
        entity.setSource(bo.getSource());
        entity.setIsDeleted(bo.getIsDeleted());
        entity.setCreateTime(bo.getCreateTime());
        entity.setUpdateTime(bo.getUpdateTime());
        
        validEntityBeforeSave(entity);
        return cryptoNewsMapper.updateById(entity) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(CryptoNews entity) {
        // 数据校验逻辑
        Assert.notNull(entity.getTitle(), "资讯标题不能为空");
        Assert.notNull(entity.getContent(), "资讯内容不能为空");
        Assert.notNull(entity.getCryptoType(), "数字货币类型不能为空");
        Assert.notNull(entity.getEmotion(), "情绪标签不能为空");
        Assert.notNull(entity.getPublishTime(), "资讯发布时间不能为空");
        Assert.notNull(entity.getSource(), "资讯来源不能为空");
    }

    /**
     * 校验并批量删除加密货币资讯
     */
    @Override
    @Transactional
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 此处可添加业务校验逻辑
        }
        return cryptoNewsMapper.deleteBatchIds(ids) > 0;
    }
}
