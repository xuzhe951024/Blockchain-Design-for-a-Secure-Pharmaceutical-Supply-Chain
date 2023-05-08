package com.rbpsc.ctp.api.entities.supplychain.roles;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class Consumer extends RoleBase {
    int dose;

    public void satisfyDosage() {
        this.dose--;
    }

    public boolean isSatisfied() {
        return this.dose == 0;
    }
}
