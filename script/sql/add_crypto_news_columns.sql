-- 为crypto_news表添加缺失的create_by和update_by列
-- 作者: zcc
-- 日期: 2025-12-19

ALTER TABLE `crypto_news`
ADD COLUMN `create_by` varchar(64) DEFAULT '' COMMENT '创建者' AFTER `source`,
ADD COLUMN `update_by` varchar(64) DEFAULT '' COMMENT '更新者' AFTER `create_by`;

-- 更新现有数据的create_by和update_by为默认值
UPDATE `crypto_news` SET `create_by` = '', `update_by` = '' WHERE `create_by` IS NULL OR `update_by` IS NULL;
