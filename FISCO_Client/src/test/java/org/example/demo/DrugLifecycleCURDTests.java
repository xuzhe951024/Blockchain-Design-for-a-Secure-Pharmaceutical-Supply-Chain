package org.example.demo;

import org.example.demo.model.bo.DrugLifecycleContractAddDrugInputBO;
import org.example.demo.service.DrugLifecycleContractService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DrugLifecycleCURDTests {

    @Autowired
    DrugLifecycleContractService drugLifecycleContractService;

    @Test
    public void testAddDrug() throws Exception {
        DrugLifecycleContractAddDrugInputBO drugLifecycleContractAddDrugInputBO = new DrugLifecycleContractAddDrugInputBO();
        drugLifecycleContractAddDrugInputBO.set_id(UUID.randomUUID().toString());
        drugLifecycleContractAddDrugInputBO.set_drugName("testDrug1");
        drugLifecycleContractAddDrugInputBO.set_drugTagTagId(UUID.randomUUID().toString());
        drugLifecycleContractAddDrugInputBO.set_fake(false);
        drugLifecycleContractAddDrugInputBO.set_drugLifeCycleId("DrugLifecycle0");
        drugLifecycleContractAddDrugInputBO.set_recalled(false);
        drugLifecycleContractAddDrugInputBO.set_batchId(UUID.randomUUID().toString());

        drugLifecycleContractService.addDrug(drugLifecycleContractAddDrugInputBO);
    }

}
