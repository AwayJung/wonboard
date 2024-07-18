package kr.re.mydata.wonboard.model.response.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 유저 API 응답 모델
 *
 * @author yrlee@mydata.re.kr
 */
@Data
public class UserV2Resp {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String loginEmail;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String accessToken;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String refreshToken;
}

