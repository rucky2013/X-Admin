package com.leaf.xadmin.exception;

import com.leaf.xadmin.enums.ErrorStatus;

/**
 * @author leaf
 * <p>date: 2018-01-18 20:28</p>
 * <p>version: 1.0</p>
 */
public class TokenDecodeException extends GlobalException {
    public TokenDecodeException() {}
    public TokenDecodeException(ErrorStatus status) {
        super(status);
    }
}
