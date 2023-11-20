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
import net.corda.v5.ledger.utxo.StateAndRef;
import net.corda.v5.ledger.utxo.UtxoLedgerService;
import net.corda.v5.ledger.utxo.transaction.UtxoSignedTransaction;
import net.corda.v5.ledger.utxo.transaction.UtxoTransactionBuilder;
import net.corda.v5.membership.MemberInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class DrugLifecycleTransferFlow implements ClientStartableFlow {

    private final static Logger log = LoggerFactory.getLogger(DrugLifecycleTransferFlow.class);

    // Injects the JsonMarshallingService to read and populate JSON parameters.
    @CordaInject
    public JsonMarshallingService jsonMarshallingService;

    // Injects the MemberLookup to look up the VNode identities.
    @CordaInject
    public MemberLookup memberLookup;

    // Injects the UtxoLedgerService to enable the flow to make use of the Ledger API.
    @CordaInject
    public UtxoLedgerService ledgerService;

    // FlowEngine service is required to run SubFlows.
    @CordaInject
    public FlowEngine flowEngine;

    @Override
    @Suspendable
    public String call(ClientRequestBody requestBody) {
        log.info("IOUTransferFlow.call() called");

        try {
            // Obtain the deserialized input arguments to the flow from the requestBody.
            DrugLifecycleTransferFlowArgs flowArgs = requestBody.getRequestBodyAs(jsonMarshallingService, DrugLifecycleTransferFlowArgs.class);

            // Get flow args from the input JSON
            String drugLifecycleId = flowArgs.getDrugLifecycleId();
            String newOperationMSG = flowArgs.getNewOperationMSG();

            //query the IOU input
            List<StateAndRef<DrugLifecycleState>> drugLifeCycleStateAndRefs = ledgerService.findUnconsumedStatesByType(DrugLifecycleState.class);
            List<StateAndRef<DrugLifecycleState>> drugLifeCycleStateAndRefsWithId = drugLifeCycleStateAndRefs.stream()
                    .filter(sar -> sar.getState().getContractState().getDrugLifeCycleReceipt().getId().equals(drugLifecycleId)).collect(toList());
            if (drugLifeCycleStateAndRefsWithId.size() != 1)
                throw new CordaRuntimeException("Multiple or zero IOU states with id " + drugLifecycleId + " found");
            StateAndRef<DrugLifecycleState> drugLifeCycleStateAndRef = drugLifeCycleStateAndRefsWithId.get(0);
            DrugLifecycleState drugLifeCycleInput = drugLifeCycleStateAndRef.getState().getContractState();

            //flow logic checks
            MemberInfo myInfo = memberLookup.myInfo();
            if (!(myInfo.getName().toString().equals(drugLifeCycleInput.
                    getDrugLifeCycleReceipt().
                    peakOperationVOQ().
                    getRoleName())))
                throw new CordaRuntimeException("Not an authorized supply chain up stream: local node name: " + myInfo.getName().toString() +
                        "\tOperation upStream name: " +
                        drugLifeCycleInput.
                            getDrugLifeCycleReceipt().
                            peakOperationVOQ().
                            getRoleName());

            // Get MemberInfos for the Vnode running the flow and the otherMember.
            MemberInfo targetSupplyChainNode = requireNonNull(
                    memberLookup.lookup(drugLifeCycleInput.getSupplyChainDownStream()),
                    "MemberLookup can't find otherMember specified in flow arguments."
            );

            MemberInfo newSupplyChainNode = requireNonNull(
                    memberLookup.lookup(MemberX500Name.parse(flowArgs.getNewSupplyChainDownStream())),
                    "MemberLookup can't find otherMember specified in flow arguments."
            );

            String oldOperationMSG = drugLifeCycleInput.getDrugLifeCycleReceipt().peakOperationVOQ().getOperationMSG();
            if (!oldOperationMSG.equals(newOperationMSG)) throw new CordaRuntimeException("Not a legal operationMSG.");

            // Create the IOUState from the input arguments and member information.
            Receipt receipt = ReceiptFactory.produceReceipt(drugLifeCycleInput.getDrugLifeCycleReceipt().getBatchId(),
                    newOperationMSG,
                    targetSupplyChainNode.getName().toString());
            DrugLifeCycleReceipt drugLifeCycleReceipt = jsonMarshallingService.parse(jsonMarshallingService.format(drugLifeCycleInput.getDrugLifeCycleReceipt()), DrugLifeCycleReceipt.class);
            drugLifeCycleReceipt.addOperation(receipt);
            DrugLifecycleState drugLifeCycleOutput = drugLifeCycleInput.passToNextSupplyChainNode(drugLifeCycleReceipt,
                    myInfo.getName(),
                    newSupplyChainNode.getName(),
                    Arrays.asList(myInfo.getLedgerKeys().get(0), targetSupplyChainNode.getLedgerKeys().get(0)));

            //get notary from input
            MemberX500Name notary = drugLifeCycleStateAndRef.getState().getNotaryName();

            // Use UTXOTransactionBuilder to build up the draft transaction.
            UtxoTransactionBuilder txBuilder = ledgerService.createTransactionBuilder()
                    .setNotary(notary)
                    .setTimeWindowBetween(Instant.now(), Instant.now().plusMillis(Duration.ofDays(1).toMillis()))
                    .addInputState(drugLifeCycleStateAndRef.getRef())
                    .addOutputState(drugLifeCycleOutput)
                    .addCommand(new IOUContract.Transfer())
                    .addSignatories(Arrays.asList(myInfo.getLedgerKeys().get(0), targetSupplyChainNode.getLedgerKeys().get(0)));

            // Convert the transaction builder to a UTXOSignedTransaction and sign with this Vnode's first Ledger key.
            // Note, toSignedTransaction() is currently a placeholder method, hence being marked as deprecated.
            UtxoSignedTransaction signedTransaction = txBuilder.toSignedTransaction();

            // Call FinalizeIOUSubFlow which will finalise the transaction.
            // If successful the flow will return a String of the created transaction id,
            // if not successful it will return an error message.
            return flowEngine.subFlow(new FinalizeDrugLifecycleFlow.FinalizeDrugLifecycle(signedTransaction, Arrays.asList(myInfo.getName(), targetSupplyChainNode.getName())));
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
    "clientRequestId": "transferdrugLifeCycle-1",
    "flowClassName": "com.r3.developers.samples.obligation.workflows.drug.DrugLifecycleTransferFlow",
    "requestBody": {
        "newSupplyChainDownStream":"CN=Charlie, OU=Test Dept, O=R3, L=London, C=GB",
        "newOperationMSG":"test",
        "drugLifecycleId":"d1762bf0-8285-45a6-8453-21940fd60b60"
        }
}
 */