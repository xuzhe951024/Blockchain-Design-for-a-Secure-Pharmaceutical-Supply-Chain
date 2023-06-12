package com.rbpsc.ctp.common.Constant;

import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/
public class ServiceConstants {
    public static final String NORMAL_CASE = "N";
    public static final String POISSON_CASE = "P";
    public static final String MANGODB_COLLECTION_NAME_BENCHMARK = "benchmark";

    public static final String V1_BASE_ADD = "v1";

    public static final String DRUG_LIFECYCLE_REQUEST_PARAM_NAMES = "drugLifeCycle";

    public static final String SERVICE_NAME_TOGGLE_API = "/v1/drugLifeCycle/drugOrderStep/toggle";

    public static final String WEB_SOCKET_ENDPOINT = "/websocket";

    public static final AtomicBoolean API_ENABLED = new AtomicBoolean(true);

    public static final int RESPONSE_CODE_SUCCESS = 202;
    public static final int RESPONSE_CODE_FAIL_OPERATION_TYPE_NOT_MATCH = 400;
    public static final int RESPONSE_CODE_FAIL_FIND_ADDRESS = 404;

    public static final int RESPONSE_CODE_FAIL_BAD_GATEWAY = 502;

    public static final int RESPONSE_CODE_FAIL_SERVICE_DISABLED = 503;
    public static final Map<Integer, String> RESPONSE_CODE_MESSAGE_MAP = new HashMap<Integer, String>(){{
        put(RESPONSE_CODE_SUCCESS, "Success");
        put(RESPONSE_CODE_FAIL_OPERATION_TYPE_NOT_MATCH, "Wrong operation type!");
        put(RESPONSE_CODE_FAIL_FIND_ADDRESS, "Address not found!");
        put(RESPONSE_CODE_FAIL_BAD_GATEWAY, "DrugLifeCycle operation service error or database error!");
        put(RESPONSE_CODE_FAIL_SERVICE_DISABLED, "Service now disabled!");
    }};
}
