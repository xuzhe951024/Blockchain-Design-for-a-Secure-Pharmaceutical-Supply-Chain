package com.r3.developers.samples.obligation.etities.roles;

import com.r3.developers.samples.obligation.etities.base.RoleBase;
import net.corda.v5.base.annotations.CordaSerializable;

@CordaSerializable
public class Consumer extends RoleBase {
    private int expectedDose;

    private boolean satisfied;

    public void satisfyDosage() {
        if (this.expectedDose > 0){
            this.expectedDose--;
            if (this.expectedDose == 0){
                this.satisfied = true;
            }
        }
    }

    public boolean isSatisfied() {
        return this.expectedDose == 0;
    }

    public int getExpectedDose() {
        return expectedDose;
    }

    public void setExpectedDose(int expectedDose) {
        this.expectedDose = expectedDose;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }
}
