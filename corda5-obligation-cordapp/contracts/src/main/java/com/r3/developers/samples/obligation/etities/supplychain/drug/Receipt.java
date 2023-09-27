package com.r3.developers.samples.obligation.etities.supplychain.drug;

import com.r3.developers.samples.obligation.etities.base.OperationBase;
import net.corda.v5.base.annotations.CordaSerializable;

@CordaSerializable
public class Receipt extends OperationBase {

    private boolean isProcessed = true;

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}
