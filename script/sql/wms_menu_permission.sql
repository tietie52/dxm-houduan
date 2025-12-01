-- 消息分析报告模块菜单和权限配置
-- 作者: Lion Li
-- 日期: 2024-11-04

-- 插入消息分析报告模块菜单
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('消息分析报告', 0, 6, '/wms/messageAnalysisReport', NULL, '1', '0', 'M', '1', '1', NULL, 'message', 'admin', NOW(), '', NOW(), '消息分析报告模块'),
('报告列表', 2000, 1, 'list', 'wms/messageAnalysisReport/index', '1', '0', 'C', '1', '1', 'wms:messageAnalysisReport:list', 'list', 'admin', NOW(), '', NOW(), '消息分析报告列表'),
('报告查询', 2001, 1, '', NULL, '1', '0', 'F', '1', '1', 'wms:messageAnalysisReport:query', '#', 'admin', NOW(), '', NOW(), '报告查询'),
('报告新增', 2001, 2, '', NULL, '1', '0', 'F', '1', '1', 'wms:messageAnalysisReport:add', '#', 'admin', NOW(), '', NOW(), '报告新增'),
('报告修改', 2001, 3, '', NULL, '1', '0', 'F', '1', '1', 'wms:messageAnalysisReport:edit', '#', 'admin', NOW(), '', NOW(), '报告修改'),
('报告删除', 2001, 4, '', NULL, '1', '0', 'F', '1', '1', 'wms:messageAnalysisReport:remove', '#', 'admin', NOW(), '', NOW(), '报告删除'),
('报告导出', 2001, 5, '', NULL, '1', '0', 'F', '1', '1', 'wms:messageAnalysisReport:export', '#', 'admin', NOW(), '', NOW(), '报告导出'),
('待审核报告', 2000, 2, 'pending', 'wms/messageAnalysisReport/pending', '1', '0', 'C', '1', '1', 'wms:messageAnalysisReport:pendingList', 'audit', 'admin', NOW(), '', NOW(), '待审核报告列表'),
('报告审核', 2007, 1, '', NULL, '1', '0', 'F', '1', '1', 'wms:messageAnalysisReport:audit', '#', 'admin', NOW(), '', NOW(), '报告审核'),
('执行调整', 2001, 6, '', NULL, '1', '0', 'F', '1', '1', 'wms:messageAnalysisReport:execute', '#', 'admin', NOW(), '', NOW(), '执行调整方案');

-- 更新菜单ID（确保ID唯一性）
UPDATE `sys_menu` SET `menu_id` = 2000 WHERE `menu_name` = '消息分析报告';
UPDATE `sys_menu` SET `menu_id` = 2001 WHERE `menu_name` = '报告列表';
UPDATE `sys_menu` SET `menu_id` = 2002 WHERE `menu_name` = '报告查询';
UPDATE `sys_menu` SET `menu_id` = 2003 WHERE `menu_name` = '报告新增';
UPDATE `sys_menu` SET `menu_id` = 2004 WHERE `menu_name` = '报告修改';
UPDATE `sys_menu` SET `menu_id` = 2005 WHERE `menu_name` = '报告删除';
UPDATE `sys_menu` SET `menu_id` = 2006 WHERE `menu_name` = '报告导出';
UPDATE `sys_menu` SET `menu_id` = 2007 WHERE `menu_name` = '待审核报告';
UPDATE `sys_menu` SET `menu_id` = 2008 WHERE `menu_name` = '报告审核';
UPDATE `sys_menu` SET `menu_id` = 2009 WHERE `menu_name` = '执行调整';

-- 更新父菜单ID
UPDATE `sys_menu` SET `parent_id` = 2000 WHERE `menu_id` IN (2001, 2007);
UPDATE `sys_menu` SET `parent_id` = 2001 WHERE `menu_id` IN (2002, 2003, 2004, 2005, 2006, 2009);
UPDATE `sys_menu` SET `parent_id` = 2007 WHERE `menu_id` = 2008;

-- 插入角色权限关联（为管理员角色添加消息分析报告权限）
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 2000), -- 管理员角色关联消息分析报告模块
(1, 2001), -- 报告列表
(1, 2002), -- 报告查询
(1, 2003), -- 报告新增
(1, 2004), -- 报告修改
(1, 2005), -- 报告删除
(1, 2006), -- 报告导出
(1, 2007), -- 待审核报告
(1, 2008), -- 报告审核
(1, 2009); -- 执行调整

-- 插入字典数据（审核状态和执行状态）
INSERT INTO `sys_dict_type` (`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('审核状态', 'wms_audit_status', '1', 'admin', NOW(), '', NOW(), '消息分析报告审核状态'),
('执行状态', 'wms_execute_status', '1', 'admin', NOW(), '', NOW(), '消息分析报告执行状态');

INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, '待审核', '0', 'wms_audit_status', '', 'primary', 'N', '1', 'admin', NOW(), '', NOW(), '待审核状态'),
(2, '已通过', '1', 'wms_audit_status', '', 'success', 'N', '1', 'admin', NOW(), '', NOW(), '审核通过状态'),
(3, '已拒绝', '2', 'wms_audit_status', '', 'danger', 'N', '1', 'admin', NOW(), '', NOW(), '审核拒绝状态'),
(1, '未执行', '0', 'wms_execute_status', '', 'warning', 'N', '1', 'admin', NOW(), '', NOW(), '未执行状态'),
(2, '已执行', '1', 'wms_execute_status', '', 'success', 'N', '1', 'admin', NOW(), '', NOW(), '已执行状态');

-- 插入参数配置（可选）
INSERT INTO `sys_config` (`config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('消息分析报告默认审核人', 'wms.message.analysis.default.auditor', 'admin', 'Y', 'admin', NOW(), '', NOW(), '消息分析报告默认审核人'),
('持仓查询接口URL', 'wms.holding.query.url', '/api/holding/query', 'Y', 'admin', NOW(), '', NOW(), '持仓查询接口URL'),
('AI报告接收接口URL', 'wms.ai.report.url', '/wms/messageAnalysisReport/ai/receive', 'Y', 'admin', NOW(), '', NOW(), 'AI报告接收接口URL');