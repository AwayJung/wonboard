package kr.re.mydata.wonboard.controller.v2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.model.response.ApiV2Resp;
import kr.re.mydata.wonboard.model.db.Article;
import kr.re.mydata.wonboard.model.db.Attach;
import kr.re.mydata.wonboard.model.request.v2.ArticleV2Req;
import kr.re.mydata.wonboard.model.response.v2.ArticleV2Resp;
import kr.re.mydata.wonboard.model.response.v2.DetailV2Resp;
import kr.re.mydata.wonboard.model.response.v2.ListV2Resp;
import kr.re.mydata.wonboard.model.response.v2.PageV2Resp;
import kr.re.mydata.wonboard.service.v2.ArticleV2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
/**
 * 게시글 관련 요청을 처리하는 컨트롤러
 *  @author wjjung@mydata.re.kr
 */

@RestController
@RequestMapping("/v2/articles")
public class ArticleV2Controller {

    @Autowired
    private ArticleV2Service articleService;

    /**
     * 게시글 생성
     * @param articleV2Req 게시글 정보 객체
     * @param file 첨부 파일
     * @return 생성 성공 응답
     */
    @PostMapping("/")
    public ResponseEntity<ApiV2Resp> post(@RequestPart @Valid @NotNull ArticleV2Req articleV2Req, @RequestPart("file") @NotNull MultipartFile file) throws Exception {
        articleService .post(articleV2Req, file);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS_CREATED.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS_CREATED));
    }

    /**
     * 게시글 목록 조회
     * @param page 페이지 번호
     * @param size 페이지 당 게시글 수
     * @return 조회된 게시글 목록, 페이징 정보
     */
    @GetMapping("/")
    public ResponseEntity<ApiV2Resp> getArticleList(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        PageV2Resp result = articleService.getList(page, size);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS, result));
    }

    /**
     * 특정 게시글을 조회
     * @param id 게시글 ID
     * @return 조회된 게시글 상세 정보 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiV2Resp> getArticle(@PathVariable("id") int id) throws CommonApiException {
        DetailV2Resp detail = articleService.getDetail(id);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS, detail));
    }

    /**
     * 게시글 업데이트
     * @param postId 업데이트할 게시글의 ID
     * @param articleV2Req 업데이트할 게시글 정보
     * @param newFile 새로운 첨부 파일 (선택)
     * @return 업데이트 성공 응답 반환
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiV2Resp> updateArticle(@PathVariable("id") int postId, @RequestPart @Valid @NotNull ArticleV2Req articleV2Req, @RequestPart(value = "newFile", required = false) MultipartFile newFile) throws Exception {
//        Article article = new Article();
//        article.setTitle(articleV2Req.getTitle());
//        article.setContent(articleV2Req.getContent());

        articleService.update(postId, articleV2Req, newFile);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS));
    }

    /**
     * 게시글 삭제
     * @param id 삭제할 게시글의 ID
     * @return 삭제 성공 응답 반환
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiV2Resp> deleteArticle(@PathVariable("id") int id) throws Exception {
        articleService.delete(id);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS));
    }
//
//    @GetMapping("/upload/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<Resource> serverFile(@PathVariable String filename){
//        Resource file = articleService.loadAsResource(filename);
//        return ResponseEntity.ok().body(file);
//    }
}

