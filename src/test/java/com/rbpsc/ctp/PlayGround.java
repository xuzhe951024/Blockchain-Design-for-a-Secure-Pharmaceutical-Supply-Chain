package com.rbpsc.ctp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.ctp.api.entities.base.BaseEntity;
import com.rbpsc.ctp.api.entities.base.BaseResponse;
import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;

import static com.rbpsc.ctp.common.Constant.ServiceConstants.RESPONSE_CODE_FAIL_SERVICE_DISABLED;

@Slf4j
public class PlayGround {
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        Queue<String> queue = new ArrayDeque<>(Arrays.asList("a", "b", "c"));
        System.out.printf("queue poll = %s%n", queue.poll());
        queue.remove();
        System.out.printf("queue = %s%n", queue);

        System.out.println("queue class name: " + queue.getClass().getName());

        Consumer consumer = new Consumer();
        BaseEntity<String> entity = (BaseEntity<String>) consumer;
        System.out.println("entity class:" + entity.getClass().getName());

        AttackAvailability attackAvailability = new AttackAvailability();
        System.out.println(attackAvailability);

        SupplyChainBaseEntity supplyChainBaseEntity = new SupplyChainBaseEntity();
        System.out.println(supplyChainBaseEntity);

        DrugLifeCycleResponse drugLifeCycleResponse = new DrugLifeCycleResponse();
        System.out.println(drugLifeCycleResponse);

        attackAvailability.setRoleName("1");
        attackAvailability.setTargetDrugId("2");

        System.out.println(new ObjectMapper().writeValueAsString((RoleBase)attackAvailability));
    }
}
