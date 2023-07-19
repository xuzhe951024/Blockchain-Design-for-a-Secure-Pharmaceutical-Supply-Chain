package main.java.org.rbpsc.api.entities.dto.response;

import main.java.org.rbpsc.api.entities.base.BaseResponse;
import main.java.org.rbpsc.common.constant.ServiceConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DrugLifeCycleResponse extends BaseResponse<String> {
    public DrugLifeCycleResponse(){
        this.setResponseWithCode(ServiceConstants.RESPONSE_CODE_SUCCESS);
    }

    public DrugLifeCycleResponse(int code){
        this.setResponseWithCode(code);
    }
}
