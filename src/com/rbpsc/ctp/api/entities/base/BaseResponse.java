package com.rbpsc.ctp.api.entities.base;

import com.rbpsc.ctp.common.Constant.ServiceConstants;
import lombok.Data;


@Data
public class BaseResponse<T> {
    private int responseCode;
    private String message;
    private String describe;
    private T responseBody;

    public boolean isSuccess(){
        return this.responseCode == ServiceConstants.RESPONSE_CODE_SUCCESS;
    }

    public void setResponseWithCode(int code){
        this.responseCode = code;
        this.message = ServiceConstants.RESPONSE_CODE_MESSAGE_MAP.get(code);
    }
}
