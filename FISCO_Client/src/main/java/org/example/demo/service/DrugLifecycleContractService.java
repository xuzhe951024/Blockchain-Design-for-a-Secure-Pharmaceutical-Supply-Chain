package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.DrugLifecycleContractAddDrugInputBO;
import org.example.demo.model.bo.DrugLifecycleContractAddExpectedReceiverInputBO;
import org.example.demo.model.bo.DrugLifecycleContractAddReceiptInputBO;
import org.example.demo.model.bo.DrugLifecycleContractCheckDrugInfoInputBO;
import org.example.demo.model.bo.DrugLifecycleContractCheckDrugLifecycleInputBO;
import org.example.demo.model.bo.DrugLifecycleContractCheckExpectedReceiverInputBO;
import org.example.demo.model.bo.DrugLifecycleContractCheckReceiptInputBO;
import org.example.demo.model.bo.DrugLifecycleContractCreateDrugLifeCycleInputBO;
import org.example.demo.model.bo.DrugLifecycleContractSelectDrugLifeCycleInputBO;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class DrugLifecycleContractService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/DrugLifecycleContract.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/DrugLifecycleContract.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/DrugLifecycleContract.bin");

  @Value("${system.contract.drugLifecycleContractAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse addExpectedReceiver(DrugLifecycleContractAddExpectedReceiverInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addExpectedReceiver", input.toArgs());
  }

  public TransactionResponse addReceipt(DrugLifecycleContractAddReceiptInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addReceipt", input.toArgs());
  }

  public CallResponse checkDrugLifecycle(DrugLifecycleContractCheckDrugLifecycleInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "checkDrugLifecycle", input.toArgs());
  }

  public CallResponse checkReceipt(DrugLifecycleContractCheckReceiptInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "checkReceipt", input.toArgs());
  }

  public CallResponse checkDrugInfo(DrugLifecycleContractCheckDrugInfoInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "checkDrugInfo", input.toArgs());
  }

  public TransactionResponse createDrugLifeCycle(DrugLifecycleContractCreateDrugLifeCycleInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "createDrugLifeCycle", input.toArgs());
  }

  public CallResponse checkExpectedReceiver(DrugLifecycleContractCheckExpectedReceiverInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "checkExpectedReceiver", input.toArgs());
  }

  public TransactionResponse addDrug(DrugLifecycleContractAddDrugInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addDrug", input.toArgs());
  }

  public CallResponse selectDrugLifeCycle(DrugLifecycleContractSelectDrugLifeCycleInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "selectDrugLifeCycle", input.toArgs());
  }
}
