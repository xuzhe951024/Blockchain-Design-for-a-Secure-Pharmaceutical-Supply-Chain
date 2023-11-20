package com.rbpsc.biz.service;

import org.rbpsc.api.entities.dto.DrugOperationDTO;
import org.rbpsc.api.entities.dto.response.DrugLifeCycleResponse;

public interface BlockChainRequestsHandler {
    DrugLifeCycleResponse sendToNextStep(DrugOperationDTO drugOperationDTO);
}
