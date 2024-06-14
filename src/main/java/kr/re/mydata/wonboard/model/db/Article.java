package kr.re.mydata.wonboard.model.db;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    private int id;
    private String title;
    private String content;
    private LocalDateTime regDt; ;
    private LocalDateTime modDt;
    private String regUserId;
    private String updUserId;
}
