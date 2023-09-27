package com.r3.developers.samples.obligation.utils;

import com.r3.developers.samples.obligation.etities.supplychain.drug.Receipt;

import java.util.UUID;

import static com.r3.developers.samples.obligation.etities.constant.EntityConstants.CORDA_REST_ADD;

public class ReceiptFactory {
    public static Receipt produceReceipt(String batchId, String operationMSG, String roleName){
        Receipt receipt = new Receipt();

        receipt.setId(UUID.randomUUID().toString());
        receipt.setProcessed(true);
        receipt.setAddress(CORDA_REST_ADD);
        receipt.setBatchId(batchId);
        receipt.setOperationMSG(operationMSG);
        receipt.setRoleName(roleName);

        return receipt;
    }
}
