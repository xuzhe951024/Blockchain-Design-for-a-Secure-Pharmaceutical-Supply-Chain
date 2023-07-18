package com.rbpsc.ctp.api.entities.dto.response;

import com.rbpsc.ctp.api.entities.base.BaseResponse;
import com.rbpsc.ctp.common.Constant.ServiceConstants;
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
