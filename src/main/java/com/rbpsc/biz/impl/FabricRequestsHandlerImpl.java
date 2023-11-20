package com.rbpsc.biz.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.biz.service.BlockChainRequestsHandler;
import com.rbpsc.common.factories.DataEntityFactory;
import com.rbpsc.common.utiles.FileUtils;
import com.rbpsc.common.utiles.fabric.EnrollAdmin;
import com.rbpsc.common.utiles.fabric.RegisterUser;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.*;
import org.rbpsc.api.entities.dto.DrugOperationDTO;
import org.rbpsc.api.entities.dto.response.DrugLifeCycleResponse;
import org.rbpsc.api.entities.dto.wrappers.DrugLifeCycleReceipt;
import org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import org.rbpsc.api.entities.supplychain.operations.OperationBase;
import org.rbpsc.api.entities.supplychain.operations.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.rbpsc.common.constant.EntityConstants.FABRIC_WALLET_ID_FILE_SUFFIX;
import static org.rbpsc.common.constant.ServiceConstants.*;

@Service
@Slf4j
public class FabricRequestsHandlerImpl implements BlockChainRequestsHandler {
    @Override
    public DrugLifeCycleResponse sendToNextStep(DrugOperationDTO drugOperationDTO) {

        DrugLifeCycleResponse response = new DrugLifeCycleResponse();

        if (!drugLifeCycleExists(drugOperationDTO.getDrug().getId())){
            if (null == createDrugLifeCycle(drugOperationDTO.getDrug(), drugOperationDTO.getOperationDTO().getOperation())){
                response.setResponseWithCode(RESPONSE_CODE_FAIL_SERVICE_DISABLED);
            }
        } else {
            if (!addToReceiptQueue(drugOperationDTO.getDrug(), drugOperationDTO.getOperationDTO().getOperation())){
                response.setResponseWithCode(RESPONSE_CODE_FAIL_SERVICE_DISABLED);
            }
        }

        return response;
    }

    @Autowired
    EnrollAdmin enrollAdmin;

    @Autowired
    RegisterUser registerUser;

    @Value("${fabric.config.networkconfig_path}")
    String pathToNetWorkConfig;

    @Value("${fabric.config.wallet_path}")
    String pathToWallet;

    @Autowired
    ObjectMapper objectMapper;

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

    private void enrollAndRegisterMSPUserId(){
        // enrolls the admin and registers the user
        try {
            FileUtils.deleteFilesWithExtension(pathToWallet, FABRIC_WALLET_ID_FILE_SUFFIX);
            enrollAdmin.enroll();
            registerUser.register();
        } catch (Exception e) {
            log.error("89:" + e.getMessage());
        }
    }

    private Boolean drugLifeCycleExists(String id){
        // connect to the network and invoke the smart contract
        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork(CHANNEL_NAME);
            Contract contract = network.getContract(CHAINCODE_NAME);

            byte[] result;
            String resultString;

            log.info("\n");
            log.info("Check if DrugLifeCycle: " + id + " existed in Fabric ledger");
            result = contract.evaluateTransaction("drugLifeCycleExists", id);
            resultString = new String(result);
            log.info("result: " + resultString);

            if (TRUE.equals(resultString)){
                return true;
            }
        }
        catch(Exception e){
            log.error("157:" + e.getMessage());
        }

        return false;
    }

    private DrugLifeCycleReceipt queryDrugLifeCycle(String id){
        // connect to the network and invoke the smart contract
        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork(CHANNEL_NAME);
            Contract contract = network.getContract(CHAINCODE_NAME);

            byte[] result;
            String resultString;

            if (drugLifeCycleExists(id)){
                log.info("\n");
                log.info("Evaluate Transaction: ReadDrugLifeCycle" + id);
                result = contract.evaluateTransaction("ReadDrugLifeCycle", id);
                resultString = new String(result);
                log.info("result: " + resultString);

                return objectMapper.readValue(resultString, DrugLifeCycleReceipt.class);
            }
        }
        catch(Exception e){
            log.error("157:" + e.getMessage());
        }

        return null;
    }

    private String createDrugLifeCycle(DrugInfo drug, OperationBase operationBase){
        String targetId = drug.getId();
        if (drugLifeCycleExists(targetId)) {
            log.error("DrugLifecycle: " + targetId + " is already existed");
            return null;
        }

        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork(CHANNEL_NAME);
            Contract contract = network.getContract(CHAINCODE_NAME);

            log.info("\n");
            log.info("Submit Transaction: CreateDruglifeCycle");
            DrugLifeCycle<Receipt> receiptDrugLifeCycle = DataEntityFactory.createDrugLifeCycleReceipt(drug);
            Receipt receipt = DataEntityFactory.createReceipt(operationBase);
            receiptDrugLifeCycle.addOperation(receipt);
            log.info("121:" + objectMapper.writeValueAsString(receiptDrugLifeCycle));
            return new String(contract.submitTransaction("CreateDruglifeCycle", objectMapper.writeValueAsString(receiptDrugLifeCycle)));
        }
        catch(Exception e){
            log.error("157:" + e.getMessage());
        }

        return null;
    }

    private Boolean addToReceiptQueue(DrugInfo drug, OperationBase operationBase){
        String targetId = drug.getId();
        if (!drugLifeCycleExists(targetId)) {
            log.error("DrugLifecycle: " + targetId + " not exists!");
            return false;
        }

        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork(CHANNEL_NAME);
            Contract contract = network.getContract(CHAINCODE_NAME);

            log.info("\n");
            log.info("Submit Transaction: addReceiptToDrugLifeCycle");
            Receipt receipt = DataEntityFactory.createReceipt(operationBase);
            receipt.setOperationMSG("new test receipt");
            contract.submitTransaction("addReceiptToDrugLifeCycle", targetId, objectMapper.writeValueAsString(receipt));
            return true;
        }
        catch(Exception e){
            log.error("157:" + e.getMessage());
        }

        return false;
    }
}
