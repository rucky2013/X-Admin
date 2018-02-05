package com.leaf.xadmin.exception;

import com.leaf.xadmin.enums.ErrorStatus;

/**
 * @author leaf
 * <p>date: 2018-01-15 20:54</p>
 */
public class TokenExpiredException extends GlobalException {
    public TokenExpiredException() {}
    public TokenExpiredException(ErrorStatus status) {
        super(status);
    }
}
