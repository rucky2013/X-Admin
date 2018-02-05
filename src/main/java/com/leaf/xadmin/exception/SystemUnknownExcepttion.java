package com.leaf.xadmin.exception;

import com.leaf.xadmin.enums.ErrorStatus;

/**
 * @author leaf
 * <p>date: 2018-01-20 21:14</p>
 * <p>version: 1.0</p>
 */
public class SystemUnknownExcepttion extends GlobalException {
    public SystemUnknownExcepttion() {}
    public SystemUnknownExcepttion(ErrorStatus status) {
        super(status);
    }
}
