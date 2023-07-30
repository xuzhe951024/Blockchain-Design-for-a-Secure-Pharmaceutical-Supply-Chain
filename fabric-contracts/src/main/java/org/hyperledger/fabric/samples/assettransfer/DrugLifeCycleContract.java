/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.ArrayList;
import java.util.List;


import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;
import org.rbpsc.api.entities.dto.wrappers.DrugLifeCycleReceipt;
import org.rbpsc.api.entities.supplychain.operations.Receipt;

@Contract(
        name = "basic",
        info = @Info(
                title = "Drug life cycle",
                description = "The Drug life cycle management",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "a.transfer@example.com",
                        name = "Drug life cycle",
                        url = "https://hyperledger.example.com")))
@Default
@Slf4j
public final class DrugLifeCycleContract implements ContractInterface {

    private final Genson genson = new Genson();

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }

    /**
     * Creates some initial assets on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();



    }

    /**
     * Creates a new asset on the ledger.
     *
     * @param ctx the transaction context
     * @param drugLifeCycleJSON the desired drugLifeCycle entity
     * @return the created asset
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String CreateDruglifeCycle(final Context ctx, final String drugLifeCycleJSON) {
        ChaincodeStub stub = ctx.getStub();

        DrugLifeCycleReceipt drugLifeCycleReceipt = genson.deserialize(drugLifeCycleJSON, DrugLifeCycleReceipt.class);

        if (drugLifeCycleExists(ctx, drugLifeCycleReceipt.getId())) {
            String errorMessage = String.format("drugLifeCycle %s already exists", drugLifeCycleReceipt.getId());
            log.error(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        stub.putStringState(drugLifeCycleReceipt.getId(), drugLifeCycleJSON);

        return drugLifeCycleJSON;
    }

    /**
     * Retrieves an asset with the specified ID from the ledger.
     *
     * @param ctx the transaction context
     * @param drugLifeCycleId the ID of the asset
     * @return the asset found on the ledger if there was one
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String ReadDrugLifeCycle(final Context ctx, final String drugLifeCycleId) {
        ChaincodeStub stub = ctx.getStub();
        String drugLifeCycleJSON = stub.getStringState(drugLifeCycleId);

        if (drugLifeCycleJSON == null || drugLifeCycleJSON.isEmpty()) {
            String errorMessage = String.format("drugLifeCycle %s does not exist", drugLifeCycleId);
            log.error(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        return drugLifeCycleJSON;
    }

    /**
     * Updates the properties of an asset on the ledger.
     *
     * @param ctx the transaction context
     * @param drugLifeCycleJSON the desired drugLifeCycle
     * @return the transferred asset
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String UpdateDrugLifeCycle(final Context ctx, final String drugLifeCycleJSON) {
        ChaincodeStub stub = ctx.getStub();

        DrugLifeCycleReceipt drugLifeCycleReceipt = genson.deserialize(drugLifeCycleJSON, DrugLifeCycleReceipt.class);

        if (!drugLifeCycleExists(ctx, drugLifeCycleReceipt.getId())) {
            String errorMessage = String.format("drugLifeCycle %s does not exist", drugLifeCycleReceipt.getId());
            log.error(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        // Use Genson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        stub.putStringState(drugLifeCycleReceipt.getId(), drugLifeCycleJSON);
        return drugLifeCycleJSON;
    }

    /**
     * Deletes asset on the ledger.
     *
     * @param ctx the transaction context
     * @param drugLifeCycleID the ID of the asset being deleted
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteDrugLifeCycle(final Context ctx, final String drugLifeCycleID) {
        ChaincodeStub stub = ctx.getStub();

        if (!drugLifeCycleExists(ctx, drugLifeCycleID)) {
            String errorMessage = String.format("drugLifeCycleID %s does not exist", drugLifeCycleID);
            log.error(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        stub.delState(drugLifeCycleID);
    }

    /**
     * Checks the existence of the asset on the ledger
     *
     * @param ctx the transaction context
     * @param drugLifeCycleID the ID of the asset
     * @return boolean indicating the existence of the asset
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean drugLifeCycleExists(final Context ctx, final String drugLifeCycleID) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(drugLifeCycleID);

        return (assetJSON != null && !assetJSON.isEmpty());
    }

    /**
     * Changes the owner of an asset on the ledger.
     *
     * @param ctx the transaction context
     * @param drugLifeCycleID the ID of the asset being transferred
     * @param receiptJSON the new valid receipt
     * @return the old owner
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Boolean addReceiptToDrugLifeCycle(final Context ctx, final String drugLifeCycleID, final String receiptJSON) {
        ChaincodeStub stub = ctx.getStub();
        String drugLifeCycleJSON = stub.getStringState(drugLifeCycleID);

        if (drugLifeCycleJSON == null || drugLifeCycleJSON.isEmpty()) {
            String errorMessage = String.format("drugLifeCycle %s does not exist", drugLifeCycleID);
            log.error(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        DrugLifeCycleReceipt drugLifeCycle = genson.deserialize(drugLifeCycleJSON, DrugLifeCycleReceipt.class);
        Receipt receipt = genson.deserialize(receiptJSON, Receipt.class);
        drugLifeCycle.addOperation(receipt);

        // Use a Genson to conver the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = genson.serialize(drugLifeCycle);
        stub.putStringState(drugLifeCycleID, sortedJson);

        drugLifeCycleJSON = stub.getStringState(drugLifeCycleID);
        DrugLifeCycleReceipt drugLifeCycleUpdated = genson.deserialize(drugLifeCycleJSON, DrugLifeCycleReceipt.class);

        return drugLifeCycleUpdated.getOperationQueueSize() > drugLifeCycle.getOperationQueueSize();
    }

    /**
     * Retrieves all assets from the ledger.
     *
     * @param ctx the transaction context
     * @return array of assets found on the ledger
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllDrugLifeCycle(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<DrugLifeCycleReceipt> queryResults = new ArrayList<DrugLifeCycleReceipt>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            DrugLifeCycleReceipt drugLifeCycle = genson.deserialize(result.getStringValue(), DrugLifeCycleReceipt.class);
            log.info(drugLifeCycle.toString());
            queryResults.add(drugLifeCycle);
        }

        return genson.serialize(queryResults);
    }
}
