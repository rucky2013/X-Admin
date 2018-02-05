package com.leaf.xadmin.exception;

import com.leaf.xadmin.enums.ErrorStatus;

/**
 *
 * @author leaf
 * <p>date: 2018-01-18 13:27</p>
 * <p>version: 1.0</p>
 */
public class TokenInvalidException extends GlobalException {
    public TokenInvalidException() {}
    public TokenInvalidException(ErrorStatus status) {
        super(status);
    }
}
