package kr.re.mydata.wonboard.model.request;
import org.springframework.web.multipart.MultipartFile;
import kr.re.mydata.wonboard.model.db.Article;
import lombok.Data;

@Data
public class ArticleReq {
    private Article article;
    private MultipartFile file;
}
