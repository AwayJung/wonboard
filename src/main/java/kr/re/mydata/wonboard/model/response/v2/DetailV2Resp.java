package kr.re.mydata.wonboard.model.response.v2;

import lombok.Data;

@Data
public class DetailV2Resp {
    private int id;
    private String writer;
    private String title;
    private String content;
    private String path;
}
