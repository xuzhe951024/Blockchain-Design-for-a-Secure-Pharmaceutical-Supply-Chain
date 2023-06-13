package com.rbpsc.ctp.api.entities.configs;


import com.rbpsc.ctp.api.entities.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExperimentConfig extends BaseEntity<String> {
    private String experimentName;
    private String experimentDescription;
    private String drugName;
    private int maxThreadCount;
    private int manufacturerCount;
    private List<Integer> distributorsForEachLevel;
    private int consumerCount;
    private int doesForEachConsumer;
}
