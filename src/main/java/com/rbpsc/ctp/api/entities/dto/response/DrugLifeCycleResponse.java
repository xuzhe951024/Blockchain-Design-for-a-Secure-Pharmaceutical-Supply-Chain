package com.rbpsc.ctp.api.entities.dto.response;

import com.rbpsc.ctp.api.entities.base.BaseResponse;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.rbpsc.ctp.common.Constant.ServiceConstants.RESPONSE_CODE_MESSAGE_MAP;
import static com.rbpsc.ctp.common.Constant.ServiceConstants.RESPONSE_CODE_SUCCESS;

@Data
@EqualsAndHashCode(callSuper = true)
public class DrugLifeCycleResponse extends BaseResponse<String> {
    public DrugLifeCycleResponse(){
        this.setResponseWithCode(RESPONSE_CODE_SUCCESS);
    }

    public DrugLifeCycleResponse(int code){
        this.setResponseWithCode(code);
    }
}
