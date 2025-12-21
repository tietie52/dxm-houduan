package com.ruoyi.wms.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.common.web.core.BaseController;
import com.ruoyi.wms.config.DifyConfig;
import com.ruoyi.wms.domain.bo.CoinBo;
import com.ruoyi.wms.domain.entity.Coin;
import com.ruoyi.wms.domain.vo.CoinVo;
import com.ruoyi.wms.service.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Slf4j
public class CoinController extends BaseController {

    private final CoinService coinService;
    private final DifyConfig difyConfig;
    private final RestTemplate restTemplate;

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
     * 调用Dify API并保存硬币数据
     */
    @Log(title = "调用Dify API并保存硬币数据", businessType = BusinessType.INSERT)
    @PostMapping("/postDifyApi")
    public R<Void> postDifyApi(@RequestBody List<Map<String, Object>> coinList) {
        try {
            log.info("接收到硬币数据请求: {}", coinList);
            
            // 构建Dify API请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-KEY", difyConfig.getApiKey());
            
            // 构建Dify API请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", Map.of("coins", coinList));
            requestBody.put("response_mode", "blocking");
            requestBody.put("user", "ruoyi-system");
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 调用Dify API - 使用正确的端点
            String apiEndpoint = "/chat-messages";
            String fullApiUrl = difyConfig.getApiUrl() + apiEndpoint;
            log.info("开始调用Dify API，URL: {}", fullApiUrl);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                fullApiUrl, requestEntity, Map.class);
            
            log.info("Dify API调用完成，状态码: {}", response.getStatusCode());
            
            // 处理Dify API响应
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> difyResponse = response.getBody();
                log.info("Dify API调用成功，返回结果: {}", difyResponse);
                
                // 解析Dify响应数据并转换为Coin实体
                List<Coin> coins = parseDifyResponse(difyResponse);
                
                // 保存到数据库
                if (!coins.isEmpty()) {
                    boolean result = coinService.saveBatch(coins);
                    if (result) {
                        log.info("成功保存{}条硬币数据到数据库", coins.size());
                        return R.ok("对接Dify并保存数据成功");
                    } else {
                        log.error("保存硬币数据到数据库失败");
                        return R.fail("保存数据失败");
                    }
                } else {
                    log.warn("从Dify API获取的数据为空");
                    return R.fail("从Dify API获取的数据为空");
                }
            } else {
                log.error("Dify API调用失败，状态码: {}, 响应体: {}", response.getStatusCode(), response.getBody());
                return R.fail("调用Dify API失败，状态码: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("对接Dify异常", e);
            return R.fail("对接Dify异常: " + e.getMessage());
        }
    }
    
    /**
     * 解析Dify API响应数据
     */
    private List<Coin> parseDifyResponse(Map<String, Object> difyResponse) {
        List<Coin> coins = new ArrayList<>();
        
        try {
            log.info("Dify API完整响应: {}", difyResponse);
            
            if (difyResponse != null) {
                // 提取响应数据 - 不同的Dify应用可能有不同的响应结构
                Object dataObj = difyResponse.get("data");
                if (dataObj instanceof Map) {
                    Map<String, Object> dataMap = (Map<String, Object>) dataObj;
                    Object contentObj = dataMap.get("content");
                    
                    if (contentObj instanceof String) {
                        // 如果返回的是JSON字符串，需要解析
                        String contentStr = (String) contentObj;
                        log.info("Dify API返回的content字符串: {}", contentStr);
                        
                        // 这里可以根据实际返回的JSON结构进行解析
                        // 示例：假设contentStr是包含coins数组的JSON字符串
                        // ObjectMapper mapper = new ObjectMapper();
                        // Map<String, Object> contentMap = mapper.readValue(contentStr, Map.class);
                        // List<Map<String, Object>> coinDataList = (List<Map<String, Object>>) contentMap.get("coins");
                    } else if (contentObj instanceof Map) {
                        Map<String, Object> contentMap = (Map<String, Object>) contentObj;
                        Object coinsObj = contentMap.get("coins");
                        if (coinsObj instanceof List) {
                            List<Map<String, Object>> coinDataList = (List<Map<String, Object>>) coinsObj;
                            for (Map<String, Object> coinData : coinDataList) {
                                Coin coin = new Coin();
                                coin.setCoinName((String) coinData.get("name"));
                                coin.setCoinCode((String) coinData.get("symbol"));
                                coin.setSyncStatus(1); // 同步成功
                                coin.setSyncTime(LocalDateTime.now());
                                coin.setSyncSource("Dify API");
                                coin.setIsDeleted(0);
                                coin.setCreateTime(LocalDateTime.now());
                                coin.setUpdateTime(LocalDateTime.now());
                                coin.setCreateBy("system");
                                coin.setUpdateBy("system");
                                coins.add(coin);
                            }
                        }
                    }
                }
                
                // 备用解析逻辑：直接从响应中提取coins字段
                Object coinsObj = difyResponse.get("coins");
                if (coinsObj instanceof List) {
                    List<Map<String, Object>> coinDataList = (List<Map<String, Object>>) coinsObj;
                    for (Map<String, Object> coinData : coinDataList) {
                        Coin coin = new Coin();
                        coin.setCoinName((String) coinData.get("name"));
                        coin.setCoinCode((String) coinData.get("symbol"));
                        coin.setSyncStatus(1); // 同步成功
                        coin.setSyncTime(LocalDateTime.now());
                        coin.setSyncSource("Dify API");
                        coin.setIsDeleted(0);
                        coin.setCreateTime(LocalDateTime.now());
                        coin.setUpdateTime(LocalDateTime.now());
                        coin.setCreateBy("system");
                        coin.setUpdateBy("system");
                        coins.add(coin);
                    }
                }
            }
            
            log.info("成功解析{}条硬币数据", coins.size());
            return coins;
        } catch (Exception e) {
            log.error("解析Dify API响应数据失败", e);
            return coins;
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