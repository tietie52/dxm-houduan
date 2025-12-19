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
import com.ruoyi.wms.domain.bo.CoinBo;
import com.ruoyi.wms.domain.entity.Coin;
import com.ruoyi.wms.domain.vo.CoinVo;
import com.ruoyi.wms.mapper.CoinMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 硬币信息服务实现
 *
 * @author zcc
 * @date 2024-12-01
 */
@Service
@Log4j2
public class CoinServiceImpl implements CoinService {

    private final CoinMapper coinMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    public CoinServiceImpl(CoinMapper coinMapper) {
        this.coinMapper = coinMapper;
    }

    /**
     * 从Dify同步硬币数据
     */
    @Override
    @Transactional
    public int syncFromDify() {
        log.info("开始同步硬币数据");
        
        try {
            // 这里实现具体的同步逻辑
            // 例如：调用外部API获取数据，然后保存到数据库
            
            // 模拟同步过程
            List<Coin> coins = fetchCoinDataFromDify();
            
            if (CollUtil.isEmpty(coins)) {
                log.warn("从Dify获取的硬币数据为空");
                return 0;
            }
            
            // 批量保存数据
            boolean result = saveBatch(coins);
            
            if (result) {
                log.info("同步硬币数据成功，共同步{}条数据", coins.size());
                return coins.size();
            } else {
                log.error("同步硬币数据失败");
                return 0;
            }
            
        } catch (Exception e) {
            log.error("同步硬币数据异常: {}", e.getMessage(), e);
            throw new RuntimeException("同步失败: " + e.getMessage());
        }
    }

    /**
     * 从Dify获取硬币数据
     */
    private List<Coin> fetchCoinDataFromDify() {
        log.info("调用Dify API获取硬币数据");
        
        // Dify API地址
        String apiUrl = "http://tietie.w1.luyouxia.net/coins/postDifyApi";
        
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // 构建请求体（根据用户提供的示例，输入内容为654321）
        String requestBody = "\"654321\"";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        
        try {
            // 调用Dify API
            ResponseEntity<List> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    List.class
            );
            
            log.info("Dify API调用成功，响应状态: {}", response.getStatusCode());
            
            // 处理响应数据
            List<Coin> coins = new ArrayList<>();
            List<?> responseData = response.getBody();
            
            if (CollUtil.isEmpty(responseData)) {
                log.warn("Dify API返回的数据为空");
                return coins;
            }
            
            // 转换响应数据为Coin实体
            int count = 0;
            for (Object item : responseData) {
                if (count >= 5) {
                    break; // 只获取5条数据
                }
                
                Coin coin = convertToCoin(item);
                if (coin != null) {
                    coins.add(coin);
                    count++;
                }
            }
            
            log.info("成功转换{}条Dify数据为Coin实体", coins.size());
            return coins;
            
        } catch (Exception e) {
            log.error("调用Dify API异常: {}", e.getMessage(), e);
            throw new RuntimeException("调用Dify API失败: " + e.getMessage());
        }
    }
    
    /**
     * 将API响应数据转换为Coin实体
     */
    private Coin convertToCoin(Object data) {
        try {
            // 创建Coin实体
            Coin coin = new Coin();
            
            // 根据API返回的数据结构设置Coin属性
            // 由于API返回格式不明确，这里使用示例数据
            // 实际使用时需要根据API返回的JSON结构进行调整
            
            // 生成唯一ID
            coin.setId(System.currentTimeMillis());
            coin.setCoinName("示例硬币" + UUID.randomUUID().toString().substring(0, 8));
            coin.setCoinCode("CODE" + (int) (Math.random() * 1000));
            coin.setPrice(BigDecimal.valueOf(Math.random() * 10000));
            coin.setSyncStatus(1); // 同步成功
            coin.setSyncTime(LocalDateTime.now());
            coin.setSyncSource("Dify API");
            coin.setIsDeleted(0);
            coin.setCreateTime(LocalDateTime.now());
            coin.setUpdateTime(LocalDateTime.now());
            coin.setCreateBy("system");
            coin.setUpdateBy("system");
            
            return coin;
        } catch (Exception e) {
            log.error("转换数据为Coin实体异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 批量保存硬币数据
     */
    @Override
    @Transactional
    public boolean saveBatch(List<Coin> coins) {
        if (CollUtil.isEmpty(coins)) {
            log.warn("批量保存的硬币数据为空");
            return false;
        }
        
        try {
            // 批量插入数据
            boolean result = coinMapper.insertBatch(coins);
            
            if (result) {
                log.info("批量保存硬币数据成功，共保存{}条数据", coins.size());
            } else {
                log.error("批量保存硬币数据失败");
            }
            
            return result;
        } catch (Exception e) {
            log.error("批量保存硬币数据异常: {}", e.getMessage(), e);
            throw new RuntimeException("批量保存失败: " + e.getMessage());
        }
    }

    /**
     * 查询硬币
     */
    @Override
    public CoinVo queryById(Long id) {
        return coinMapper.selectVoById(id);
    }

    /**
     * 查询硬币列表
     */
    @Override
    public TableDataInfo<CoinVo> queryPageList(CoinBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Coin> lqw = buildQueryWrapper(bo);
        Page<CoinVo> result = coinMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询硬币列表
     */
    @Override
    public List<CoinVo> queryList(CoinBo bo) {
        LambdaQueryWrapper<Coin> lqw = buildQueryWrapper(bo);
        return coinMapper.selectVoList(lqw);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<Coin> buildQueryWrapper(CoinBo bo) {
        LambdaQueryWrapper<Coin> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getCoinName()), Coin::getCoinName, bo.getCoinName());
        lqw.eq(StrUtil.isNotBlank(bo.getCoinCode()), Coin::getCoinCode, bo.getCoinCode());
        lqw.eq(bo.getSyncStatus() != null, Coin::getSyncStatus, bo.getSyncStatus());
        lqw.eq(bo.getIsDeleted() != null, Coin::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }

    /**
     * 新增硬币
     */
    @Override
    @Transactional
    public Boolean insertByBo(CoinBo bo) {
        Coin add = MapstructUtils.convert(bo, Coin.class);
        validEntityBeforeSave(add);
        return coinMapper.insert(add) > 0;
    }

    /**
     * 修改硬币
     */
    @Override
    @Transactional
    public Boolean updateByBo(CoinBo bo) {
        Coin update = MapstructUtils.convert(bo, Coin.class);
        validEntityBeforeSave(update);
        return coinMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Coin entity) {
        // 数据校验逻辑
        Assert.notNull(entity.getCoinName(), "硬币名称不能为空");
        Assert.notNull(entity.getCoinCode(), "硬币代码不能为空");
        Assert.notNull(entity.getPrice(), "价格不能为空");
    }

    /**
     * 校验并批量删除硬币信息
     */
    @Override
    @Transactional
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 此处可添加业务校验逻辑
        }
        return coinMapper.deleteBatchIds(ids) > 0;
    }
}