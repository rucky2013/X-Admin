-- 数据库创建
DROP DATABASE IF EXISTS house_rent;
CREATE DATABASE house_rent DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;;

-- 选择数据库
USE house_rent;

-- 用户表
DROP TABLE user;
CREATE TABLE user (
  `id` VARCHAR(32) PRIMARY KEY,
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
  `name` VARCHAR(16) NOT NULL COMMENT '用户名',
  `email` VARCHAR(32) COMMENT '邮箱',
  `pass` VARCHAR(16) NOT NULL COMMENT '密码',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(默认1代表正常)',
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型(默认1代表普通用户)',
  `last_login_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '最后登录时间',
  `last_login_ip` INTEGER UNSIGNED COMMENT '最后登录ip',
  `last_login_local` VARCHAR(32) COMMENT '最后登录地点',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  UNIQUE INDEX uniq_phone(phone),
  UNIQUE INDEX uniq_name(name),
  UNIQUE INDEX uniq_email(email)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='用户表';

-- 账户信息表
DROP TABLE account;
CREATE TABLE account(
  `id` VARCHAR(32) PRIMARY KEY,
  `nickname` VARCHAR(16) NOT NULL COMMENT '昵称/用户名',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
  `email` VARCHAR(32) COMMENT '邮箱',
  `avatar` VARCHAR(32) COMMENT '头像',
  `age` INTEGER DEFAULT 18 COMMENT '年龄',
  `sex` TINYINT DEFAULT 1 COMMENT '性别',
  `desc` VARCHAR(64) COMMENT '描述',
  `qq` VARCHAR(64) COMMENT 'qq号',
  `wechat` VARCHAR(64) COMMENT '微信号',
  `realname` VARCHAR(16) COMMENT '真实姓名',
  `identity` VARCHAR(18) COMMENT '身份证号',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  UNIQUE INDEX uniq_nickname(nickname),
  UNIQUE INDEX uniq_phone(phone),
  UNIQUE INDEX uniq_email(email),
  UNIQUE INDEX uniq_qq(qq),
  UNIQUE INDEX uniq_wechat(wechat),
  UNIQUE INDEX uniq_identity(identity)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='账户信息表';

-- 角色表
DROP TABLE role;
CREATE TABLE role (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(16) NOT NULL COMMENT '角色名',
  `desc` VARCHAR(32) COMMENT '角色描述',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  UNIQUE INDEX uniq_name(name)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='角色表';

-- 用户角色表(关联表)
DROP TABLE user_role;
CREATE TABLE user_role (
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户id',
  `role_id` BIGINT NOT NULL COMMENT '角色id',
  PRIMARY KEY (user_id, role_id)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='用户角色表';

-- 权限表
DROP TABLE permission;
CREATE TABLE permission (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(16) NOT NULL COMMENT '权限名',
  `desc` VARCHAR(32) COMMENT '权限描述',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  UNIQUE INDEX uniq_name(name)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='权限表';

-- 角色权限表(关联表)
DROP TABLE role_permission;
CREATE TABLE role_permission (
  `role_id` BIGINT NOT NULL COMMENT '角色id',
  `permission_id` BIGINT NOT NULL COMMENT '权限id',
  PRIMARY KEY (role_id, permission_id)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='角色权限表';

-- 管理员表
DROP TABLE admin;
CREATE TABLE admin (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(16) NOT NULL COMMENT '管理员名',
  `pass` VARCHAR(16) NOT NULL COMMENT '密码',
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  `last_login_time` TIMESTAMP NOT NULL COMMENT '最后登录时间',
  `last_login_ip` INTEGER UNSIGNED COMMENT '最后登录ip',
  `last_login_local` VARCHAR(32) COMMENT '最后登录地点',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  UNIQUE INDEX uniq_name(name)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='管理员表';

-- 公告表
DROP TABLE notice;
CREATE TABLE notice (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(16) NOT NULL COMMENT '名称',
  `desc` VARCHAR(128) NOT NULL COMMENT '描述',
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  `ip` INTEGER UNSIGNED COMMENT 'ip地址',
  `admin_id` BIGINT NOT NULL COMMENT '管理员id',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间'
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='公告表';

-- 系统日志表
DROP TABLE sys_log;
CREATE TABLE sys_log (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `local` VARCHAR(32) COMMENT '位置',
  `desc` VARCHAR(128) COMMENT '描述',
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间'
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='系统日志表(记录系统运行日志)';

-- 管理员日志表
DROP TABLE admin_log;
CREATE TABLE admin_log (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `local` VARCHAR(32) COMMENT '位置',
  `desc` VARCHAR(128) COMMENT '描述',
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  `ip` INTEGER UNSIGNED COMMENT 'ip地址',
  `admin_id` BIGINT NOT NULL COMMENT '管理员id',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间'
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='管理员日志表(记录管理员日常操作)';

-- 用户日志表
DROP TABLE user_log;
CREATE TABLE user_log (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `local` VARCHAR(32) COMMENT '位置',
  `desc` VARCHAR(128) COMMENT '描述',
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  `ip` INTEGER UNSIGNED COMMENT 'ip地址',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户id',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间'
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='用户日志表(记录用户日常操作)';

-- 资源表
DROP TABLE resource;
CREATE TABLE resource (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(32) COMMENT '资源名',
  `desc` VARCHAR(64) COMMENT '资源描述',
  `path` VARCHAR(64) COMMENT '请求资源路径',
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '资源类型',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  UNIQUE INDEX uniq_path(path)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='资源表';

-- 资源权限表
DROP TABLE resource_perm;
CREATE TABLE resource_perm (
  `resource_id` BIGINT NOT NULL COMMENT '资源id',
  `permission_id` BIGINT NOT NULL COMMENT '权限id',
  PRIMARY KEY (resource_id, permission_id)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='资源权限表';

-- 资源角色表
DROP TABLE resource_role;
CREATE TABLE resource_role (
  `resource_id` BIGINT NOT NULL COMMENT '资源id',
  `role_id` BIGINT NOT NULL COMMENT '角色id',
  PRIMARY KEY (resource_id, role_id)
) ENGINE=innodb, CHARSET=utf8mb4, COMMENT='资源角色表';