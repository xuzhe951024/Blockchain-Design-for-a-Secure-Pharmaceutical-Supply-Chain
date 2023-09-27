package com.r3.developers.samples.obligation.states;

import com.r3.developers.samples.obligation.contracts.IOUContract;
import com.r3.developers.samples.obligation.etities.dto.DrugLifeCycleReceipt;
import net.corda.v5.base.annotations.ConstructorForDeserialization;
import net.corda.v5.base.types.MemberX500Name;
import net.corda.v5.ledger.utxo.BelongsToContract;
import net.corda.v5.ledger.utxo.ContractState;
import org.jetbrains.annotations.NotNull;


import java.security.PublicKey;
import java.util.List;

//Link with the Contract class
@BelongsToContract(IOUContract.class)
public class DrugLifecycleState implements ContractState {

    //private variables
    public final DrugLifeCycleReceipt drugLifeCycleReceipt;
    public final MemberX500Name supplyChainUpStream;
    public final MemberX500Name supplyChainDownStream;
    public List<PublicKey> participants;


    @ConstructorForDeserialization
    public DrugLifecycleState(DrugLifeCycleReceipt drugLifeCycleReceipt, MemberX500Name supplyChainUpStream, MemberX500Name supplyChainDownStream, List<PublicKey> participants) {
        this.drugLifeCycleReceipt = drugLifeCycleReceipt;
        this.supplyChainUpStream = supplyChainUpStream;
        this.supplyChainDownStream = supplyChainDownStream;
        this.participants = participants;
    }

    @NotNull
    @Override
    public List<PublicKey> getParticipants() {
        return participants;
    }

    public DrugLifeCycleReceipt getDrugLifeCycleReceipt() {
        return drugLifeCycleReceipt;
    }

    public MemberX500Name getSupplyChainUpStream() {
        return supplyChainUpStream;
    }

    public MemberX500Name getSupplyChainDownStream() {
        return supplyChainDownStream;
    }

    public void setParticipants(List<PublicKey> participants) {
        this.participants = participants;
    }

    public DrugLifecycleState passToNextSupplyChainNode(DrugLifeCycleReceipt drugLifeCycleReceipt, MemberX500Name supplyChainUpStream, MemberX500Name supplyChainDownStream, List<PublicKey> participants){
        return new DrugLifecycleState(drugLifeCycleReceipt, supplyChainUpStream, supplyChainDownStream, participants);
    }
}
