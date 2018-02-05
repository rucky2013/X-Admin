package com.leaf.xadmin.exception;

import com.leaf.xadmin.enums.ErrorStatus;

/**
 * @author leaf
 * <p>date: 2018-01-21 16:20</p>
 * <p>version: 1.0</p>
 */
public class AccountLockException extends GlobalException {
    public AccountLockException() {}
    public AccountLockException(ErrorStatus status) {
        super(status);
    }
}
