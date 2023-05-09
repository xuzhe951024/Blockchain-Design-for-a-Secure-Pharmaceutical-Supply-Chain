package com.rbpsc.ctp.api.controllor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.ctp.api.entities.configs.ExperimentConfig;
import com.rbpsc.ctp.api.entities.dto.OperationVO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleView;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.factories.FunctionEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackModelBase;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.ctp.common.utiles.ParentToChildConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
//@RequestMapping()
@Slf4j
public class WebPageController {


    @GetMapping("/cards")
    public String getCards(Model model) {
        ExperimentConfig experimentConfig = new ExperimentConfig();
        experimentConfig.setExperimentName("TestPage");
        experimentConfig.setConsumerCount(3);
        experimentConfig.setDoesForEachConsumer(1);
        experimentConfig.setDrugName("Covid-Vaccine");
        experimentConfig.setDistributorsForEachLevel(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
        }});

        List<Consumer> consumerList = new ArrayList<Consumer>(){{
            for (int i = 0; i < experimentConfig.getConsumerCount(); i++) {
                Consumer consumer = DataEntityFactory.createConsumer(experimentConfig.getDoesForEachConsumer(), "127.0.0.1");
                add(consumer);
            }
        }};

        List<List<Institution>> institutionTree = new ArrayList<List<Institution>>(){{
            for (int institudNum :
                    experimentConfig.getDistributorsForEachLevel()) {
                List<Institution> institutionList = new ArrayList<Institution>(){{
                    for (int i = 0; i < institudNum; i++) {
                        Institution institution = DataEntityFactory.createInstitution("127.0.0.1");
                        add(institution);
                    }
                }};

                add(institutionList);
            }
        }};

        DrugLifeCycleView drugLifeCycleView = FunctionEntityFactory.createDrugLifeCycleView(experimentConfig, consumerList, institutionTree);

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
            OperationVO<RoleBase> operationVO = lifeCycle.pollOperationVOQ();
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
