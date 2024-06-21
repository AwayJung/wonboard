package kr.re.mydata.wonboard.model.response.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

    @Data
    public class ArticleV2Resp {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String title;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String content;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String regUserId;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private LocalDateTime regDt;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private  LocalDateTime updDt;
}
