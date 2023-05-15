package com.rbpsc.ctp.api.controllor.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.ctp.api.entities.dto.OperationVO;
import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.ctp.biz.service.SupplyChainStepsService;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import com.rbpsc.ctp.configuration.v1prefix.V1RestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import static com.rbpsc.ctp.common.Constant.ServiceConstants.*;

@V1RestController
@RequestMapping(value = "/drugLifeCycle/drugOrderStep", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class DrugLiveCycleController {

    @Autowired
    SupplyChainStepsService supplyChainStepsService;

    @GetMapping(value = "/checkAvailable")
    public DrugLifeCycleResponse checkAvailable(){
        DrugLifeCycleResponse response = new DrugLifeCycleResponse();
        if (!API_ENABLED.get()){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_SERVICE_DISABLED);
            response.setDescribe("Service may be suffering from DDoS attack!");
            return response;
        }
        return response;
    }

    @GetMapping(value = "/toggle")
    public String toggle(){
        API_ENABLED.set(!API_ENABLED.get());
        return "API is now " + (API_ENABLED.get() ? "enabled" : "disabled") + ".";
    }

    @PostMapping("/manufacture")
    public DrugLifeCycleResponse nextStepManufacture(@RequestBody DrugLifeCycle drugLifeCycle) throws JsonProcessingException {

        DrugLifeCycleResponse response = checkBeforeServe(drugLifeCycle);

        if (!response.isSuccess()){
            return response;
        }

        drugLifeCycle.setDrug(supplyChainStepsService.manufacture(drugLifeCycle.getDrug()));

        log.info("Step:\n " + drugLifeCycle.pollOperationVOQ().toString() + "\n has been well processed!");

        return sendToNextStep(drugLifeCycle);
    }

    @PostMapping("/distributor")
    public DrugLifeCycleResponse nextStepDistributor(@RequestBody DrugLifeCycle drugLifeCycle) throws JsonProcessingException {

        DrugLifeCycleResponse response = checkBeforeServe(drugLifeCycle);

        if (!response.isSuccess()){
            return response;
        }

        if (!supplyChainStepsService.distributor(drugLifeCycle.getDrug())){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
            return response;
        }

        log.info("Step:\n " + drugLifeCycle.pollOperationVOQ().toString() + "\n has been well processed!");

        return sendToNextStep(drugLifeCycle);
    }

    @PostMapping("/consumer")
    public DrugLifeCycleResponse nextStepConsumer(@RequestBody DrugLifeCycle drugLifeCycle) throws JsonProcessingException {

        DrugLifeCycleResponse response = checkBeforeServe(drugLifeCycle);

        if (!response.isSuccess()){
            return response;
        }

        if (drugLifeCycle.getOperationQueueSize() != 0){
            log.error(String.format("Operation queue size is {%d} instead of 0, no permission for the current operation!", drugLifeCycle.getOperationQueueSize()));
            response.setResponseWithCode(RESPONSE_CODE_FAIL_OPERATION_TYPE_NOT_MATCH);
            return response;
        }

        if (!supplyChainStepsService.consumer(drugLifeCycle)){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
            return response;
        }

        log.info("Step:\n " + drugLifeCycle.pollOperationVOQ().toString() + "\n has been well processed!");

        return sendToNextStep(drugLifeCycle);
    }

    private DrugLifeCycleResponse checkBeforeServe(DrugLifeCycle drugLifeCycle) {
        DrugLifeCycleResponse response = new DrugLifeCycleResponse();

        if (!API_ENABLED.get()){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_SERVICE_DISABLED);
            response.setDescribe("Service may be suffering from DDoS attack!");
            return response;
        }

        // TODO: add role check for self
        OperationVO<RoleBase> operationVO = drugLifeCycle.peakOperationVOQ();
        if (!DrugOrderStep.class.getName().equals(operationVO.getOperationType())){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_OPERATION_TYPE_NOT_MATCH);
            response.setDescribe(String.format("check if operation type{%s} matches node role{%s}.",
                    operationVO.getOperationType(),
                    "TODO:Role"));

            return response;
        }

        return response;
    }

    private DrugLifeCycleResponse sendToNextStep(DrugLifeCycle drugLifeCycle) throws JsonProcessingException {
        DrugLifeCycleResponse response = new DrugLifeCycleResponse();

        OperationVO<RoleBase> operationVO = drugLifeCycle.peakOperationVOQ();
        if (StringUtils.isEmpty(operationVO.getOperation().getAddress())){
            log.error("Address can not be empty!");
            response.setResponseWithCode(RESPONSE_CODE_FAIL_FIND_ADDRESS);

            response.setDescribe(String.format("Please check address for operation:{%s}", operationVO));

            return response;
        }

        WebClientUtil webClientUtil = new WebClientUtil();

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add(DRUG_LIFECYCLE_REQUEST_PARAM_NAMES, new ObjectMapper().writeValueAsString(drugLifeCycle));

        Mono<String> responseMono = webClientUtil.postWithParams(operationVO.getOperation().getAddress(), param, String.class);

        responseMono.subscribe(log::info, error -> {
            throw new RuntimeException(error);
        });

        return response;
    }
}