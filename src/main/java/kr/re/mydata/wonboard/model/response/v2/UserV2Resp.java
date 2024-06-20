package kr.re.mydata.wonboard.model.response.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserV2Resp {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String loginEmail;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String AccessToken;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String RefreshToken;
}

