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
import com.ruoyi.wms.domain.bo.CryptoNewsBo;
import com.ruoyi.wms.domain.entity.CryptoNews;
import com.ruoyi.wms.domain.vo.CryptoNewsVo;
import com.ruoyi.wms.mapper.CryptoNewsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.ruoyi.common.json.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加密货币资讯服务实现类
 *
 * @author zcc
 * @date 2025-12-16
 */
@Service
@Log4j2
public class CryptoNewsServiceImpl implements CryptoNewsService {

    private final CryptoNewsMapper cryptoNewsMapper;
    private final RestTemplate restTemplate;
    private final DifyConfig difyConfig;

    public CryptoNewsServiceImpl(CryptoNewsMapper cryptoNewsMapper, RestTemplate restTemplate, DifyConfig difyConfig) {
        this.cryptoNewsMapper = cryptoNewsMapper;
        this.restTemplate = restTemplate;
        this.difyConfig = difyConfig;
    }

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

    /**
     * 从Dify同步加密货币资讯数据
     * 手动接口调用，获取5条数据
     */
    @Override
    @Transactional
    public Integer syncFromDify() {
        return syncFromDifyWithLock(5);
    }

    /**
     * 从Dify同步加密货币资讯数据（带锁）
     * 通用方法，支持手动同步和定时任务，防止并发冲突
     *
     * @param count 获取资讯数量
     * @return 同步成功的数量
     */
    @Override
    @Transactional
    public synchronized Integer syncFromDifyWithLock(int count) {
        log.info("开始同步加密货币资讯数据，获取数量: {}", count);
        
        try {
            // 从Dify获取加密货币资讯数据
            List<CryptoNews> newsList = fetchNewsDataFromDify(count);
            
            if (CollUtil.isEmpty(newsList)) {
                log.warn("从Dify获取的加密货币资讯数据为空");
                return 0;
            }
            
            // 去重处理
            List<CryptoNews> uniqueNewsList = removeDuplicates(newsList);
            
            if (CollUtil.isEmpty(uniqueNewsList)) {
                log.warn("去重后无新的加密货币资讯数据");
                return 0;
            }
            
            // 批量保存数据
            int savedCount = saveNewsBatch(uniqueNewsList);
            
            log.info("同步加密货币资讯数据成功，共同步{}条数据", savedCount);
            return savedCount;
        } catch (Exception e) {
            log.error("同步加密货币资讯数据异常: {}", e.getMessage(), e);
            throw new RuntimeException("同步失败: " + e.getMessage());
        }
    }

    /**
     * 从Dify获取加密货币资讯数据
     *
     * @param count 获取资讯数量
     */
    private List<CryptoNews> fetchNewsDataFromDify(int count) {
        log.info("调用Dify API获取加密货币资讯数据，数量: {}", count);
        
        try {
            // 构建Dify API请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + difyConfig.getApiKey());
            
            // 构建Dify API请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", Map.of("query", "获取" + count + "条最新加密货币资讯数据"));
            requestBody.put("response_mode", "blocking");
            requestBody.put("user", "ruoyi-system");
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 调用Dify API - 使用workflow端点
            String apiEndpoint = "/workflows/run";
            String fullApiUrl = difyConfig.getApiUrl() + apiEndpoint;
            log.info("开始调用Dify API，URL: {}", fullApiUrl);
            
            // 调用Dify API - 获取原始字符串响应
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
                        }
                    } else if (difyResponse instanceof Map) {
                        // 处理根节点是对象的情况
                        Map<String, Object> mapResponse = (Map<String, Object>) difyResponse;
                        
                        // 1. 首先尝试直接从响应根节点提取news字段
                        Object newsObj = mapResponse.get("news");
                        if (newsObj instanceof List) {
                            List<?> newsList = (List<?>) newsObj;
                            if (!newsList.isEmpty() && newsList.get(0) instanceof Map) {
                                difyData = (List<Map<String, Object>>) newsList;
                                log.info("从响应根节点news获取到{}条数据", difyData.size());
                            }
                        }
                        
                        // 2. 如果news字段不在根节点，尝试从data.content或data.outputs结构中提取
                        if (difyData.isEmpty()) {
                            Object dataObj = mapResponse.get("data");
                            if (dataObj instanceof Map) {
                                Map<String, Object> dataMap = (Map<String, Object>) dataObj;
                                
                                // 2.1 尝试从data.outputs中获取news字段
                                Object outputsObj = dataMap.get("outputs");
                                if (outputsObj instanceof Map) {
                                    Map<String, Object> outputsMap = (Map<String, Object>) outputsObj;
                                    Object outputsNewsObj = outputsMap.get("news");
                                    if (outputsNewsObj instanceof String) {
                                        String newsJson = (String) outputsNewsObj;
                                        log.info("从data.outputs中获取的news字符串: {}", newsJson);
                                        
                                        try {
                                            List<Map<String, Object>> parsedList = JsonUtils.parseObject(newsJson, new TypeReference<List<Map<String, Object>>>() {});
                                            difyData = parsedList;
                                            log.info("成功解析data.outputs.news中的JSON字符串，共{}条数据", difyData.size());
                                        } catch (Exception e) {
                                            log.error("解析data.outputs.news中的JSON字符串失败: {}", e.getMessage(), e);
                                        }
                                    }
                                }
                                
                                // 2.2 如果outputs中没有有效数据，尝试从content中获取
                                if (difyData.isEmpty()) {
                                    Object contentObj = dataMap.get("content");
                                    if (contentObj instanceof String) {
                                        // 如果content返回的是JSON字符串
                                        String contentStr = (String) contentObj;
                                        log.info("Dify API返回的content字符串: {}", contentStr);
                                        try {
                                            List<Map<String, Object>> parsedList = JsonUtils.parseObject(contentStr, new TypeReference<List<Map<String, Object>>>() {});
                                            difyData = parsedList;
                                            log.info("成功解析content字符串为列表，共{}条数据", difyData.size());
                                        } catch (Exception e) {
                                            log.error("解析content字符串失败: {}", e.getMessage(), e);
                                        }
                                    } else if (contentObj instanceof Map) {
                                        Map<String, Object> contentMap = (Map<String, Object>) contentObj;
                                        Object contentNewsObj = contentMap.get("news");
                                        if (contentNewsObj instanceof List) {
                                            List<?> newsList = (List<?>) contentNewsObj;
                                            if (!newsList.isEmpty() && newsList.get(0) instanceof Map) {
                                                difyData = (List<Map<String, Object>>) newsList;
                                                log.info("从content.news获取到{}条数据", difyData.size());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                log.info("从Dify API获取到{}条有效数据", difyData.size());
                
                // 处理响应数据
                List<CryptoNews> newsList = new ArrayList<>();
                
                // 将Dify数据转换为CryptoNews实体
                for (Map<String, Object> item : difyData) {
                    CryptoNews news = convertToCryptoNews(item);
                    if (news != null) {
                        newsList.add(news);
                    }
                }
                
                log.info("成功转换{}条Dify数据为CryptoNews实体", newsList.size());
                return newsList;
            } else {
                log.warn("Dify API返回的响应体为空");
                return new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("调用Dify API获取加密货币资讯数据失败: {}", e.getMessage(), e);
            throw new RuntimeException("Dify接口调用失败: " + e.getMessage());
        }
    }

    /**
     * 将Dify数据转换为CryptoNews实体
     */
    private CryptoNews convertToCryptoNews(Map<String, Object> item) {
        try {
            CryptoNews news = new CryptoNews();
            
            // 映射字段
            news.setTitle((String) item.get("title"));
            news.setContent((String) item.get("content"));
            news.setCryptoType((String) item.get("crypto_type"));
            news.setEmotion((String) item.get("emotion"));
            
            // 处理发布时间
            Object publishTimeObj = item.get("publish_time");
            if (publishTimeObj instanceof String) {
                // 假设Dify返回的时间格式为YYYY-MM-DD HH:mm:ss
                String publishTimeStr = (String) publishTimeObj;
                news.setPublishTime(LocalDateTime.parse(publishTimeStr));
            }
            
            news.setSource((String) item.get("source"));
            
            // 设置公共字段
            news.setCreateBy("system");
            news.setUpdateBy("system");
            news.setCreateTime(LocalDateTime.now());
            news.setUpdateTime(LocalDateTime.now());
            news.setIsDeleted(0);
            
            // 数据校验
            validEntityBeforeSave(news);
            
            return news;
        } catch (Exception e) {
            log.error("转换Dify数据为CryptoNews实体失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 去重处理
     */
    private List<CryptoNews> removeDuplicates(List<CryptoNews> newsList) {
        log.info("开始去重处理，原数据量: {}", newsList.size());
        
        List<CryptoNews> uniqueList = new ArrayList<>();
        
        for (CryptoNews news : newsList) {
            // 根据title+publish_time判断是否已存在
            LambdaQueryWrapper<CryptoNews> lqw = Wrappers.lambdaQuery();
            lqw.eq(CryptoNews::getTitle, news.getTitle());
            lqw.eq(CryptoNews::getPublishTime, news.getPublishTime());
            
            CryptoNews existingNews = cryptoNewsMapper.selectOne(lqw);
            if (existingNews == null) {
                uniqueList.add(news);
            }
        }
        
        log.info("去重处理完成，去重后数据量: {}", uniqueList.size());
        return uniqueList;
    }

    /**
     * 批量保存加密货币资讯数据
     */
    private int saveNewsBatch(List<CryptoNews> newsList) {
        log.info("开始批量保存加密货币资讯数据，数量: {}", newsList.size());
        
        if (CollUtil.isEmpty(newsList)) {
            return 0;
        }
        
        int savedCount = 0;
        for (CryptoNews news : newsList) {
            try {
                if (cryptoNewsMapper.insert(news) > 0) {
                    savedCount++;
                }
            } catch (Exception e) {
                log.error("保存单条加密货币资讯数据失败: {}", e.getMessage(), e);
                // 继续保存其他数据
            }
        }
        
        log.info("批量保存加密货币资讯数据完成，成功保存: {}", savedCount);
        return savedCount;
    }
}
