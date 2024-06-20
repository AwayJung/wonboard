package kr.re.mydata.wonboard.common.constant;

import org.springframework.http.HttpStatus;

public enum ApiRespPolicy {
    SUCCESS(HttpStatus.OK, 20000, "message.api.resp.policy.success"),
    SUCCESS_CREATED(HttpStatus.CREATED, 20100,"message.api.resp.policy.success_created"),

    // Default Error
    ERR_SYSTEM(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "message.api.resp.policy.err_system"),

    ERR_INVALID_PARAMS(HttpStatus.BAD_REQUEST, 40001, "message.api.resp.policy.err_invalid_params"),
    ERR_DUPLICATED_USER(HttpStatus.CONFLICT, 40901, "message.api.resp.policy.err_duplicated_user"),
    ERR_INVALID_USER(HttpStatus.UNAUTHORIZED, 40400, "message.api.resp.policy.err_invalid_user"),;

    private final HttpStatus httpStatus;
    private final int code;
    private final String messageKey;

    ApiRespPolicy(HttpStatus httpStatus, int code, String messageKey) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.messageKey = messageKey;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
