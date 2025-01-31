package kr.re.mydata.wonboard.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserReq {
    private int id;
    private String loginEmail;
    private String password;
    private String refreshToken;
    private LocalDateTime refreshTokenIssueDt;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private String name;

}