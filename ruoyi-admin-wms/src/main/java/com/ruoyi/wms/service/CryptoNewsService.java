package com.ruoyi.wms.service;

import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.CryptoNewsBo;
import com.ruoyi.wms.domain.vo.CryptoNewsVo;

import java.util.Collection;
import java.util.List;

/**
 * 加密货币资讯Service接口
 *
 * @author zcc
 * @date 2025-12-16
 */
public interface CryptoNewsService {

    /**
     * 查询加密货币资讯详情
     */
    CryptoNewsVo queryById(Long id);

    /**
     * 查询加密货币资讯列表（分页）
     */
    TableDataInfo<CryptoNewsVo> queryPageList(CryptoNewsBo bo, PageQuery pageQuery);

    /**
     * 查询加密货币资讯列表（不分页）
     */
    List<CryptoNewsVo> queryList(CryptoNewsBo bo);

    /**
     * 新增加密货币资讯
     */
    Boolean insertByBo(CryptoNewsBo bo);

    /**
     * 修改加密货币资讯
     */
    Boolean updateByBo(CryptoNewsBo bo);

    /**
     * 校验并批量删除加密货币资讯
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
