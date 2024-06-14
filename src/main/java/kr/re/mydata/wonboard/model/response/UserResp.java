package kr.re.mydata.wonboard.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResp {
    private String loginEmail;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private String name;
}

