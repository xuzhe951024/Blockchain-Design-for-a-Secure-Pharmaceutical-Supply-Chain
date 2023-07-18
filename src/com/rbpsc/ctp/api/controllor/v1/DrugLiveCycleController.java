package com.rbpsc.ctp.api.controllor.v1;


import lombok.extern.slf4j.Slf4j;
import com.rbpsc.ctp.api.entities.dto.DrugOperationDTO;
import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.biz.service.AttackStepsService;
import com.rbpsc.ctp.biz.service.SupplyChainStepsService;
import com.rbpsc.ctp.configuration.v1prefix.V1RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.rbpsc.ctp.common.Constant.ServiceConstants.*;


@V1RestController
@RequestMapping(value = "/drugLifeCycle/drugOrderStep", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class DrugLiveCycleController {

    //TODO read self roleName from ENV

    final
    SupplyChainStepsService supplyChainStepsService;

    final AttackStepsService attackStepsService;

    public DrugLiveCycleController(SupplyChainStepsService supplyChainStepsService, AttackStepsService attackStepsService) {
        this.supplyChainStepsService = supplyChainStepsService;
        this.attackStepsService = attackStepsService;
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

        if (!supplyChainStepsService.manufacture(drugOperationDTO.getDrug(), drugOperationDTO.getOperationDTO().getOperation())){
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

        if (!supplyChainStepsService.distributor(drugOperationDTO.getDrug(), drugOperationDTO.getOperationDTO().getOperation())){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
            response.setDescribe("Distributing failed!");
            return response;
        }

        log.info("Step:\n " + drugOperationDTO + "\n has been well processed!");

        return response;
    }

    @PostMapping("/consumer")
    public DrugLifeCycleResponse nextStepConsumer(@RequestBody DrugOperationDTO drugOperationDTO) {

        DrugLifeCycleResponse response = checkBeforeServe(drugOperationDTO);

        if (!response.isSuccess()){
            return response;
        }

        if (!supplyChainStepsService.consumer(drugOperationDTO.getDrug(), drugOperationDTO.getOperationDTO().getOperation(), drugOperationDTO.getExpectedReceiver())){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
            response.setDescribe("Consuming failed!");
            return response;
        }

        log.info("Step:\n " + drugOperationDTO + "\n has been well processed!");

        return response;
    }

    @PostMapping("/attack_confidentiality")
    public DrugLifeCycleResponse attackConfidentiality(@RequestBody DrugOperationDTO drugOperationDTO) {

        DrugLifeCycleResponse response = checkBeforeServe(drugOperationDTO);

        if (!response.isSuccess()){
            return response;
        }

        if (!attackStepsService.attackConfidentiality(drugOperationDTO.getDrug(), drugOperationDTO.getOperationDTO().getOperation())){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
            response.setDescribe("Attack failed!");
            return response;
        }

        log.info("Step:\n " + drugOperationDTO + "\n has been well processed!");

        return response;
    }

    @PostMapping("/attack_Integrity")
    public DrugLifeCycleResponse attackIntegrity(@RequestBody DrugOperationDTO drugOperationDTO) {

        DrugLifeCycleResponse response = checkBeforeServe(drugOperationDTO);

        if (!response.isSuccess()){
            return response;
        }

        if (!attackStepsService.attackIntegrity(drugOperationDTO.getDrug(), drugOperationDTO.getOperationDTO().getOperation())){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
            response.setDescribe("Attack failed!");
            return response;
        }

        log.info("Step:\n " + drugOperationDTO + "\n has been well processed!");

        return response;
    }

    @PostMapping("/attack_availability")
    public DrugLifeCycleResponse attackAvailability(@RequestBody DrugOperationDTO drugOperationDTO) {

//        DrugLifeCycleResponse response = checkBeforeServe(drugOperationDTO);
//
//        if (!response.isSuccess()){
//            return response;
//        }
//
//        if (!attackStepsService.attackAvailability(drugOperationDTO.getOperationDTO().getOperation())){
//            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
//            response.setDescribe("Attack failed!");
//            return response;
//        }
//
//        log.info("Step:\n " + drugOperationDTO + "\n has been well processed!");

        return toggle(false);
    }

    private DrugLifeCycleResponse checkBeforeServe(DrugOperationDTO drugOperationDTO) {
        DrugLifeCycleResponse response = new DrugLifeCycleResponse();

        if (!API_ENABLED.get()){
            response.setResponseWithCode(RESPONSE_CODE_FAIL_SERVICE_DISABLED);
            response.setDescribe("Service may be suffering from DDoS attack!");
            return response;
        }

//        TODO: use check here when deploy in docker containers
//        String selfRole = System.getenv(ROLE_NAME);
//        if (StringUtils.isEmpty(selfRole)) {
//            response.setResponseWithCode(RESPONSE_CODE_FAIL_BAD_GATEWAY);
//            response.setDescribe("SelfRole is empty!");
//            throw new NullPointerException("Null selfRole value!");
//        }
//
//        if (!selfRole.equals(drugOperationDTO.getOperationDTO().getOperation().getAddress().split("\\.")[0])){
//            response.setResponseWithCode(RESPONSE_CODE_FAIL_OPERATION_TYPE_NOT_MATCH);
//            response.setDescribe(String.format("check if operation add{%s} matches node role{%s}.",
//                    drugOperationDTO.getOperationDTO().getOperation().getAddress().split("\\.")[0],
//                    selfRole));
//
//            return response;
//        }

        return response;
    }
}
