//package kr.re.mydata.wonboard.controller;
//
//import kr.re.mydata.wonboard.model.db.Article;
//import kr.re.mydata.wonboard.service.ArticleService;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import kr.re.mydata.wonboard.model.common.ApiResp;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RequestMapping("/article")
//
//@RestController
//public class ArticleController {
//
//    @Autowired
//    private ArticleService articleService;
//
//
//    @PostMapping("/")
//    public ResponseEntity<ApiResp> postArticle(@RequestPart("article") Article article, @RequestPart("file") MultipartFile file) {
//        try {
//            System.out.println("컨트롤러에서 도착" + article.getTitle() + article.getContent() + file.getOriginalFilename());
//            Article postArticle = articleService.postArticle(article, file);
//            ApiResp apiResp = new ApiResp(postArticle, "success", "글 작성 성공", HttpStatus.OK);
//            return ResponseEntity.ok(apiResp);
//        } catch (RuntimeException e) {
//            System.out.println("컨트롤러에서 난 오류임" + e.getMessage());
//            ApiResp apiResp = new ApiResp(null, "failure", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResp);
//        }
//    }
//
////    @GetMapping("/list")
////    public ResponseEntity<ApiResp> getArticleList() {
////        try {
////            return ResponseEntity.ok(new ApiResp(articleService.getArticleList(), "success", "글 목록 조회 성공", HttpStatus.OK));
////        } catch (RuntimeException e) {
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResp(null, "글 목록 조회 실패", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
////        }
////    }
////
////    @GetMapping("/{id}")
////    public ResponseEntity<ApiResp> getArticle(@PathVariable("id") int id) {
////        try {
////            return ResponseEntity.ok(new ApiResp(articleService.getArticle(id), "success", "글 조회 성공", HttpStatus.OK));
////        } catch (RuntimeException e) {
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResp(null, "글 조회 실패", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
////        }
////    }
////
////    @PutMapping("/{id}")
////    public ResponseEntity<ApiResp> updateArticle(@PathVariable("id") int id, @RequestPart("article") Article article, @RequestPart(value = "file", required = false) MultipartFile file) {
////        try {
////            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////            if (auth.getPrincipal() instanceof UserDetails) {
////                UserDetails userDetails = (UserDetails) auth.getPrincipal();
////                String currentUserId = userDetails.getUsername();
////                System.out.println("Current user id: " + currentUserId);
////                article.setUpdUserId(currentUserId);
////            } else {
////                System.out.println("The principal is not an instance of UserDetails.");
////            }
////            return ResponseEntity.ok(new ApiResp(articleService.updateArticle(id, article, file), "success", "글 수정 성공", HttpStatus.OK));
////        } catch (RuntimeException e) {
////            e.printStackTrace();  // 예외의 스택 트레이스를 출력합니다.
////            System.out.println("RuntimeException occurred in updateArticle: " + e.getMessage());
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResp(null, "글 수정 실패", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
////        } catch (IOException e) {
////            System.out.println("IOException occurred in updateArticle: " + e.getMessage());
////            throw new RuntimeException(e);
////        }
////    }
////
////    @DeleteMapping("/{id}")
////    public ResponseEntity<ApiResp> deleteArticle(@PathVariable("id") int id) {
////        try {
////            return ResponseEntity.ok(new ApiResp(articleService.deleteArticle(id), "success", "글 삭제 성공", HttpStatus.OK));
////        } catch (RuntimeException e) {
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResp(null, "글 삭제 실패", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
////        }
////    }
//}
