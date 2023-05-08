package com.rbpsc.ctp.api.entities.supplychain.attack;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class AttackAvailability extends AttackModelBase{
    int expectedCompromisedNodes;
}
