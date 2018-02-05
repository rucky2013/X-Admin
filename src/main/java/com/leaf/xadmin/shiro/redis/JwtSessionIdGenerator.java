package com.leaf.xadmin.shiro.redis;

import com.leaf.xadmin.utils.generater.SnowflakeUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

/**
 * @author leaf
 * <p>date: 2018-01-20 19:42</p>
 * <p>version: 1.0</p>
 */
public class JwtSessionIdGenerator implements SessionIdGenerator {
    private SnowflakeUtil util = new SnowflakeUtil(0, 1);

    @Override
    public Serializable generateId(Session session) {
        if (session.getId() == null) {
            return util.nextId();
        }
        return session.getId();
    }
}
