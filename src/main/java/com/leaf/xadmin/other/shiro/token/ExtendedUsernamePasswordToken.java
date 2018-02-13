package com.leaf.xadmin.other.shiro.token;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author leaf
 * <p>date: 2018-02-06 22:35</p>
 * <p>version: 1.0</p>
 */
@Data
public class ExtendedUsernamePasswordToken extends UsernamePasswordToken {
    private String loginType;

    public ExtendedUsernamePasswordToken(final String username, final String password, String loginType) {
        super(username, password);
        this.loginType = loginType;
    }
}
