-- 洽谈室预约管理系统测试数据

-- ----------------------------
-- 洽谈室表测试数据
-- ----------------------------
INSERT INTO oa_neg_room VALUES (1, '洽谈室A', '一楼东侧', 10, '投影仪,白板,音响', 30, '0', 1, 'admin', sysdate(), '', null, '标准洽谈室');
INSERT INTO oa_neg_room VALUES (2, '洽谈室B', '一楼西侧', 8, '投影仪,白板', 15, '0', 2, 'admin', sysdate(), '', null, '小型洽谈室');
INSERT INTO oa_neg_room VALUES (3, '会议室C', '二楼东侧', 20, '投影仪,音响,视频会议系统', 45, '0', 3, 'admin', sysdate(), '', null, '大型会议室');
INSERT INTO oa_neg_room VALUES (4, 'VIP洽谈室', '三楼VIP区', 6, '投影仪,白板,咖啡机', 60, '0', 4, 'admin', sysdate(), '', null, '高级洽谈室');
INSERT INTO oa_neg_room VALUES (5, '培训室', '二楼西侧', 30, '投影仪,音响,白板', 0, '1', 5, 'admin', sysdate(), '', null, '暂时停用');

-- ----------------------------
-- 茶水表测试数据
-- ----------------------------
INSERT INTO oa_neg_tea VALUES (1, '龙井茶', '茶叶', 50, '0', 'admin', sysdate(), '', null, '优质绿茶');
INSERT INTO oa_neg_tea VALUES (2, '铁观音', '茶叶', 40, '0', 'admin', sysdate(), '', null, '经典乌龙茶');
INSERT INTO oa_neg_tea VALUES (3, '普洱茶', '茶叶', 30, '0', 'admin', sysdate(), '', null, '陈年普洱');
INSERT INTO oa_neg_tea VALUES (4, '美式咖啡', '咖啡', 100, '0', 'admin', sysdate(), '', null, '标准美式');
INSERT INTO oa_neg_tea VALUES (5, '拿铁咖啡', '咖啡', 80, '0', 'admin', sysdate(), '', null, '经典拿铁');
INSERT INTO oa_neg_tea VALUES (6, '卡布奇诺', '咖啡', 60, '0', 'admin', sysdate(), '', null, '意式卡布奇诺');
INSERT INTO oa_neg_tea VALUES (7, '橙汁', '果汁', 30, '0', 'admin', sysdate(), '', null, '鲜榨橙汁');
INSERT INTO oa_neg_tea VALUES (8, '苹果汁', '果汁', 25, '0', 'admin', sysdate(), '', null, '鲜榨苹果汁');
INSERT INTO oa_neg_tea VALUES (9, '矿泉水', '其他', 200, '0', 'admin', sysdate(), '', null, '天然矿泉水');
INSERT INTO oa_neg_tea VALUES (10, '红茶', '茶叶', 45, '1', 'admin', sysdate(), '', null, '缺货');

-- ----------------------------
-- 预约表测试数据
-- ----------------------------
-- 今天的预约
INSERT INTO oa_neg_log VALUES (1, '客户A项目洽谈', 1, '洽谈室A', 1, 1, '张律师', '客户A', '13800138001', '合同审查', 
    DATE_ADD(CURDATE(), INTERVAL 10 HOUR), DATE_ADD(CURDATE(), INTERVAL 11 HOUR), '1', 'admin', sysdate(), '', null, '重要客户');
    
INSERT INTO oa_neg_log VALUES (2, '合作方案讨论', 2, '洽谈室B', 2, 1, '李律师', '合作伙伴B', '13800138002', '业务合作', 
    DATE_ADD(CURDATE(), INTERVAL 14 HOUR), DATE_ADD(CURDATE(), INTERVAL 15 HOUR), '0', 'admin', sysdate(), '', null, '初步洽谈');

-- 明天的预约
INSERT INTO oa_neg_log VALUES (3, '投资协议签署', 3, '会议室C', 1, 1, '张律师', '投资方C', '13800138003', '投资协议', 
    DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 1 DAY), INTERVAL 9 HOUR), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 1 DAY), INTERVAL 11 HOUR), '1', 'admin', sysdate(), '', null, '签约仪式');

INSERT INTO oa_neg_log VALUES (4, '涉外案件沟通', 4, 'VIP洽谈室', 3, 1, '王律师', '外方代表D', '13800138004', '国际贸易纠纷', 
    DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 1 DAY), INTERVAL 14 HOUR), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 1 DAY), INTERVAL 16 HOUR), '0', 'admin', sysdate(), '', null, '涉外案件');

-- 后天的预约
INSERT INTO oa_neg_log VALUES (5, '知识产权咨询', 1, '洽谈室A', 2, 1, '李律师', '客户E', '13800138005', '专利申请', 
    DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 2 DAY), INTERVAL 10 HOUR), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 2 DAY), INTERVAL 12 HOUR), '1', 'admin', sysdate(), '', null, '常规咨询');

-- 已完成的预约
INSERT INTO oa_neg_log VALUES (6, '合同纠纷调解', 2, '洽谈室B', 1, 1, '张律师', '甲方客户', '13800138006', '合同纠纷', 
    DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -2 DAY), INTERVAL 10 HOUR), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -2 DAY), INTERVAL 11 HOUR), '3', 'admin', sysdate(), '', null, '已调解完成');

-- 已取消的预约
INSERT INTO oa_neg_log VALUES (7, '初步咨询', 3, '会议室C', 3, 1, '王律师', '潜在客户', '13800138007', '法律咨询', 
    DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -1 DAY), INTERVAL 15 HOUR), DATE_ADD(DATE_ADD(CURDATE(), INTERVAL -1 DAY), INTERVAL 16 HOUR), '4', 'admin', sysdate(), '', null, '客户临时取消');

-- ----------------------------
-- 预约茶水关联表测试数据
-- ----------------------------
-- 预约1的茶水需求
INSERT INTO oa_neg_log_tea VALUES (1, 1, 1, 2, 'admin', sysdate(), '', null, '龙井茶2杯');
INSERT INTO oa_neg_log_tea VALUES (2, 1, 4, 2, 'admin', sysdate(), '', null, '美式咖啡2杯');
INSERT INTO oa_neg_log_tea VALUES (3, 1, 9, 2, 'admin', sysdate(), '', null, '矿泉水2瓶');

-- 预约2的茶水需求
INSERT INTO oa_neg_log_tea VALUES (4, 2, 2, 1, 'admin', sysdate(), '', null, '铁观音1杯');
INSERT INTO oa_neg_log_tea VALUES (5, 2, 5, 1, 'admin', sysdate(), '', null, '拿铁咖啡1杯');

-- 预约3的茶水需求
INSERT INTO oa_neg_log_tea VALUES (6, 3, 3, 1, 'admin', sysdate(), '', null, '普洱茶1杯');
INSERT INTO oa_neg_log_tea VALUES (7, 3, 6, 2, 'admin', sysdate(), '', null, '卡布奇诺2杯');
INSERT INTO oa_neg_log_tea VALUES (8, 3, 7, 1, 'admin', sysdate(), '', null, '橙汁1杯');

-- 预约4的茶水需求
INSERT INTO oa_neg_log_tea VALUES (9, 4, 1, 1, 'admin', sysdate(), '', null, '龙井茶1杯');
INSERT INTO oa_neg_log_tea VALUES (10, 4, 8, 1, 'admin', sysdate(), '', null, '苹果汁1杯');

-- 预约5的茶水需求
INSERT INTO oa_neg_log_tea VALUES (11, 5, 2, 2, 'admin', sysdate(), '', null, '铁观音2杯');