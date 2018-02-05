package com.leaf.xadmin.exception;

import com.leaf.xadmin.enums.ErrorStatus;

/**
 * @author leaf
 * <p>date: 2018-01-28 14:42</p>
 * <p>version: 1.0</p>
 */
public class ForceLogoutException extends GlobalException {
    public ForceLogoutException() {}
    public ForceLogoutException(ErrorStatus status) {
        super(status);
    }
}
