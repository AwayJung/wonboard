package kr.re.mydata.wonboard.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleResp {
    private String title;
    private String content;
    private String regUserId;
    private  LocalDateTime regDt;
    private  LocalDateTime updDt;
}
