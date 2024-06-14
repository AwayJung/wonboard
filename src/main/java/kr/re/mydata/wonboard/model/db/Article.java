package kr.re.mydata.wonboard.model.db;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    private int id;
    private String title;
    private String content;
    private LocalDateTime reg_dt;
    private LocalDateTime mod_dt;
    private String reg_user_id;
    private String upd_user_id;
}
