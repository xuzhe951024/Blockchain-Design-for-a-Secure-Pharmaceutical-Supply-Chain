package com.rbpsc.ctp.api.entities.work_request;

import com.rbpsc.ctp.api.entities.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkLoadRecord extends BaseEntity<String> {
    @NotNull
    private WorkLoadReq workLoadReq;

    @NotNull
    private List<Double> responseTimeList;

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
