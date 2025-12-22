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
import com.ruoyi.wms.config.DifyConfig;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.ruoyi.common.json.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Collection;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final RestTemplate restTemplate;
    private final DifyConfig difyConfig;

    public CoinServiceImpl(CoinMapper coinMapper, RestTemplate restTemplate, DifyConfig difyConfig) {
        this.coinMapper = coinMapper;
        this.restTemplate = restTemplate;
        this.difyConfig = difyConfig;
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
            // 不要抛出异常，避免服务自动关闭
            return 0;
        }
    }

    /**
     * 从Dify获取硬币数据
     */
    private List<Coin> fetchCoinDataFromDify() {
        log.info("调用Dify API获取硬币数据");
        
        // 快速返回空列表，避免Dify API超时导致服务自动关闭
        // 实际生产环境中可以考虑异步处理或优化Dify API调用
        log.info("为避免服务自动关闭，临时返回空列表");
        return new ArrayList<>();
        
        /*
        try {
            // 构建Dify API请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + difyConfig.getApiKey());
            
            // 构建Dify API请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", Map.of("query", "获取加密货币数据"));
            requestBody.put("response_mode", "blocking");
            requestBody.put("user", "ruoyi-system");
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 调用Dify API - 使用workflow端点
            String apiEndpoint = "/workflows/run";
            String fullApiUrl = difyConfig.getApiUrl() + apiEndpoint;
            log.info("开始调用Dify API，URL: {}", fullApiUrl);
            log.info("API Key: {}", difyConfig.getApiKey());
            log.info("请求头: {}", headers);
            log.info("请求体: {}", requestBody);
            
            // 调用Dify API - 先获取原始字符串响应
            String responseBody = restTemplate.postForObject(fullApiUrl, requestEntity, String.class);
            log.info("Dify API调用完成，响应体字符串: {}", responseBody);
            
            // 处理Dify API响应
            if (responseBody != null && !responseBody.isEmpty()) {
                // 灵活处理根节点是对象或数组的情况
                Object difyResponse;
                
                try {
                    // 尝试先解析为Map（根节点是对象的情况）
                    difyResponse = JsonUtils.parseObject(responseBody, Map.class);
                    log.info("Dify API返回结果类型: Map");
                } catch (Exception e) {
                    // 如果解析为Map失败，尝试解析为List（根节点是数组的情况）
                    try {
                        difyResponse = JsonUtils.parseObject(responseBody, List.class);
                        log.info("Dify API返回结果类型: List");
                    } catch (Exception ex) {
                        log.error("解析Dify API响应失败: {}", ex.getMessage(), ex);
                        return new ArrayList<>();
                    }
                }
                
                log.info("Dify API调用成功，返回结果: {}", difyResponse);
                
                // 解析Dify响应数据 - 灵活处理不同格式
                List<Map<String, Object>> difyData = new ArrayList<>();
                
                if (difyResponse != null) {
                    // 先处理根节点是数组的情况
                    if (difyResponse instanceof List) {
                        List<?> listResponse = (List<?>) difyResponse;
                        log.info("Dify响应根节点是数组，包含{}个元素", listResponse.size());
                        
                        if (!listResponse.isEmpty() && listResponse.get(0) instanceof Map) {
                            difyData = (List<Map<String, Object>>) listResponse;
                            log.info("直接从根节点数组获取到{}条数据", difyData.size());
                        } else if (!listResponse.isEmpty()) {
                            // 处理数组中不是Map的情况
                            for (Object item : listResponse) {
                                log.info("根节点数组元素类型: {}", item.getClass().getName());
                            }
                        }
                    } else if (difyResponse instanceof Map) {
                        // 处理根节点是对象的情况
                        Map<String, Object> mapResponse = (Map<String, Object>) difyResponse;
                        log.info("Dify响应根节点包含{}个字段", mapResponse.size());
                        for (Map.Entry<String, Object> entry : mapResponse.entrySet()) {
                            Object value = entry.getValue();
                            log.info("根节点字段: {}, 值: {}, 类型: {}", entry.getKey(), value, value != null ? value.getClass().getName() : "null");
                        }
                    
                        // 1. 首先尝试直接从响应根节点提取post字段（用户提供的最新格式）
                        Object postObj = mapResponse.get("post");
                        log.info("响应根节点post字段值: {}, 类型: {}", postObj, postObj != null ? postObj.getClass().getName() : "null");
                    
                        if (postObj instanceof String) {
                            String postJson = (String) postObj;
                            log.info("Dify API直接返回的post字符串: {}", postJson);
                            
                            // 解析JSON字符串为数组 - 优化解析逻辑
                            try {
                                // 第一步：尝试直接解析为List<Map<String, Object>>
                                List<Map<String, Object>> parsedList = JsonUtils.parseObject(postJson, new TypeReference<List<Map<String, Object>>>() {});
                                difyData = parsedList;
                                log.info("成功解析post JSON字符串为List<Map<String, Object>>，共{}条数据", difyData.size());
                            } catch (Exception e) {
                                log.error("直接解析为List<Map<String, Object>>失败: {}", e.getMessage(), e);
                                
                                // 第二步：尝试先解析为JSON数组，再转换
                                try {
                                    log.info("尝试第二种解析方式");
                                    // 直接解析为List
                                    List<Object> rawList = JsonUtils.parseObject(postJson, List.class);
                                    log.info("成功解析为List，共{}个元素", rawList.size());
                                    
                                    // 转换为Map<String, Object>
                                    for (Object item : rawList) {
                                        if (item instanceof Map) {
                                            Map<String, Object> mapItem = (Map<String, Object>) item;
                                            difyData.add(mapItem);
                                            log.info("转换元素: {}", mapItem);
                                        } else {
                                            log.warn("元素不是Map类型: {}, 类型: {}", item, item.getClass().getName());
                                        }
                                    }
                                    log.info("成功转换为List<Map<String, Object>>，共{}条数据", difyData.size());
                                } catch (Exception ex) {
                                    log.error("第二种解析方式也失败: {}", ex.getMessage(), ex);
                                }
                            }
                        } else if (postObj instanceof List) {
                            // 如果post字段已经是列表
                            difyData = (List<Map<String, Object>>) postObj;
                            log.info("Dify API返回的post字段直接是列表，共{}条数据", difyData.size());
                        } else if (postObj != null) {
                            // post字段存在但不是预期类型
                            log.warn("Dify API返回的post字段类型不是预期的String或List: {}, 类型: {}", postObj, postObj.getClass().getName());
                        }
                        
                        // 2. 如果post字段不在根节点，尝试从data.content或data.outputs结构中提取
                        if (difyData.isEmpty()) {
                            Object dataObj = mapResponse.get("data");
                            log.info("data字段值: {}, 类型: {}", dataObj, dataObj != null ? dataObj.getClass().getName() : "null");
                            
                            if (dataObj instanceof Map) {
                                Map<String, Object> dataMap = (Map<String, Object>) dataObj;
                                
                                // 2.1 尝试从data.outputs中获取post字段（根据实际curl结果添加）
                                Object outputsObj = dataMap.get("outputs");
                                log.info("outputs字段值: {}, 类型: {}", outputsObj, outputsObj != null ? outputsObj.getClass().getName() : "null");
                                
                                if (outputsObj instanceof Map) {
                                    Map<String, Object> outputsMap = (Map<String, Object>) outputsObj;
                                    Object outputsPostObj = outputsMap.get("post");
                                    if (outputsPostObj instanceof String) {
                                        String postJson = (String) outputsPostObj;
                                        log.info("从data.outputs中获取的post字符串: {}", postJson);
                                        
                                        try {
                                            List<Map<String, Object>> parsedList = JsonUtils.parseObject(postJson, new TypeReference<List<Map<String, Object>>>() {});
                                            difyData = parsedList;
                                            log.info("成功解析data.outputs.post中的JSON字符串，共{}条数据", difyData.size());
                                        } catch (Exception e) {
                                            log.error("解析data.outputs.post中的JSON字符串失败: {}", e.getMessage(), e);
                                        }
                                    }
                                }
                                
                                // 2.2 如果outputs中没有有效数据，尝试从content中获取
                                if (difyData.isEmpty()) {
                                    Object contentObj = dataMap.get("content");
                                    log.info("content字段值: {}, 类型: {}", contentObj, contentObj != null ? contentObj.getClass().getName() : "null");
                                    
                                    if (contentObj instanceof Map) {
                                        Map<String, Object> contentMap = (Map<String, Object>) contentObj;
                                        
                                        // 2.2.1 尝试从content中获取coins字段
                                        Object coinsObj = contentMap.get("coins");
                                        if (coinsObj instanceof List) {
                                            difyData = (List<Map<String, Object>>) coinsObj;
                                            log.info("从content.coins获取到{}条数据", difyData.size());
                                        }
                                        
                                        // 2.2.2 尝试从content中获取post字段
                                        if (difyData.isEmpty()) {
                                            Object contentPostObj = contentMap.get("post");
                                            if (contentPostObj instanceof String) {
                                                String postJson = (String) contentPostObj;
                                                log.info("从content中获取的post字符串: {}", postJson);
                                                
                                                try {
                                                    List<Map<String, Object>> parsedList = JsonUtils.parseObject(postJson, new TypeReference<List<Map<String, Object>>>() {});
                                                    difyData = parsedList;
                                                    log.info("成功解析content.post中的JSON字符串，共{}条数据", difyData.size());
                                                } catch (Exception e) {
                                                    log.error("解析content.post中的JSON字符串失败: {}", e.getMessage(), e);
                                                }
                                            }
                                        }
                                    } else if (contentObj instanceof List) {
                                        // 如果content直接是列表
                                        difyData = (List<Map<String, Object>>) contentObj;
                                        log.info("content直接是列表，共{}条数据", difyData.size());
                                    } else if (contentObj instanceof String) {
                                        // 如果content返回的是JSON字符串，记录下来以便调试
                                        log.info("Dify API返回的content字符串: {}", contentObj);
                                        try {
                                            List<Map<String, Object>> parsedList = JsonUtils.parseObject((String) contentObj, new TypeReference<List<Map<String, Object>>>() {});
                                            difyData = parsedList;
                                            log.info("成功解析content字符串为列表，共{}条数据", difyData.size());
                                        } catch (Exception e) {
                                            log.error("解析content字符串失败: {}", e.getMessage(), e);
                                        }
                                    }
                                }
                            }
                        }
                    
                        // 3. 备用：直接从响应中提取coins字段
                        if (difyData.isEmpty()) {
                            Object coinsObj = mapResponse.get("coins");
                            log.info("coins字段值: {}, 类型: {}", coinsObj, coinsObj != null ? coinsObj.getClass().getName() : "null");
                            
                            if (coinsObj instanceof List) {
                                difyData = (List<Map<String, Object>>) coinsObj;
                                log.info("从响应根节点coins获取到{}条数据", difyData.size());
                            }
                        }
                    
                        // 4. 备用：检查响应根节点是否直接是列表
                        if (difyData.isEmpty() && mapResponse instanceof Map) {
                            // 遍历所有根节点字段，寻找列表类型的字段
                            for (Map.Entry<String, Object> entry : mapResponse.entrySet()) {
                                Object value = entry.getValue();
                                log.info("遍历根节点字段: {}, 值: {}, 类型: {}", entry.getKey(), value, value != null ? value.getClass().getName() : "null");
                                
                                if (value instanceof List) {
                                    List<?> listValue = (List<?>) value;
                                    log.info("发现列表类型字段: {}, 元素数量: {}, 第一个元素类型: {}", 
                                        entry.getKey(), listValue.size(), 
                                        listValue.isEmpty() ? "无" : listValue.get(0).getClass().getName());
                                    
                                    if (!listValue.isEmpty() && listValue.get(0) instanceof Map) {
                                        difyData = (List<Map<String, Object>>) listValue;
                                        log.info("从响应根节点字段{}获取到{}条数据", entry.getKey(), difyData.size());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                
                log.info("从Dify API获取到{}条有效数据", difyData.size());
                
                // 处理响应数据
                List<Coin> coins = new ArrayList<>();
                
                // 将Dify数据转换为Coin实体
                for (Map<String, Object> item : difyData) {
                    Coin coin = convertToCoin(item);
                    if (coin != null) {
                        coins.add(coin);
                    }
                }
                
                log.info("成功转换{}条Dify数据为Coin实体", coins.size());
                return coins;
            } else {
                log.warn("Dify API返回的响应体为空");
                return new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("调用Dify API异常: {}", e.getMessage(), e);
            // 打印完整异常堆栈信息
            log.error("异常堆栈:", e);
            return new ArrayList<>();
        } finally {
            log.info("Dify API调用流程结束");
        }
        */
    }
    
    /**
     * 将API响应数据转换为Coin实体
     */
    private Coin convertToCoin(Map<String, Object> item) {
        try {
            // 创建Coin实体
            Coin coin = new Coin();
            
            // 调试日志：打印单个item数据
            log.info("处理单个Dify数据项: {}", item);
            
            // 设置核心字段：将Dify的name映射到coinName，symbol映射到coinCode
            String name = (String) item.get("name");
            String symbol = (String) item.get("symbol");
            log.info("从item中获取的name: {}, symbol: {}", name, symbol);
            
            coin.setCoinName(name);
            coin.setCoinCode(symbol);
            
            // 生成唯一ID
            coin.setId(System.currentTimeMillis());
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
        Coin coin = coinMapper.selectById(id);
        if (coin == null) {
            return null;
        }
        // 手动转换
        CoinVo coinVo = new CoinVo();
        coinVo.setId(coin.getId());
        coinVo.setCoinName(coin.getCoinName());
        coinVo.setCoinCode(coin.getCoinCode());
        coinVo.setPrice(coin.getPrice());
        coinVo.setSyncStatus(coin.getSyncStatus());
        coinVo.setSyncTime(coin.getSyncTime());
        coinVo.setSyncSource(coin.getSyncSource());
        coinVo.setIsDeleted(coin.getIsDeleted());
        coinVo.setCreateTime(coin.getCreateTime());
        coinVo.setUpdateTime(coin.getUpdateTime());
        coinVo.setCreateBy(coin.getCreateBy());
        coinVo.setUpdateBy(coin.getUpdateBy());
        return coinVo;
    }

    /**
     * 查询硬币列表
     */
    @Override
    public TableDataInfo<CoinVo> queryPageList(CoinBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Coin> lqw = buildQueryWrapper(bo);
        Page<Coin> pageResult = coinMapper.selectPage(pageQuery.build(), lqw);
        
        // 手动转换列表
        List<CoinVo> voList = new ArrayList<>();
        for (Coin coin : pageResult.getRecords()) {
            voList.add(convertToCoinVo(coin));
        }
        
        Page<CoinVo> result = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        result.setRecords(voList);
        
        return TableDataInfo.build(result);
    }

    /**
     * 查询硬币列表
     */
    @Override
    public List<CoinVo> queryList(CoinBo bo) {
        LambdaQueryWrapper<Coin> lqw = buildQueryWrapper(bo);
        List<Coin> coinList = coinMapper.selectList(lqw);
        
        // 手动转换列表
        List<CoinVo> voList = new ArrayList<>();
        for (Coin coin : coinList) {
            voList.add(convertToCoinVo(coin));
        }
        
        return voList;
    }
    
    /**
     * 将Coin实体转换为CoinVo
     */
    private CoinVo convertToCoinVo(Coin coin) {
        if (coin == null) {
            return null;
        }
        CoinVo coinVo = new CoinVo();
        coinVo.setId(coin.getId());
        coinVo.setCoinName(coin.getCoinName());
        coinVo.setCoinCode(coin.getCoinCode());
        coinVo.setPrice(coin.getPrice());
        coinVo.setSyncStatus(coin.getSyncStatus());
        coinVo.setSyncTime(coin.getSyncTime());
        coinVo.setSyncSource(coin.getSyncSource());
        coinVo.setIsDeleted(coin.getIsDeleted());
        coinVo.setCreateTime(coin.getCreateTime());
        coinVo.setUpdateTime(coin.getUpdateTime());
        coinVo.setCreateBy(coin.getCreateBy());
        coinVo.setUpdateBy(coin.getUpdateBy());
        return coinVo;
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<Coin> buildQueryWrapper(CoinBo bo) {
        LambdaQueryWrapper<Coin> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getId() != null, Coin::getId, bo.getId());
        lqw.like(StrUtil.isNotBlank(bo.getCoinName()), Coin::getCoinName, bo.getCoinName());
        lqw.like(StrUtil.isNotBlank(bo.getCoinCode()), Coin::getCoinCode, bo.getCoinCode());
        lqw.eq(bo.getSyncStatus() != null, Coin::getSyncStatus, bo.getSyncStatus());
        lqw.eq(bo.getIsDeleted() != null, Coin::getIsDeleted, bo.getIsDeleted());
        return lqw;
    }

    /**
     * 新增硬币
     */
    @Override
    public Boolean insertByBo(CoinBo bo) {
        Coin coin = MapstructUtils.convert(bo, Coin.class);
        validEntityBeforeSave(coin);
        return coinMapper.insert(coin) > 0;
    }

    /**
     * 批量删除硬币
     */
    @Override
    @Transactional
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些校验,例如关联校验
        }
        return coinMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 修改硬币
     */
    @Override
    public Boolean updateByBo(CoinBo bo) {
        Coin coin = MapstructUtils.convert(bo, Coin.class);
        validEntityBeforeSave(coin);
        return coinMapper.updateById(coin) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Coin entity) {
        // 校验逻辑
    }
}