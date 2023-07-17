package main.java.com.rbpsc.ctp.api.entities.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import main.java.com.rbpsc.ctp.api.entities.base.BaseResponse;

import static main.java.com.rbpsc.ctp.common.Constant.ServiceConstants.RESPONSE_CODE_SUCCESS;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DrugLifeCycleResponse extends BaseResponse<String> {
    public DrugLifeCycleResponse(){
        this.setResponseWithCode(RESPONSE_CODE_SUCCESS);
    }

    public DrugLifeCycleResponse(int code){
        this.setResponseWithCode(code);
    }
}
