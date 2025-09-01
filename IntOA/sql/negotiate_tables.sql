-- 洽谈室预约管理系统表结构
-- 厺除不必要的外键约束，提高性能和灵活性

-- ----------------------------
-- 预约表
-- ----------------------------
DROP TABLE IF EXISTS oa_neg_log;
CREATE TABLE oa_neg_log (
    log_id         BIGINT AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
    title          VARCHAR(255) NOT NULL COMMENT '预约标题',
    room_id        BIGINT NOT NULL COMMENT '房间ID',
    room_name      VARCHAR(50) NOT NULL COMMENT '房间名称（创建时的快照）',
    user_id        BIGINT NOT NULL COMMENT '律师用户ID（关联 sys_user.user_id）',
    role_id        BIGINT NOT NULL COMMENT '律师角色ID（关联 sys_role.role_id）- 用于数据权限',
    nick_name      VARCHAR(50) NOT NULL COMMENT '用户昵称',
    client_name    VARCHAR(100) NOT NULL COMMENT '当事人姓名',
    client_contact VARCHAR(50) NULL COMMENT '当事人联系方式',
    case_reference VARCHAR(255) NULL COMMENT '相关案号/案由',
    start_time     DATETIME NOT NULL COMMENT '开始时间',
    end_time       DATETIME NOT NULL COMMENT '结束时间',
    status         CHAR DEFAULT '0' NOT NULL COMMENT '状态（0待确认 1已确认 2已签到 3已完成 4已取消）',
    create_by      VARCHAR(64) DEFAULT '' NULL COMMENT '创建者',
    create_time    DATETIME NULL COMMENT '创建时间',
    update_by      VARCHAR(64) DEFAULT '' NULL COMMENT '更新者',
    update_time    DATETIME NULL COMMENT '更新时间',
    remark         VARCHAR(500) NULL COMMENT '备注'
) ENGINE=InnoDB COMMENT '预约表' COLLATE = utf8mb4_unicode_ci;

-- 创建索引以提高查询性能和冲突检测效率
CREATE INDEX idx_neg_log_room_id ON oa_neg_log (room_id);
CREATE INDEX idx_neg_log_user_id ON oa_neg_log (user_id);
CREATE INDEX idx_neg_log_role_id ON oa_neg_log (role_id);
CREATE INDEX idx_neg_log_start_time ON oa_neg_log (start_time);
CREATE INDEX idx_neg_log_end_time ON oa_neg_log (end_time);
CREATE INDEX idx_neg_log_status ON oa_neg_log (status);
-- 为冲突检测创建复合索引
CREATE INDEX idx_neg_log_room_time ON oa_neg_log (room_id, start_time, end_time);
CREATE INDEX idx_neg_log_user_time ON oa_neg_log (user_id, start_time, end_time);

-- ----------------------------
-- 洽谈室表
-- ----------------------------
DROP TABLE IF EXISTS oa_neg_room;
CREATE TABLE oa_neg_room (
    room_id     BIGINT AUTO_INCREMENT COMMENT '洽谈室主键ID' PRIMARY KEY,
    room_name   VARCHAR(50) NOT NULL COMMENT '房间名称',
    location    VARCHAR(100) NULL COMMENT '位置',
    capacity    BIGINT NULL COMMENT '容纳人数',
    equipment   VARCHAR(255) NULL COMMENT '设备信息（如投影仪、白板）',
    buffer_time BIGINT DEFAULT 0 NULL COMMENT '缓冲时间（分钟）',
    status      CHAR DEFAULT '0' NOT NULL COMMENT '状态（0可用 1禁用）',
    order_num   BIGINT DEFAULT 0 NOT NULL COMMENT '显示顺序',
    create_by   VARCHAR(64) DEFAULT '' NULL COMMENT '创建者',
    create_time DATETIME NULL COMMENT '创建时间',
    update_by   VARCHAR(64) DEFAULT '' NULL COMMENT '更新者',
    update_time DATETIME NULL COMMENT '更新时间',
    remark      VARCHAR(500) NULL COMMENT '备注'
) ENGINE=InnoDB COMMENT '洽谈室表' COLLATE = utf8mb4_unicode_ci;

-- 创建索引以提高查询性能
CREATE INDEX idx_neg_room_status ON oa_neg_room (status);
CREATE INDEX idx_neg_room_order_num ON oa_neg_room (order_num);

-- ----------------------------
-- 茶水表
-- ----------------------------
DROP TABLE IF EXISTS oa_neg_tea;
CREATE TABLE oa_neg_tea (
    tea_id         BIGINT AUTO_INCREMENT COMMENT '茶水主键ID' PRIMARY KEY,
    tea_name       VARCHAR(50) NOT NULL COMMENT '茶水名称',
    category       VARCHAR(50) NULL COMMENT '分类（如茶类、咖啡、果汁）',
    stock_quantity BIGINT DEFAULT 0 NOT NULL COMMENT '库存数量',
    status         CHAR DEFAULT '0' NOT NULL COMMENT '状态（0可用 1缺货）',
    create_by      VARCHAR(64) DEFAULT '' NULL COMMENT '创建者',
    create_time    DATETIME NULL COMMENT '创建时间',
    update_by      VARCHAR(64) DEFAULT '' NULL COMMENT '更新者',
    update_time    DATETIME NULL COMMENT '更新时间',
    remark         VARCHAR(500) NULL COMMENT '备注'
) ENGINE=InnoDB COMMENT '茶水表' COLLATE = utf8mb4_unicode_ci;

-- 创建索引以提高查询性能
CREATE INDEX idx_neg_tea_category ON oa_neg_tea (category);
CREATE INDEX idx_neg_tea_status ON oa_neg_tea (status);

-- ----------------------------
-- 预约茶水关联表
-- ----------------------------
DROP TABLE IF EXISTS oa_neg_log_tea;
CREATE TABLE oa_neg_log_tea (
    log_tea_id  BIGINT AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
    log_id      BIGINT NOT NULL COMMENT '预约ID',
    tea_id      BIGINT NOT NULL COMMENT '茶水ID',
    quantity    BIGINT NOT NULL COMMENT '数量',
    create_by   VARCHAR(64) DEFAULT '' NULL COMMENT '创建者',
    create_time DATETIME NULL COMMENT '创建时间',
    update_by   VARCHAR(64) DEFAULT '' NULL COMMENT '更新者',
    update_time DATETIME NULL COMMENT '更新时间',
    remark      VARCHAR(500) NULL COMMENT '备注'
) ENGINE=InnoDB COMMENT '预约茶水关联表' COLLATE = utf8mb4_unicode_ci;

-- 创建索引以提高查询性能
CREATE INDEX idx_neg_log_tea_log_id ON oa_neg_log_tea (log_id);
CREATE INDEX idx_neg_log_tea_tea_id ON oa_neg_log_tea (tea_id);

-- ----------------------------
-- 预约冲突检测视图（可选）
-- ----------------------------
-- CREATE VIEW view_neg_conflict AS
-- SELECT 
--     r1.log_id as conflict_log_id,
--     r1.room_id,
--     r1.room_name,
--     r1.start_time,
--     r1.end_time,
--     r1.title as conflict_title,
--     r1.nick_name as conflict_user
-- FROM oa_neg_log r1
-- INNER JOIN oa_neg_log r2 
--     ON r1.room_id = r2.room_id 
--     AND r1.log_id != r2.log_id
--     AND r1.status IN ('0', '1') 
--     AND r2.status IN ('0', '1')
--     AND r1.start_time < r2.end_time 
--     AND r1.end_time > r2.start_time;