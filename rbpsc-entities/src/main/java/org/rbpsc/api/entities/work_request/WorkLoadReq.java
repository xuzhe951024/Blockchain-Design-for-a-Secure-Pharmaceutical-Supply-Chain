package org.rbpsc.api.entities.work_request;

import org.rbpsc.common.constant.ServiceConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/
@Data
public class WorkLoadReq {
    @NotBlank(message = "url can not be null or blank")
    String url;

    @NotNull(message = "request count can not be null")
    Integer reqCount;

    @NotNull(message = "lamda can not be null")
    double lamda;

    @NotNull(message = "sigma can not be null")
    double sigma;

    @NotNull(message = "mu can not be null")
    double mu;

    @NotBlank(message = "case can not be null")
    String normalOrPoisson;

    public boolean isNormalCase(){
        return normalOrPoisson.equals(ServiceConstants.NORMAL_CASE);
    }
}
