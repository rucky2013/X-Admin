package com.leaf.xadmin.utils.response;

import com.leaf.xadmin.enums.ResponseStatus;
import com.leaf.xadmin.vo.ErrorTemplateVO;
import com.leaf.xadmin.vo.ResponseResultVO;
import com.leaf.xadmin.enums.ErrorStatus;

/**
 * @author leaf
 * <p>date: 2018-01-13 21:35</p>
 */
public class ResponseResultUtil {

    public static ResponseResultVO success(Object data) {
        ResponseResultVO result = new ResponseResultVO();
        result.setStatus(ResponseStatus.SUCCESS.getStatus());
        result.setMessage(ResponseStatus.SUCCESS.getMessage());
        result.setResult(data);
        return result;
    }

    public static ResponseResultVO fail(ErrorTemplateVO template) {
        ResponseResultVO result = new ResponseResultVO();
        result.setStatus(ResponseStatus.FAIL.getStatus());
        result.setMessage(ResponseStatus.FAIL.getMessage());
        result.setResult(template);
        return result;
    }

    public static ResponseResultVO fail(ErrorStatus status, String exception, String detail, String path) {
        ErrorTemplateVO template = ErrorTemplateVO.builder()
                .error(status.getError())
                .code(status.getCode())
                .message(status.getMessage())
                .exception(exception)
                .detail(detail)
                .path(path)
                .build();
        return fail(template);
    }

    public static ResponseResultVO fail(ErrorStatus status, String exception, String detail) {
        return fail(status, exception, detail, null);
    }

    public static ResponseResultVO fail(ErrorStatus status, String exception) {
        return fail(status, exception, null, null);
    }

}