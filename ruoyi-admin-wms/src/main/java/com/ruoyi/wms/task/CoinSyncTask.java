package com.ruoyi.wms.task;

import com.ruoyi.wms.service.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 硬币数据同步定时任务
 *
 * @author zcc
 * @date 2024-12-19
 */
@RequiredArgsConstructor
@Component
@Log4j2
public class CoinSyncTask {

    private final CoinService coinService;

    /**
     * 定时从Dify同步硬币数据
     * 每30分钟执行一次
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void syncCoinDataFromDify() {
        log.info("开始执行定时同步硬币数据任务");
        try {
            int syncCount = coinService.syncFromDify();
            log.info("定时同步硬币数据任务执行完成，共同步{}条数据", syncCount);
        } catch (Exception e) {
            log.error("定时同步硬币数据任务执行失败: {}", e.getMessage(), e);
        }
    }
}