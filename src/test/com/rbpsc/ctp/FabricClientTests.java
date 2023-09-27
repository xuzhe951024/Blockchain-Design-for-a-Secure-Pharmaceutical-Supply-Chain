package com.rbpsc.ctp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.common.factories.DataEntityFactory;
import com.rbpsc.common.utiles.fabric.EnrollAdmin;
import com.rbpsc.common.utiles.fabric.RegisterUser;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.*;
import org.junit.jupiter.api.Test;
import org.rbpsc.api.entities.dto.wrappers.DrugLifeCycleReceipt;
import org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import org.rbpsc.api.entities.supplychain.operations.OperationBase;
import org.rbpsc.api.entities.supplychain.operations.Receipt;
import org.rbpsc.api.entities.supplychain.roles.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.rbpsc.common.constant.ServiceConstants.*;

@SpringBootTest
@Slf4j
public class FabricClientTests {

    @Autowired
    EnrollAdmin enrollAdmin;

    @Autowired
    RegisterUser registerUser;

    @Value("${fabric.config.networkconfig_path}")
    String pathToNetWorkConfig;

    @Value("${fabric.config.wallet_path}")
    String pathToWallet;

    static {
        System.setProperty(FABRIC_SERVICE_LOCAL_DISCOVERY_CONFIG, TRUE);
    }

    // helper function for getting connected to the gateway
    public Gateway connect() throws Exception{
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get(pathToWallet);
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get(pathToNetWorkConfig);

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, FABRIC_JAVA_CLIENT_ID).networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }

    public static DrugLifeCycle<Receipt> buildDrugCycle(String drugName){

        OperationBase operationBase = new OperationBase();
        operationBase.setId(UUID.randomUUID().toString());
        operationBase.setOperationMSG("Test MSG 1");
        operationBase.setBatchId("test");

        Receipt receipt = DataEntityFactory.createReceipt(operationBase);
        receipt.setRoleName("testRole");
        receipt.setAddress("http://manufacture0-T/v1/drugLifeCycle/drugOrderStep/manufacture");
        receipt.setBatchId("test");
        DrugInfo drugInfo = DataEntityFactory.createDrugInfo(operationBase, drugName);
        drugInfo.setDrugTagTagId(UUID.randomUUID().toString());

        DrugLifeCycle<Receipt> drugLifeCycle = DataEntityFactory.createDrugLifeCycleReceipt(drugInfo);
        drugLifeCycle.addOperation(receipt);

        Consumer consumer = DataEntityFactory.createConsumer(1, "http://manufacture0-T/v1/consumer", "test", Optional.of(UUID.randomUUID().toString()));
        drugLifeCycle.setExpectedReceiver(consumer);

        return drugLifeCycle;
    }

    public static DrugLifeCycle<Receipt> addReceiptToDrugLifeCycle(String msg, DrugLifeCycle<Receipt> receiptDrugLifeCycle){

        Receipt receipt = receiptDrugLifeCycle.peakOperationVOQ();
        receipt.setId(UUID.randomUUID().toString());
        receipt.setOperationMSG(msg);

        receiptDrugLifeCycle.addOperation(receipt);

        return receiptDrugLifeCycle;
    }

    @Autowired
    ObjectMapper objectMapper;
    
    @Test
    public void testInteractToSmartContract(){
        // enrolls the admin and registers the user
        try {
            enrollAdmin.enroll();
            registerUser.register();
        } catch (Exception e) {
            log.error("89:" + e.getMessage());
        }

        // connect to the network and invoke the smart contract
        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork(CHANNEL_NAME);
            Contract contract = network.getContract(CHAINCODE_NAME);

            byte[] result;

            log.info("Submit Transaction: InitLedger creates the initial set of assets on the ledger.");
            contract.submitTransaction("InitLedger");

            log.info("\n");
            log.info("Submit Transaction: DrugLifeCycle covid-vaccine");
            DrugLifeCycle<Receipt> covidVaccine = buildDrugCycle("covid-vaccine");
//            covidVaccine.setId("57fab5fa-2ffd-4890-964f-cb1ea07a5d99");
            log.info("121:" + objectMapper.writeValueAsString(covidVaccine));
            contract.submitTransaction("CreateDruglifeCycle", objectMapper.writeValueAsString(covidVaccine));


            log.info("\n");
            result = contract.evaluateTransaction("GetAllDrugLifeCycle");
            log.info("Evaluate Transaction: GetAllAssets, result: " + new String(result));


            log.info("\n");
            log.info("Evaluate Transaction: ReadDrugLifeCycle" + covidVaccine.getId());
            result = contract.evaluateTransaction("ReadDrugLifeCycle", covidVaccine.getId());
            log.info("result: " + new String(result));
            assert objectMapper.readValue(new String(result), DrugLifeCycleReceipt.class).getId().equals(covidVaccine.getId());

            log.info("\n");
            log.info("Submit Transaction: addReceiptToDrugLifeCycle");
            Receipt receipt = objectMapper.readValue(objectMapper.writeValueAsString(covidVaccine.peakOperationVOQ()), Receipt.class);
            receipt.setId(UUID.randomUUID().toString());
            receipt.setOperationMSG("new test receipt");
            contract.submitTransaction("addReceiptToDrugLifeCycle", covidVaccine.getId(), objectMapper.writeValueAsString(receipt));
//
//            log.info("\n");
//            log.info("Evaluate Transaction: AssetExists asset1");
//            // AssetExists returns "true" if an asset with given assetID exist
//            result = contract.evaluateTransaction("AssetExists", "asset1");
//            log.info("result: " + new String(result));
//
//            log.info("\n");
//            log.info("Submit Transaction: UpdateAsset asset1, new AppraisedValue : 350");
//            // UpdateAsset updates an existing asset with new properties. Same args as CreateAsset
//            contract.submitTransaction("UpdateAsset", "asset1", "blue", "5", "Tomoko", "350");
//
//            log.info("\n");
//            log.info("Evaluate Transaction: ReadAsset asset1");
//            result = contract.evaluateTransaction("ReadAsset", "asset1");
//            log.info("result: " + new String(result));
//
//            try {
//                log.info("\n");
//                log.info("Submit Transaction: UpdateAsset asset70");
//                // None existing asset asset70 should throw Error
//                contract.submitTransaction("UpdateAsset", "asset70", "blue", "5", "Tomoko", "300");
//            } catch (Exception e) {
//                log.error("Expected an error on UpdateAsset of non-existing Asset: " + e);
//            }
//

//
//            log.info("\n");
//            log.info("Evaluate Transaction: ReadAsset asset1");
//            result = contract.evaluateTransaction("ReadAsset", "asset1");
//            log.info("result: " + new String(result));
        }
        catch(Exception e){
            log.error("157:" + e.getMessage());
            System.exit(1);
        }

    }
    
}
