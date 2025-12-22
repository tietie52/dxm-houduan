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
            // 不要抛出异常，避免服务自动关闭
            return 0;
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
            requestBody.put("inputs", Map.of("count", count));
            requestBody.put("response_mode", "blocking");
            requestBody.put("user", "ruoyi-system");
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // 调用Dify API - 使用workflow端点
            String apiEndpoint = "/workflows/run";
            String fullApiUrl = difyConfig.getApiUrl() + apiEndpoint;
            
            // 打印请求详细信息
            log.info("Dify请求配置：{}", Map.of(
                "url", fullApiUrl,
                "apiKey", difyConfig.getApiKey().substring(0, 10) + "...", // 脱敏打印
                "requestBody", requestBody,
                "headers", Map.of(
                    "Authorization", "Bearer " + difyConfig.getApiKey().substring(0, 10) + "...",
                    "Content-Type", MediaType.APPLICATION_JSON_VALUE
                )
            ));
            
            // 调用Dify API - 获取原始字符串响应
            log.info("开始调用Dify API");
            String responseBody = restTemplate.postForObject(fullApiUrl, requestEntity, String.class);
            log.info("Dify API调用完成，响应体字符串: {}", responseBody);
            
            if (responseBody == null || responseBody.isEmpty()) {
                log.warn("Dify API返回空响应");
                return new ArrayList<>();
            }
            
            // 处理Dify API响应
            List<CryptoNews> newsList = new ArrayList<>();
            
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
            
            // 提取新闻数据
            List<Map<String, Object>> newsDataList = new ArrayList<>();
            
            if (difyResponse instanceof List) {
                // 如果响应直接是数组
                List<?> listResponse = (List<?>) difyResponse;
                log.info("Dify响应直接是数组，包含{}个元素", listResponse.size());
                
                // 过滤Map类型的元素
                for (Object item : listResponse) {
                    if (item instanceof Map) {
                        newsDataList.add((Map<String, Object>) item);
                    }
                }
            } else if (difyResponse instanceof Map) {
                // 如果响应是对象
                Map<String, Object> mapResponse = (Map<String, Object>) difyResponse;
                log.info("Dify响应是对象，包含{}个字段", mapResponse.size());
                
                // 打印所有字段及其类型
                for (Map.Entry<String, Object> entry : mapResponse.entrySet()) {
                    Object value = entry.getValue();
                    log.info("Dify响应字段: {}, 值: {}, 类型: {}", 
                        entry.getKey(), value, value != null ? value.getClass().getName() : "null");
                }
                
                // 尝试从不同位置提取数据
                Object dataObj = mapResponse.get("data");
                Map<String, Object> dataMap = null;
                if (dataObj instanceof Map) {
                    dataMap = (Map<String, Object>) dataObj;
                    
                    // 检查content字段
                    Object contentObj = dataMap.get("content");
                    if (contentObj instanceof Map) {
                        Map<String, Object> contentMap = (Map<String, Object>) contentObj;
                        Object newsObj = contentMap.get("news");
                        if (newsObj instanceof List) {
                            newsDataList = (List<Map<String, Object>>) newsObj;
                        }
                    } else if (contentObj instanceof List) {
                        newsDataList = (List<Map<String, Object>>) contentObj;
                    } else if (contentObj instanceof String) {
                        // 如果content是字符串，尝试解析为JSON
                        log.info("Dify content是字符串: {}", contentObj);
                        try {
                            newsDataList = JsonUtils.parseObject((String) contentObj, new TypeReference<List<Map<String, Object>>>() {});
                        } catch (Exception e) {
                            log.error("解析content字符串为JSON失败: {}", e.getMessage(), e);
                        }
                    }
                }
                
                // 如果data.content中没有数据，尝试从data.outputs中提取
                if (newsDataList.isEmpty() && dataMap != null) {
                    // 从dataMap中获取outputs字段，而不是从根响应中获取
                    Object outputsObj = dataMap.get("outputs");
                    log.info("从dataMap中获取outputs字段，类型: {}, 值: {}", 
                        outputsObj != null ? outputsObj.getClass().getName() : "null", outputsObj);
                    
                    if (outputsObj instanceof Map) {
                        Map<String, Object> outputsMap = (Map<String, Object>) outputsObj;
                        
                        // 先尝试查找news字段（原逻辑保留）
                        Object newsObj = outputsMap.get("news");
                        if (newsObj instanceof List) {
                            newsDataList = (List<Map<String, Object>>) newsObj;
                            log.info("newsObj是List类型，包含{}个元素", newsDataList.size());
                        } else if (newsObj instanceof String) {
                            log.info("Dify outputs.news是字符串: {}", newsObj);
                            try {
                                newsDataList = JsonUtils.parseObject((String) newsObj, new TypeReference<List<Map<String, Object>>>() {});
                                log.info("成功解析outputs.news字符串为JSON，包含{}个元素", newsDataList.size());
                            } catch (Exception e) {
                                log.error("解析outputs.news字符串为JSON失败: {}", e.getMessage(), e);
                            }
                        }
                        
                        // 如果news字段没有数据，尝试查找post字段（Dify实际返回的字段）
                        if (newsDataList.isEmpty()) {
                            Object postObj = outputsMap.get("post");
                            log.info("Dify outputs.post的类型: {}, 值: {}", 
                                postObj != null ? postObj.getClass().getName() : "null", postObj);
                            
                            if (postObj instanceof List) {
                                newsDataList = (List<Map<String, Object>>) postObj;
                                log.info("postObj是List类型，包含{}个元素", newsDataList.size());
                            } else if (postObj instanceof String) {
                                log.info("Dify outputs.post是字符串: {}", postObj);
                                try {
                                    newsDataList = JsonUtils.parseObject((String) postObj, new TypeReference<List<Map<String, Object>>>() {});
                                    log.info("成功解析outputs.post字符串为JSON，包含{}个元素", newsDataList.size());
                                } catch (Exception e) {
                                    log.error("解析outputs.post字符串为JSON失败: {}", e.getMessage(), e);
                                }
                            } else {
                                log.warn("Dify outputs.post不是List也不是String类型，无法处理");
                            }
                        }
                    } else {
                        log.warn("Dify data.outputs不是Map类型，无法处理");
                    }
                }
                
                // 如果还是没有数据，尝试直接从response中找news字段
                if (newsDataList.isEmpty()) {
                    Object newsObj = mapResponse.get("news");
                    if (newsObj instanceof List) {
                        newsDataList = (List<Map<String, Object>>) newsObj;
                    }
                }
            }
            
            log.info("从Dify API提取到{}条新闻数据", newsDataList.size());
            
            // 转换为CryptoNews实体
            for (Map<String, Object> item : newsDataList) {
                CryptoNews news = convertToCryptoNews(item);
                if (news != null) {
                    newsList.add(news);
                }
            }
            
            log.info("成功转换为{}条CryptoNews实体", newsList.size());
            return newsList;
            
        } catch (Exception e) {
            log.error("调用Dify API异常: {}", e.getMessage(), e);
            return new ArrayList<>();
        } finally {
            log.info("Dify API调用流程结束");
        }
    }

    /**
     * 将Dify数据转换为CryptoNews实体
     */
    private CryptoNews convertToCryptoNews(Map<String, Object> item) {
        try {
            // 打印原始数据，便于调试
            log.info("开始转换Dify数据: {}", item);
            
            CryptoNews news = new CryptoNews();
            
            // 灵活处理不同数据结构
            String title = (String) item.get("title");
            String content = (String) item.get("content");
            
            // 如果没有title和content，可能是返回了硬币数据而不是新闻数据
            if (title == null || content == null) {
                // 检查是否是硬币数据
                String coinName = (String) item.get("name");
                String coinSymbol = (String) item.get("symbol");
                
                if (coinName != null && coinSymbol != null) {
                    // 处理硬币数据，为必填字段提供默认值
                    log.info("检测到硬币数据，进行特殊处理: {}, {}", coinName, coinSymbol);
                    news.setTitle(coinName + " 市场动态");
                    news.setContent("关于" + coinName + "(" + coinSymbol + ")的最新市场动态");
                    news.setCryptoType(coinSymbol);
                    news.setEmotion("neutral"); // 默认中性情绪
                    news.setPublishTime(LocalDateTime.now()); // 使用当前时间
                    news.setSource("Dify"); // 设置来源为Dify
                } else {
                    // 既不是新闻数据也不是硬币数据，记录错误
                    log.error("无法识别的数据结构: 缺少必要字段");
                    return null;
                }
            } else {
                // 正常处理新闻数据
                news.setTitle(title);
                news.setContent(content);
                news.setCryptoType((String) item.get("crypto_type"));
                news.setEmotion((String) item.get("emotion"));
                
                // 处理发布时间
                Object publishTimeObj = item.get("publish_time");
                if (publishTimeObj instanceof String) {
                    try {
                        // 假设Dify返回的时间格式为YYYY-MM-DD HH:mm:ss
                        String publishTimeStr = (String) publishTimeObj;
                        news.setPublishTime(LocalDateTime.parse(publishTimeStr));
                    } catch (Exception e) {
                        log.error("解析发布时间失败: {}", publishTimeObj, e);
                        news.setPublishTime(LocalDateTime.now()); // 使用当前时间作为默认值
                    }
                } else {
                    news.setPublishTime(LocalDateTime.now()); // 使用当前时间作为默认值
                }
                
                news.setSource((String) item.get("source") != null ? (String) item.get("source") : "Dify");
            }
            
            // 设置公共字段
            news.setCreateBy("system");
            news.setUpdateBy("system");
            news.setCreateTime(LocalDateTime.now());
            news.setUpdateTime(LocalDateTime.now());
            news.setIsDeleted(0);
            
            // 确保所有必填字段都有值
            if (news.getTitle() == null) news.setTitle("未知标题");
            if (news.getContent() == null) news.setContent("无内容");
            if (news.getCryptoType() == null) news.setCryptoType("unknown");
            if (news.getEmotion() == null) news.setEmotion("neutral");
            if (news.getPublishTime() == null) news.setPublishTime(LocalDateTime.now());
            if (news.getSource() == null) news.setSource("Dify");
            
            // 数据校验
            try {
                validEntityBeforeSave(news);
            } catch (Exception e) {
                log.error("数据校验失败: {}", e.getMessage(), e);
                // 如果校验失败，记录详细信息但仍然返回实体
                return news;
            }
            
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
            
            log.info("检查去重: 标题={}, 发布时间={}", news.getTitle(), news.getPublishTime());
            CryptoNews existingNews = cryptoNewsMapper.selectOne(lqw);
            if (existingNews == null) {
                log.info("数据不存在，添加到去重列表");
                uniqueList.add(news);
            } else {
                log.info("数据已存在，标题={}, ID={}", existingNews.getTitle(), existingNews.getId());
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
                log.info("保存数据: 标题={}, 加密货币类型={}", news.getTitle(), news.getCryptoType());
                if (cryptoNewsMapper.insert(news) > 0) {
                    log.info("保存成功，标题={}, ID={}", news.getTitle(), news.getId());
                    savedCount++;
                } else {
                    log.warn("保存失败，但没有抛出异常，标题={}", news.getTitle());
                }
            } catch (Exception e) {
                log.error("保存单条加密货币资讯数据失败: 标题={}, 错误={}", news.getTitle(), e.getMessage(), e);
                // 继续保存其他数据
            }
        }
        
        log.info("批量保存加密货币资讯数据完成，成功保存: {}", savedCount);
        return savedCount;
    }
}
