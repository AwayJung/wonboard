//package kr.re.mydata.wonboard.service;
//
//import kr.re.mydata.wonboard.common.config.UserDetail;
//import kr.re.mydata.wonboard.dao.ArticleDAO;
//import kr.re.mydata.wonboard.dao.AttachDAO;
//import kr.re.mydata.wonboard.model.db.Article;
//import kr.re.mydata.wonboard.model.db.Attach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//import java.util.UUID;
//
//
//@Service
//public class ArticleService {
//
//    @Autowired
//    private ArticleDAO articleDAO;
//
//    @Autowired
//    private AttachDAO attachDAO;
//
//
//    @Transactional
//    public Article postArticle(Article article, MultipartFile file) {
//        try {
//            System.out.println("postArticle called with article: " + article + " and file: " + file);
//
//
//            // principal을 통해 사용자 아이디를 가져옴
//            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            System.out.println("Principal: " + principal);
//
//            if (principal instanceof UserDetail) {
//                UserDetail userDetail = (UserDetail) principal;
//                System.out.println("Logged in user: " + userDetail.getUsername());
//                if (userDetail != null && userDetail.getUsername() != null) {
//                    if (article != null) {
//                        // principal을 통해 받아온 사용자 ID를 reg_user_id와 upd_user_id에 저장
//                        int userId = userDetail.getId();
//                        article.setRegUserId(userId);
//                        article.setUpdUserId(userId);
//                    } else {
//                        throw new IllegalStateException("Article is null");
//                    }
//                } else {
//                    throw new IllegalStateException("UserDetails is null or getUsername() is null");
//                }
//            } else {
//                throw new IllegalStateException("User is not logged in");
//            }
//
//            // Article 저장
//            int rowsAffected = articleDAO.postArticle(article);
//            if (rowsAffected != 1) {
//                throw new IllegalStateException("Article posting failed");
//            }
//                    System.out.println("Article posted with ID: " + article.getId());
//            System.out.println("Article posted: " + article);
//
//            // 첨부 파일 저장
//            if (file != null && !file.isEmpty()) {
//                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//                File serverFile = new File("D:/workspaces/spring/images/" + fileName);
//                file.transferTo(serverFile);
//
//                Attach attach = new Attach();
//                attach.setRealName(fileName);
//                attach.setName(file.getOriginalFilename());
//                attach.setPath(serverFile.getAbsolutePath());
//                attach.setPostId(article.getId()); // Article의 ID를 설정
//
//                attachDAO.postAttach(attach);
//                System.out.println("첨부파일: " + attach);
//            }
//
//            return article;
//        } catch (Exception e) {
//            System.out.println("Exception caught: " + e.getMessage());
//            throw new RuntimeException("Article posting failed", e);
//        }
//    }
//
//
//    public List<Article> getArticleList() {
//        return articleDAO.getArticleList();
//    }
//
//    public Article getArticle(int id) {
//        return articleDAO.getArticle(id);
//    }
//
//    public int updateArticle(int id, Article article, MultipartFile file) throws IOException {
//        int updatedArticle = articleDAO.update(id, article);
//        if (file != null && !file.isEmpty()) {
//            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//            File serverFile = new File("D:/workspaces/spring/images/" + fileName);
//            file.transferTo(serverFile);
//
//            Attach attach = new Attach();
//            attach.setRealName(fileName);
//            attach.setName(file.getOriginalFilename());
//            attach.setPath(serverFile.getAbsolutePath());
//            attach.setPostId(updatedArticle);
//
//            attachDAO.postAttach(attach);
//            System.out.println("첨부파일: " + attach);
//        }
//        return updatedArticle;
//    }
//
//    public int deleteArticle(int id) {
//        return articleDAO.delete(id);
//    }
//}
