package kr.re.mydata.wonboard.model.db;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class User {
    private int id;
    private String loginEmail;
    private String password;
    private String name;
    private String refreshToken;
    private LocalDateTime refreshTokenIssueDt;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
}

