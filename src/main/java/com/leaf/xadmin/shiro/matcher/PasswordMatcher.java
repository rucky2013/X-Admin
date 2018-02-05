package com.leaf.xadmin.shiro.matcher;

import com.leaf.xadmin.utils.encrypt.PassEncryptUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义密码验证工具
 *
 * @author leaf
 *
 */
@Component
public class PasswordMatcher extends SimpleCredentialsMatcher {

	@Autowired
	private PassEncryptUtil encryptUtil;

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

		// 获取当前登录用户密码
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String password = String.valueOf(usernamePasswordToken.getPassword());

		// 将用户密码转换为MD5
		Object tokenCredentials = encryptUtil.encryptPass(password);
		// 对数据库密码字段进行解密操作
		Object accountCredentials = getCredentials(info).toString();

		// 比较MD5中间值是否相同
		return equals(tokenCredentials, accountCredentials);
	}
}
