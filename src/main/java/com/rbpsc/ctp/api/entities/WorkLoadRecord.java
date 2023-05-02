package com.rbpsc.ctp.api.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/27
 **/
@Document
@Data
public class WorkLoadRecord {
    @NotBlank
    private String id;

    @NotNull
    private WorkLoadReq workLoadReq;

    @NotNull
    private List responseTimeList;

    @NotNull
    private double averageResponseTime;

    private String benchmarkString;

    public void addResponseTime(double responseTime) {
        this.averageResponseTime = (this.averageResponseTime
                * this.responseTimeList.size()
                + responseTime)
                / (this.responseTimeList.size() + 1);
        responseTimeList.add(responseTime);
    }

}
