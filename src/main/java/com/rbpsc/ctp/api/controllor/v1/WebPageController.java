package com.rbpsc.ctp.api.controllor.v1;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.rbpsc.ctp.api.entities.dto.OperationVO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleView;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackModelBase;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.ctp.common.utiles.ParentToChildConvertor;
import com.rbpsc.ctp.common.utiles.TestDataGenerator;
import com.rbpsc.ctp.configuration.v1prefix.V1RestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/web")
@Slf4j
public class WebPageController {


    @GetMapping("/cards")
    public String getCards(Model model) throws JsonProcessingException {


        DrugLifeCycleView drugLifeCycleView = TestDataGenerator.generateDrugLifeCycleViewRandom();

        model.addAttribute("drugLifeCycleView", drugLifeCycleView);
        return "cardTemp";
    }

    @PostMapping("/submit")
    public String processCards(@ModelAttribute("drugLifeCycleView") DrugLifeCycleView drugLifeCycleView, HttpServletRequest request) {

        List<DrugLifeCycle> drugLifeCycleList = drugLifeCycleView.getDrugLifeCycleList();

        // 打印 cards 以便于调试
        for (DrugLifeCycle lifeCycle :
                drugLifeCycleList) {
            log.debug("Drug Info: \n" + lifeCycle.getDrug().toString());
            OperationVO operationVO = lifeCycle.pollOperationVOQ();
            if (DrugOrderStep.class.getName().equals(operationVO.getOperationType())){
                log.debug("Normal Step:\n" + ParentToChildConvertor.convert(operationVO, DrugOrderStep.class).toString());
            } else {
                log.debug("Attack:\n" + ParentToChildConvertor.convert(operationVO, AttackModelBase.class));
            }
        }

        // 在这里执行其他操作，例如保存更新后的数据到数据库
        return "redirect:/cards";
    }

}
