package com.ruoyi.wms.service;

import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.CoinBo;
import com.ruoyi.wms.domain.entity.Coin;
import com.ruoyi.wms.domain.vo.CoinVo;

import java.util.Collection;
import java.util.List;

/**
 * 硬币信息Service接口
 *
 * @author zcc
 * @date 2024-12-01
 */
public interface CoinService {

    /**
     * 从Dify同步硬币数据
     *
     * @return 同步成功的数量
     */
    int syncFromDify();

    /**
     * 批量保存硬币数据
     *
     * @param coins 硬币列表
     * @return 是否成功
     */
    boolean saveBatch(List<Coin> coins);

    /**
     * 查询硬币
     */
    CoinVo queryById(Long id);

    /**
     * 查询硬币列表
     */
    TableDataInfo<CoinVo> queryPageList(CoinBo bo, PageQuery pageQuery);

    /**
     * 查询硬币列表
     */
    List<CoinVo> queryList(CoinBo bo);

    /**
     * 新增硬币
     */
    Boolean insertByBo(CoinBo bo);

    /**
     * 修改硬币
     */
    Boolean updateByBo(CoinBo bo);

    /**
     * 校验并批量删除硬币信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}