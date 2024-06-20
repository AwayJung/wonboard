package kr.re.mydata.wonboard.common.exception;

import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.util.MessageUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class CommonApiException extends Exception {

    private ApiRespPolicy apiRespPolicy;

    public CommonApiException(String message) {
        super(message);
    }

    public CommonApiException(ApiRespPolicy apiRespPolicy) {
        this(MessageUtil.getMessage(apiRespPolicy.getMessageKey()), apiRespPolicy);
    }

    public CommonApiException(String message, ApiRespPolicy apiRespPolicy) {
        super(message);
        this.apiRespPolicy = apiRespPolicy;
    }

}
