package com.r3.developers.samples.obligation.etities.dto;

import com.r3.developers.samples.obligation.etities.supplychain.drug.DrugLifeCycle;
import com.r3.developers.samples.obligation.etities.supplychain.drug.Receipt;
import net.corda.v5.base.annotations.CordaSerializable;

@CordaSerializable
public class DrugLifeCycleReceipt extends DrugLifeCycle<Receipt> {
}
