package com.leaf.xadmin.exception;

import com.leaf.xadmin.enums.ErrorStatus;

/**
 * @author leaf
 * <p>date: 2018-01-15 16:37</p>
 */
public class RepeatLoginException extends GlobalException {
    public RepeatLoginException(){}
    public RepeatLoginException(ErrorStatus status) {
        super(status);
    }
}
