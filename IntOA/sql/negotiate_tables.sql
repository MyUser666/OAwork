-- 洽谈室预约管理系统表结构
-- 厺除不必要的外键约束，提高性能和灵活性

-- ----------------------------
-- 预约表
-- ----------------------------
DROP TABLE IF EXISTS oa_neg_log;
CREATE TABLE oa_neg_log (
                            log_id         bigint auto_increment comment '主键ID'
                                primary key,
                            log_room_name  varchar(50)             not null comment '预约房间（创建预约时的快照）',
                            title          varchar(255)            not null comment '预约标题',
                            log_nick_name  varchar(50)             not null comment '用户昵称',
                            log_tea_name   varchar(50)             null comment '预约茶水（创建预约时的快照）',
                            log_tea_num    bigint                  null comment '预约茶水数量（创建预约时的快照）',
                            client_name    varchar(100)            null comment '当事人姓名',
                            client_contact varchar(50)             null comment '当事人联系方式',
                            case_reference varchar(255)            null comment '相关案号/案由',
                            start_time     datetime                not null comment '开始时间',
                            end_time       datetime                not null comment '结束时间',
                            status         char        default '0' not null comment '状态（0待确认 1已确认 2已签到 3已完成 4已取消）',
                            create_by      varchar(64) default ''  null comment '创建者',
                            create_time    datetime                null comment '创建时间',
                            update_by      varchar(64) default ''  null comment '更新者',
                            update_time    datetime                null comment '更新时间',
                            remark         varchar(500)            null comment '备注'
) ENGINE=InnoDB COMMENT '预约表' COLLATE = utf8mb4_unicode_ci;

-- 创建索引以提高查询性能和冲突检测效率
create index idx_neg_log_end_time
    on oa_neg_log (end_time);

create index idx_neg_log_start_time
    on oa_neg_log (start_time);

create index idx_neg_log_status
    on oa_neg_log (status);

create index idx_neg_log_user_time
    on oa_neg_log (start_time, end_time);

create index oa_neg_log_log_room_name_index
    on oa_neg_log (log_room_name)
    comment '预约房间索引';

-- ----------------------------
-- 洽谈室表
-- ----------------------------
DROP TABLE IF EXISTS oa_neg_room;
CREATE TABLE oa_neg_room (
                             room_id     bigint auto_increment comment '洽谈室主键ID'
                                 primary key,
                             room_name   varchar(50)                    not null comment '房间名称',
                             location    varchar(100)                   null comment '位置',
                             capacity    bigint                         null comment '容纳人数',
                             equipment   varchar(255)                   null comment '设备信息（如投影仪、白板）',
                             buffer_time time        default '00:00:00' null comment '缓冲时间（分钟）',
                             status      char        default '0'        not null comment '状态（0可用 1禁用）',
                             order_num   bigint      default 0          null comment '显示顺序',
                             create_by   varchar(64) default ''         null comment '创建者',
                             create_time datetime                       null comment '创建时间',
                             update_by   varchar(64) default ''         null comment '更新者',
                             update_time datetime                       null comment '更新时间',
                             remark      varchar(500)                   null comment '备注'
) ENGINE=InnoDB COMMENT '洽谈室表' COLLATE = utf8mb4_unicode_ci;

-- 创建索引以提高查询性能
create index idx_neg_room_status
    on oa_neg_room (status);


-- ----------------------------
-- 茶水表
-- ----------------------------
DROP TABLE IF EXISTS oa_neg_tea;
CREATE TABLE oa_neg_tea (
                            tea_id         bigint auto_increment comment '茶水主键ID'
                                primary key,
                            tea_name       varchar(50)             not null comment '茶水名称',
                            category       varchar(50)             null comment '分类（如茶类、咖啡、果汁）',
                            stock_quantity bigint      default 0   not null comment '库存数量',
                            status         char        default '0' not null comment '状态（0可用 1缺货）',
                            create_by      varchar(64) default ''  null comment '创建者',
                            create_time    datetime                null comment '创建时间',
                            update_by      varchar(64) default ''  null comment '更新者',
                            update_time    datetime                null comment '更新时间',
                            remark         varchar(500)            null comment '备注'
) ENGINE=InnoDB COMMENT '茶水表' COLLATE = utf8mb4_unicode_ci;

-- 创建索引以提高查询性能
create index idx_neg_tea_name
    on oa_neg_tea (tea_name)
    comment '茶水名称索引';
CREATE INDEX idx_neg_tea_status ON oa_neg_tea (status);

-- ----------------------------
-- 预约茶水关联表
-- ----------------------------
-- DROP TABLE IF EXISTS oa_neg_log_tea;
-- CREATE TABLE oa_neg_log_tea (
--     log_tea_id  BIGINT AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
--     log_id      BIGINT NOT NULL COMMENT '预约ID',
--     tea_id      BIGINT NOT NULL COMMENT '茶水ID',
--     quantity    BIGINT NOT NULL COMMENT '数量',
--     create_by   VARCHAR(64) DEFAULT '' NULL COMMENT '创建者',
--     create_time DATETIME NULL COMMENT '创建时间',
--     update_by   VARCHAR(64) DEFAULT '' NULL COMMENT '更新者',
--     update_time DATETIME NULL COMMENT '更新时间',
--     remark      VARCHAR(500) NULL COMMENT '备注'
-- ) ENGINE=InnoDB COMMENT '预约茶水关联表' COLLATE = utf8mb4_unicode_ci;
--
-- -- 创建索引以提高查询性能
-- CREATE INDEX idx_neg_log_tea_log_id ON oa_neg_log_tea (log_id);
-- CREATE INDEX idx_neg_log_tea_tea_id ON oa_neg_log_tea (tea_id);

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