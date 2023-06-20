package com.rbpsc.ctp.api.controllor.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.ctp.api.entities.dto.DrugOperationDTO;
import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.biz.service.SupplyChainStepsService;
import com.rbpsc.ctp.configuration.v1prefix.V1RestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.rbpsc.ctp.common.Constant.EntityConstants.ROLE_NAME;
import static com.rbpsc.ctp.common.Constant.ServiceConstants.*;

@V1RestController
@RequestMapping(value = "/drugLifeCycle/drugOrderStep", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class DrugLiveCycleController {

    //TODO read self roleName from ENV

    final
    SupplyChainStepsService supplyChainStepsService;

    final
    ObjectMapper objectMapper;

    public DrugLiveCycleController(SupplyChainStepsService supplyChainStepsService, ObjectMapper objectMapper) {
        this.supplyChainStepsService = supplyChainStepsService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(value = "/checkAvailable")
    public DrugLifeCycleResponse checkAvailable(){
        DrugLifeCycleResponse response = new DrugLifeCycleResponse();

        log.info("Checking: API is now " + (API_ENABLED.get() ? "enabled" : "disabled") + ".");

        if (!API_ENABLED.get()){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_SERVICE_DISABLED);
            response.setDescribe("Service may be suffering from DDoS attack!");
            return response;
        }

        return response;
    }

    @PostMapping(value = "/toggle")
    public DrugLifeCycleResponse toggle(@RequestBody boolean enable){
        //TODO control each service API here independently

        DrugLifeCycleResponse response = new DrugLifeCycleResponse();
        API_ENABLED.set(enable);
        response.setDescribe("API is now " + (API_ENABLED.get() ? "enabled" : "disabled") + ".");
        log.info(response.getDescribe());
        return response;
    }

    @PostMapping("/manufacture")
    public DrugLifeCycleResponse nextStepManufacture(@RequestBody DrugOperationDTO drugOperationDTO){

        DrugLifeCycleResponse response = checkBeforeServe(drugOperationDTO);

        if (!response.isSuccess()){
            return response;
        }

        DrugOrderStep drugOrderStep =  (DrugOrderStep) drugOperationDTO.getOperationDTO().getOperation();
        if (!supplyChainStepsService.manufacture(drugOperationDTO.getDrug(), drugOrderStep)){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
            response.setDescribe("Manufacturing failed!");
            return response;
        }

        log.info("Step:\n " + drugOperationDTO + "\n has been well processed!");

        return response;
    }

    @PostMapping("/distributor")
    public DrugLifeCycleResponse nextStepDistributor(@RequestBody DrugOperationDTO drugOperationDTO){

        DrugLifeCycleResponse response = checkBeforeServe(drugOperationDTO);

        if (!response.isSuccess()){
            return response;
        }

        DrugOrderStep drugOrderStep =  (DrugOrderStep) drugOperationDTO.getOperationDTO().getOperation();
        if (!supplyChainStepsService.manufacture(drugOperationDTO.getDrug(), drugOrderStep)){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
            response.setDescribe("Manufacturing failed!");
            return response;
        }

        log.info("Step:\n " + drugOperationDTO + "\n has been well processed!");

        return response;
    }

    @PostMapping("/consumer")
    public DrugLifeCycleResponse nextStepConsumer(@RequestBody DrugLifeCycle drugLifeCycle) throws JsonProcessingException {

//        DrugLifeCycleResponse response = checkBeforeServe(drugLifeCycle);
//
//        if (!response.isSuccess()){
//            return response;
//        }
//
//        if (drugLifeCycle.getOperationQueueSize() != 0){
//            log.error(String.format("Operation queue size is {%d} instead of 0, no permission for the current operation!", drugLifeCycle.getOperationQueueSize()));
//            response.setResponseWithCode(RESPONSE_CODE_FAIL_OPERATION_TYPE_NOT_MATCH);
//            return response;
//        }
//
//        if (!supplyChainStepsService.consumer(drugLifeCycle)){
//            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
//            return response;
//        }
//
//        log.info("Step:\n " + drugLifeCycle.pollOperationVOQ().toString() + "\n has been well processed!");

//        return sendToNextStep(drugLifeCycle);
        return null;
    }

    private DrugLifeCycleResponse checkBeforeServe(DrugOperationDTO drugOperationDTO) {
        DrugLifeCycleResponse response = new DrugLifeCycleResponse();

        if (!API_ENABLED.get()){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_SERVICE_DISABLED);
            response.setDescribe("Service may be suffering from DDoS attack!");
            return response;
        }

        String selfRole = System.getenv(ROLE_NAME);
        if (StringUtils.isEmpty(selfRole)) {
            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
            response.setDescribe("SelfRole is empty!");
            throw new NullPointerException("Null selfRole value!");
        }

        if (!DrugOrderStep.class.getName().equals(drugOperationDTO.getOperationDTO().getOperationType())){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_OPERATION_TYPE_NOT_MATCH);
            response.setDescribe(String.format("check if operation type{%s} matches node role{%s}.",
                    drugOperationDTO.getOperationDTO().getOperationType(),
                    DrugOrderStep.class.getName()));

            return response;
        }

        if (!selfRole.equals(drugOperationDTO.getOperationDTO().getOperation().getAddress())){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_OPERATION_TYPE_NOT_MATCH);
            response.setDescribe(String.format("check if operation add{%s} matches node role{%s}.",
                    drugOperationDTO.getOperationDTO().getOperation().getAddress().split(":")[0],
                    selfRole));

            return response;
        }

        return response;
    }
}
