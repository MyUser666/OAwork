DROP TABLE IF EXISTS oa_neg_log;
CREATE TABLE oa_neg_log (
  log_id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  log_room_name VARCHAR(255) NOT NULL COMMENT '预约房间（创建预约时的快照）',
  title VARCHAR(255) NOT NULL COMMENT '预约标题',
  log_nick_name VARCHAR(255) NOT NULL COMMENT '用户昵称',
  log_tea_name VARCHAR(255) COMMENT '预约茶水（创建预约时的快照）',
  log_tea_num BIGINT(20) COMMENT '预约茶水数量（创建预约时的快照）',
  client_name VARCHAR(255) NOT NULL COMMENT '当事人姓名',
  client_contact VARCHAR(255) NOT NULL COMMENT '当事人联系方式',
  case_reference VARCHAR(255) COMMENT '相关案号/案由',
  start_time DATETIME NOT NULL COMMENT '开始时间',
  end_time DATETIME NOT NULL COMMENT '结束时间',
  status CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态（0待确认 1已确认 2已签到 3已完成 4已取消）',
  create_by VARCHAR(64) NOT NULL COMMENT '创建人',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  update_by VARCHAR(64) NOT NULL COMMENT '更新人',
  update_time DATETIME NOT NULL COMMENT '更新时间',
  remark VARCHAR(500) COMMENT '备注',
  PRIMARY KEY (log_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='预约管理表';