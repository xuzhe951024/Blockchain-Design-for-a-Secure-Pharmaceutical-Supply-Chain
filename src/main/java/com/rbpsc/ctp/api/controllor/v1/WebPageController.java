package com.rbpsc.ctp.api.controllor.v1;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.rbpsc.ctp.api.entities.dto.OperationDTO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackModelBase;
import com.rbpsc.ctp.common.utiles.ParentToChildConvertor;
import com.rbpsc.ctp.common.utiles.TestDataGenerator;
import com.rbpsc.ctp.configuration.v1prefix.V1RestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@V1RestController
@RequestMapping("/web")
@Slf4j
public class WebPageController {


    @GetMapping("/cards")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<DrugLifeCycleVO> getCards() throws JsonProcessingException {


        List<DrugLifeCycle> drugLifeCycleList = TestDataGenerator.generateDrugLifeCycleViewRandom();

        return TestDataGenerator.generateDrugLifeCycleVO(drugLifeCycleList);
    }

    @PostMapping("/submit")
    @CrossOrigin(origins = "http://localhost:3000")
    public String processCards(@ModelAttribute("drugLifeCycleView") DrugLifeCycleVO drugLifeCycleVO, HttpServletRequest request) {

//        List<DrugLifeCycle> drugLifeCycleList = drugLifeCycleVO.getDrugLifeCycleList();
//
//        // 打印 cards 以便于调试
//        for (DrugLifeCycle lifeCycle :
//                drugLifeCycleList) {
//            log.debug("Drug Info: \n" + lifeCycle.getDrug().toString());
//            OperationDTO operationDTO = lifeCycle.pollOperationVOQ();
//            if (DrugOrderStep.class.getName().equals(operationDTO.getOperationType())){
//                log.debug("Normal Step:\n" + ParentToChildConvertor.convert(operationDTO, DrugOrderStep.class).toString());
//            } else {
//                log.debug("Attack:\n" + ParentToChildConvertor.convert(operationDTO, AttackModelBase.class));
//            }
//        }

        // 在这里执行其他操作，例如保存更新后的数据到数据库
        return "redirect:/cards";
    }

}
