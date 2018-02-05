package com.leaf.xadmin.constants;

/**
 * @author leaf
 * <p>date: 2018-01-18 19:58</p>
 * <p>version: 1.0</p>
 */
public class GlobalConstants {
    /**
     * jwt配置
     */
    public final static String JWT_TOKEN_NAME = "token";
    public final static int JWT_TOKEN_TIMEOUT = 30;
    public final static String JWT_SECRET = "leaf";

    /**
     * redis配置
     */
    public final static String REDIS_SESSION_KEY_PREFIX = "session:";
    public final static String REDIS_RETURN_SUCCESS = "success";


    /**
     * session配置
     */
    public final static String SESSION_FORCE_LOGOUT_KEY = "SESSION_FORCE_LOGOUT_KEY";
    public final static String SESSION_FLUSH_AUTHOR_KEY = "SESSION_FLUSH_AUTHOR_KEY";
}
