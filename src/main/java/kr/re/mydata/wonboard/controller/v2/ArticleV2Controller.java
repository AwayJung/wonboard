package kr.re.mydata.wonboard.controller.v2;

import jakarta.validation.Valid;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.model.response.ApiV2Resp;
import kr.re.mydata.wonboard.model.db.Article;
import kr.re.mydata.wonboard.model.request.v2.ArticleV2Req;
import kr.re.mydata.wonboard.model.response.v2.ArticleV2Resp;
import kr.re.mydata.wonboard.model.response.v2.DetailV2Resp;
import kr.re.mydata.wonboard.model.response.v2.ListV2Resp;
import kr.re.mydata.wonboard.service.v2.ArticleV2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v2/articles")
public class ArticleV2Controller {
    private static final Logger logger = LoggerFactory.getLogger(ArticleV2Controller.class);

    @Autowired
    private ArticleV2Service articleService;

    @PostMapping("/")
    public ResponseEntity<ApiV2Resp> post(@RequestPart @Valid ArticleV2Req articleV2Req, @RequestPart("file") MultipartFile file) throws IOException, CommonApiException {
        Article article = new Article();
        article.setTitle(articleV2Req.getTitle());
        article.setContent(articleV2Req.getContent());

        articleService .post(article, file);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS_CREATED.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS_CREATED));
    }

    @GetMapping("/")
    public ResponseEntity<ApiV2Resp> getArticleList() throws CommonApiException {
        List<ListV2Resp> result= articleService.getList();
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS, result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiV2Resp> getArticle(@PathVariable("id") int id) throws CommonApiException {
        DetailV2Resp detail = articleService.getDetail(id);
//        DetailV2Resp detailV2Resp = new DetailV2Resp();
//
//        detailV2Resp.setTitle(article.getTitle());
//        detailV2Resp.setContent(article.getContent());
//        detailV2Resp.setWriter(article.getWriter());
//        detailV2Resp.setId(article.getId());

        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS, detail));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiV2Resp> updateArticle(@PathVariable("id") int postId, @RequestPart @Valid ArticleV2Req articleV2Req, @RequestPart(value = "newFile", required = false) MultipartFile newFile) throws CommonApiException {
        Article article = new Article();
        article.setTitle(articleV2Req.getTitle());
        article.setContent(articleV2Req.getContent());

        articleService.update(postId, article, newFile);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiV2Resp> deleteArticle(@PathVariable("id") int id) throws CommonApiException {
        articleService.delete(id);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS));
    }
}
