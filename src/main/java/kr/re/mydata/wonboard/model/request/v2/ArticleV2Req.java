package kr.re.mydata.wonboard.model.request.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ArticleV2Req {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;
}
