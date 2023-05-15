package com.rbpsc.ctp.api.entities.supplychain.roles;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Consumer extends RoleBase {
    int expectedDose;

    public void satisfyDosage() {
        if (this.expectedDose > 0){
            this.expectedDose--;
        }
    }

    public boolean isSatisfied() {
        return this.expectedDose == 0;
    }
}
