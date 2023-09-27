package com.r3.developers.samples.obligation.workflows.drug;

import com.r3.developers.samples.obligation.contracts.IOUContract;
import com.r3.developers.samples.obligation.etities.dto.DrugLifeCycleReceipt;
import com.r3.developers.samples.obligation.etities.supplychain.drug.Receipt;
import com.r3.developers.samples.obligation.states.DrugLifecycleState;
import com.r3.developers.samples.obligation.utils.ReceiptFactory;
import com.r3.developers.samples.obligation.workflows.FinalizeDrugLifecycleFlow;
import net.corda.v5.application.flows.ClientRequestBody;
import net.corda.v5.application.flows.ClientStartableFlow;
import net.corda.v5.application.flows.CordaInject;
import net.corda.v5.application.flows.FlowEngine;
import net.corda.v5.application.marshalling.JsonMarshallingService;
import net.corda.v5.application.membership.MemberLookup;
import net.corda.v5.base.annotations.Suspendable;
import net.corda.v5.base.exceptions.CordaRuntimeException;
import net.corda.v5.base.types.MemberX500Name;
import net.corda.v5.ledger.common.NotaryLookup;
import net.corda.v5.ledger.utxo.UtxoLedgerService;
import net.corda.v5.ledger.utxo.transaction.UtxoSignedTransaction;
import net.corda.v5.ledger.utxo.transaction.UtxoTransactionBuilder;
import net.corda.v5.membership.MemberInfo;
import net.corda.v5.membership.NotaryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class DrugLifecycleCreateFlow implements ClientStartableFlow {
    private final static Logger log = LoggerFactory.getLogger(DrugLifecycleCreateFlow.class);

    // Injects the JsonMarshallingService to read and populate JSON parameters.
    @CordaInject
    public JsonMarshallingService jsonMarshallingService;

    // Injects the MemberLookup to look up the VNode identities.
    @CordaInject
    public MemberLookup memberLookup;

    // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
    @CordaInject
    public UtxoLedgerService ledgerService;

    // Injects the NotaryLookup to look up the notary identity.
    @CordaInject
    public NotaryLookup notaryLookup;

    // FlowEngine service is required to run SubFlows.
    @CordaInject
    public FlowEngine flowEngine;

    @Override
    @Suspendable
    public String call(ClientRequestBody requestBody) {
        log.info("IOUIssueFlow.call() called");

        try {
            // Obtain the deserialized input arguments to the flow from the requestBody.
            CreatDrugLifeCycleFlowArgs flowArgs = requestBody.getRequestBodyAs(jsonMarshallingService, CreatDrugLifeCycleFlowArgs.class);

            // Get MemberInfos for the Vnode running the flow and the otherMember.
            MemberInfo myInfo = memberLookup.myInfo();
            MemberInfo downStream = requireNonNull(
                    memberLookup.lookup(MemberX500Name.parse(flowArgs.getSupplyChainDownStream())),
                    "MemberLookup can't find otherMember specified in flow arguments."
            );

            DrugLifeCycleReceipt drugLifeCycleReceipt = flowArgs.getDrugLifeCycleReceipt();
            Receipt receipt = ReceiptFactory.produceReceipt(drugLifeCycleReceipt.getBatchId(),
                    flowArgs.getOperationMSG(),
                    myInfo.getName().toString());
            drugLifeCycleReceipt.addOperation(receipt);

            // Create the IOUState from the input arguments and member information.
            DrugLifecycleState drugLifecycleState = new DrugLifecycleState(
                    drugLifeCycleReceipt,
                    myInfo.getName(),
                    downStream.getName(),
                    Arrays.asList(myInfo.getLedgerKeys().get(0), downStream.getLedgerKeys().get(0))
            );

            // Obtain the Notary name and public key.
            NotaryInfo notary = requireNonNull(
                    notaryLookup.lookup(MemberX500Name.parse("CN=NotaryService, OU=Test Dept, O=R3, L=London, C=GB")),
                    "NotaryLookup can't find notary specified in flow arguments."
            );


            PublicKey notaryKey = null;
            for (MemberInfo memberInfo : memberLookup.lookup()) {
                if (Objects.equals(
                        memberInfo.getMemberProvidedContext().get("corda.notary.service.name"),
                        notary.getName().toString())) {
                    notaryKey = memberInfo.getLedgerKeys().get(0);
                    break;
                }
            }

            // Note, in Java CorDapps only unchecked RuntimeExceptions can be thrown not
            // declared checked exceptions as this changes the method signature and breaks override.
            if (notaryKey == null) {
                throw new CordaRuntimeException("No notary PublicKey found");
            }

            // Use UTXOTransactionBuilder to build up the draft transaction.
            UtxoTransactionBuilder txBuilder = ledgerService.createTransactionBuilder()
                    .setNotary(notary.getName())
                    .setTimeWindowBetween(Instant.now(), Instant.now().plusMillis(Duration.ofDays(1).toMillis()))
                    .addOutputState(drugLifecycleState)
                    .addCommand(new IOUContract.CreateDrugLifeCycle())
                    .addSignatories(drugLifecycleState.getParticipants());

            // Convert the transaction builder to a UTXOSignedTransaction and sign with this Vnode's first Ledger key.
            // Note, toSignedTransaction() is currently a placeholder method, hence being marked as deprecated.
            @SuppressWarnings("DEPRECATION")
            UtxoSignedTransaction signedTransaction = txBuilder.toSignedTransaction();

            // Call FinalizeIOUSubFlow which will finalise the transaction.
            // If successful the flow will return a String of the created transaction id,
            // if not successful it will return an error message.
            return flowEngine.subFlow(new FinalizeDrugLifecycleFlow.FinalizeDrugLifecycle(signedTransaction, Arrays.asList(downStream.getName())));
        }
        // Catch any exceptions, log them and rethrow the exception.
        catch (Exception e) {
            log.warn("Failed to process utxo flow for request body " + requestBody + " because: " + e.getMessage());
            throw new CordaRuntimeException(e.getMessage());
        }
    }
}
/*
RequestBody for triggering the flow via http-rpc:
{
    "clientRequestId": "createdr-1",
    "flowClassName": "com.r3.developers.samples.obligation.workflows.drug.DrugLifecycleCreateFlow",
    "requestBody": {
        "supplyChainDownStream":"CN=Bob, OU=Test Dept, O=R3, L=London, C=GB",
        "operationMSG":"test",
        "drugLifeCycleReceipt":{"id":"d1762bf0-8285-45a6-8453-21940fd60b60","idFieldName":"id","batchId":"test","drug":{"id":"d1762bf0-8285-45a6-8453-21940fd60b59","idFieldName":"id","batchId":"test","drugTagTagId":"6f228e77-7d9d-4136-806e-59472e159f03","drugName":"covid-vaccine","fake":false,"recalled":false},"lifeCycleQueue":[],"expectedReceiver":{"id":"14ebfad6-e27a-43a2-9f26-2097b13772ef","idFieldName":"id","batchId":"test","roleName":"consumer","address":"http://manufacture0-T/v1/consumer","expectedDose":1,"satisfied":false,"domain":"manufacture0-T"},"isAttacked":false,"operationQueueSize":1,"produced":false}
        }
}
 */
