package kr.re.mydata.wonboard.controller.v2;

import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.model.response.ApiV2Resp;
import kr.re.mydata.wonboard.model.db.Article;
import kr.re.mydata.wonboard.model.request.v2.ArticleV2Req;
import kr.re.mydata.wonboard.service.v2.ArticleV2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v2/articles")
public class ArticleV2Controller {
    private static final Logger logger = LoggerFactory.getLogger(ArticleV2Controller.class);

    @Autowired
    private ArticleV2Service articleService;

    @PostMapping("/post")
    public ResponseEntity<ApiV2Resp> postArticle(@RequestPart ArticleV2Req articleV2Req, @RequestPart("file") MultipartFile file) throws IOException, CommonApiException {
        Article article = new Article();
        article.setTitle(articleV2Req.getTitle());
        article.setContent(articleV2Req.getContent());

        articleService .postArticle(article, file);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS_CREATED.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS_CREATED));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiV2Resp> getArticleList() throws CommonApiException {
        articleService.getArticleList();
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiV2Resp> getArticle(@PathVariable("id") int id) throws CommonApiException {
        articleService.getArticle(id);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiV2Resp> updateArticle(@PathVariable("id") int id, @RequestPart ArticleV2Req articleV2Req) throws IOException, CommonApiException {
        Article article = new Article();
        article.setTitle(articleV2Req.getTitle());
        article.setContent(articleV2Req.getContent());

        articleService.updateArticle(id, article);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiV2Resp> deleteArticle(@PathVariable("id") int id) throws IOException, CommonApiException {
        articleService.deleteArticle(id);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS));
    }
}
