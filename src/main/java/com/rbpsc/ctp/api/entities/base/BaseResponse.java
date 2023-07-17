package main.java.com.rbpsc.ctp.api.entities.base;

import lombok.Data;

import static main.java.com.rbpsc.ctp.common.Constant.ServiceConstants.RESPONSE_CODE_MESSAGE_MAP;
import static main.java.com.rbpsc.ctp.common.Constant.ServiceConstants.RESPONSE_CODE_SUCCESS;


@Data
public class BaseResponse<T> {
    private int responseCode;
    private String message;
    private String describe;
    private T responseBody;

    public boolean isSuccess(){
        return this.responseCode == RESPONSE_CODE_SUCCESS;
    }

    public void setResponseWithCode(int code){
        this.responseCode = code;
        this.message = RESPONSE_CODE_MESSAGE_MAP.get(code);
    }
}
