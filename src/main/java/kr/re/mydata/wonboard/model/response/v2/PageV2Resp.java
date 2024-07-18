package kr.re.mydata.wonboard.model.response.v2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageV2Resp {
    private List<ListV2Resp> articles;
    private int size;
    private int limit;
    private int totalArticle;
}
