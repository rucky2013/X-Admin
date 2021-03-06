-- 选中数据库
use house_rent;

-- user测试数据
INSERT INTO
  user(id, phone, name, email, pass, type, status, last_login_ip, last_login_local)
VALUES
  ('U_410093246937563136', '15538306625', 'leaf', '806569552@qq.com', 'ecc8066511c4df6c', 1, 1, INET_ATON('255.255.255.255'), '郑州市金水区文化路97号'),
  ('U_410093692271984640', '15538306624', 'xbc', '3273104264@qq.com', 'd45465d3e25dc8b6', 1, 1, INET_ATON('255.255.255.255'), '郑州市金水区文化路97号');

-- account测试数据
INSERT INTO
  account(id, nickname, phone, email)
VALUES
  ('U_410093246937563136', 'leaf', '15538306625', '806569552@qq.com'),
  ('U_410093692271984640', 'xbc', '15538306624', '3273104264@qq.com');

-- role测试数据
INSERT INTO
  role(id, name, `desc`)
VALUES
  (1, 'new_user', '新用户'),
  (2, 'common_user', '普通用户');

-- user_role测试数据
INSERT INTO user_admin_role
VALUES
  ('U_410093246937563136', 2),
  ('U_410093692271984640', 1);

-- permission测试数据
INSERT INTO
  permission(id, name, `desc`)
VALUES
  (1, 'anon', '匿名访问权限'),
  (2, 'login', '登陆访问权限');

-- role_permission测试数据
INSERT INTO role_permission
VALUES
  (1, 1),
  (2, 1),
  (2, 2);

-- resource测试数据
INSERT INTO
  resource(id, name, `desc`, path, type)
VALUES
  (1, 'login', '用户登录', '/user/login', 1),
  (2, 'getUser', '获取指定id用户信息', '/user/getUser/{id}', 2),
  (3, 'logout', '用户退出', '/user/logout', 1);

-- resource_perm测试数据
INSERT INTO
  resource_perm
VALUES
  (1, 1),
  (2, 2),
  (3, 2);

-- admin测试数据
INSERT INTO
  admin(id, name, pass, phone, email, type, status, last_login_ip, last_login_local)
VALUES ('A_410962076853338112', 'admin', '2942ac7f99804ca7', '15538306625', '806569552@qq.com', 1, 1, INET_ATON('255.255.255.255'), '郑州市金水区文化路97号');
