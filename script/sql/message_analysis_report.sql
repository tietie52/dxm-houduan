-- 消息分析报告表
-- 作者: Lion Li
-- 日期: 2024-11-04

CREATE TABLE `message_analysis_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `message_id` bigint(20) DEFAULT NULL COMMENT '消息ID',
  `message_date` datetime DEFAULT NULL COMMENT '消息日期',
  `analysis_result` json DEFAULT NULL COMMENT '分析结果(JSON)',
  `holding_snapshot` json DEFAULT NULL COMMENT '持仓快照(JSON)',
  `adjustment_plan` json DEFAULT NULL COMMENT '调整方案(JSON)',
  `report_time` datetime DEFAULT NULL COMMENT '报告生成时间',
  `audit_status` tinyint(1) DEFAULT 0 COMMENT '审核状态(0-待审核,1-已通过,2-已拒绝)',
  `auditor` varchar(64) DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_comments` varchar(500) DEFAULT NULL COMMENT '审核意见',
  `executed` tinyint(1) DEFAULT 0 COMMENT '执行状态(0-未执行,1-已执行)',
  `execute_time` datetime DEFAULT NULL COMMENT '执行时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_executed` (`executed`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息分析报告表';

-- 初始化数据示例
INSERT INTO `message_analysis_report` (
  `message_id`, `message_date`, `analysis_result`, `holding_snapshot`, `adjustment_plan`, `report_time`
) VALUES (
  1001, 
  '2024-11-04 09:00:00',
  '{"marketTrend": "上涨", "riskLevel": "中等", "recommendation": "建议增持BTC"}',
  '{"totalValue": 10000000, "assets": [{"symbol": "BTC", "percentage": 40, "amount": 40}, {"symbol": "ETH", "percentage": 35, "amount": 35}, {"symbol": "SOL", "percentage": 15, "amount": 15}, {"symbol": "USDT", "percentage": 10, "amount": 10}]}',
  '{"adjustments": [{"symbol": "BTC", "newPercentage": 45, "change": 5}, {"symbol": "ETH", "newPercentage": 33, "change": -2}, {"symbol": "SOL", "newPercentage": 12, "change": -3}, {"symbol": "USDT", "newPercentage": 10, "change": 0}], "reason": "基于市场分析，建议增加BTC配置"}',
  '2024-11-04 10:00:00'
);

-- 创建持仓表（用于存储当前持仓数据）
CREATE TABLE `asset_holding` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `symbol` varchar(20) NOT NULL COMMENT '资产符号',
  `symbol_name` varchar(100) NOT NULL COMMENT '资产名称',
  `percentage` decimal(10,2) DEFAULT 0.00 COMMENT '持仓百分比',
  `amount` decimal(20,8) DEFAULT 0.00000000 COMMENT '持仓数量',
  `value` decimal(20,2) DEFAULT 0.00 COMMENT '持仓价值(USD)',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_symbol` (`symbol`),
  KEY `idx_update_time` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资产持仓表';

-- 初始化持仓数据（1000万美元初始配置）
INSERT INTO `asset_holding` (`symbol`, `symbol_name`, `percentage`, `amount`, `value`) VALUES
('BTC', 'Bitcoin', 40.00, 0, 4000000.00),
('ETH', 'Ethereum', 35.00, 0, 3500000.00),
('SOL', 'Solana', 15.00, 0, 1500000.00),
('USDT', 'Tether', 10.00, 0, 1000000.00);

-- 创建消息表（用于存储市场消息）
CREATE TABLE `market_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '消息标题',
  `content` text COMMENT '消息内容',
  `message_type` varchar(20) DEFAULT 'news' COMMENT '消息类型(news/analysis/alert)',
  `sentiment` varchar(10) DEFAULT 'neutral' COMMENT '情感倾向(positive/negative/neutral)',
  `source` varchar(100) DEFAULT NULL COMMENT '消息来源',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_sentiment` (`sentiment`),
  KEY `idx_message_type` (`message_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='市场消息表';

-- 创建持仓历史表（用于记录持仓变化历史）
CREATE TABLE `holding_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `report_id` bigint(20) DEFAULT NULL COMMENT '报告ID',
  `symbol` varchar(20) NOT NULL COMMENT '资产符号',
  `old_percentage` decimal(10,2) DEFAULT 0.00 COMMENT '调整前百分比',
  `new_percentage` decimal(10,2) DEFAULT 0.00 COMMENT '调整后百分比',
  `change_amount` decimal(10,2) DEFAULT 0.00 COMMENT '变化量',
  `execute_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`),
  KEY `idx_symbol` (`symbol`),
  KEY `idx_execute_time` (`execute_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='持仓历史表';