package com.leaf.xadmin.exception;

import com.leaf.xadmin.enums.ErrorStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author leaf
 * <p>date: 2018-01-02 21:54</p>
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    @Setter @Getter private ErrorStatus status;

    public GlobalException() {}

    public GlobalException(ErrorStatus status){
        super(status.getMessage());
        this.status = status;
    }

}
