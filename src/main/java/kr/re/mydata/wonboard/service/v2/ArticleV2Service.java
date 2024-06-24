package kr.re.mydata.wonboard.service.v2;

import kr.re.mydata.wonboard.common.config.UserDetail;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;

import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.jwt.JwtUtil;
import kr.re.mydata.wonboard.dao.ArticleDAO;
import kr.re.mydata.wonboard.dao.AttachDAO;
import kr.re.mydata.wonboard.model.db.Article;
import kr.re.mydata.wonboard.model.db.Attach;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ArticleV2Service {
    private static final Logger logger = LoggerFactory.getLogger(ArticleV2Service.class);

    @Autowired
    private ArticleDAO articleDAO;

    @Autowired
    private AttachDAO attachDAO;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public Article postArticle(Article article, MultipartFile file) throws IOException, CommonApiException {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String token = (String) auth.getCredentials();
            logger.info("Principal: " + principal);
            if (principal instanceof UserDetail) {
                UserDetail userDetail = (UserDetail) principal;
                logger.info("userDetail: " + userDetail.getUsername());
                if (userDetail != null && userDetail.getUsername() != null) {
//                    if (!jwtUtil.isTokenValid(token)) {
//                        throw new CommonApiException(ApiRespPolicy.ERR_TOKEN_EXPIRED);
//                    }
                    if (article != null) {
                        // principal을 통해 받아온 이메일을 reg_user_id 저장
                        article.setRegUserId(userDetail.getUsername());
                        article.setUpdUserId(userDetail.getUsername());

                        if(article.getTitle().length() > 100 || article.getContent().length() > 1000) {
                            throw new CommonApiException(ApiRespPolicy.ERR_ARTICLE_NULL);
                        }
                    } else {
                        throw new CommonApiException(ApiRespPolicy.ERR_ARTICLE_NULL);
                    }
                } else {
                    throw new CommonApiException(ApiRespPolicy.ERR_USERDETAIL_NULL);
                }
            } else {
                throw new CommonApiException(ApiRespPolicy.ERR_USER_NOT_LOGGED_IN);
            }

            // Article 저장
            int rowsAffected = articleDAO.postArticle(article);
            if (rowsAffected != 1) {
                throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);
            }
            logger.info("Article posted ID: " + article.getId());
            logger.info("Article posted successfully" + article);

            // Attach 저장
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                File serverFile = new File("D:/workspaces/spring/images/" + fileName);
                file.transferTo(serverFile);

                Attach attach = new Attach();
                attach.setRealName(file.getOriginalFilename());
                attach.setName(fileName);
                attach.setPath(serverFile.getAbsolutePath());
                attach.setPostId(article.getId());

                attachDAO.postAttach(attach);
                logger.info("Attach posted successfully" + attach);
            }
            return article;
        } catch (Exception e) {
            logger.error("Failed to post article", e);
            e.printStackTrace();
            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);
        }
    }

    @Transactional
    public List getArticleList() throws CommonApiException {
        try {
            return articleDAO.getArticleList();

        } catch (Exception e) {
            logger.error("Failed to get article list", e);
            e.printStackTrace();
            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);

        }
    }

    @Transactional
    public Article getArticle(int id) throws CommonApiException {
        try {
            return articleDAO.getArticle(id);
        } catch (Exception e) {
            logger.error("Failed to get article", e);
            e.printStackTrace();
            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);
        }
    }

    @Transactional
    public int updateArticle(int id, Article article) throws CommonApiException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                String currentUserId = userDetails.getUsername();
                logger.info("Current user id: " + currentUserId);
                article.setUpdUserId(currentUserId);
                if (article.getTitle().length() > 100 || article.getContent().length() > 1000) {
                    throw new CommonApiException(ApiRespPolicy.ERR_TEXT_LENGTH_EXCEEDED);
                }
            } else {
                throw new CommonApiException(ApiRespPolicy.ERR_USER_NOT_LOGGED_IN);
            }

            return articleDAO.updateArticle(id, article);
        } catch (CommonApiException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update article", e);
            throw new CommonApiException(ApiRespPolicy.ERR_DATABASE_NULL);
        }
    }

    @Transactional
    public int deleteArticle(int id) throws CommonApiException {
        try {
            String username;
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                username = userDetails.getUsername();
            } else {
                throw new CommonApiException(ApiRespPolicy.ERR_USER_NOT_LOGGED_IN);
            }
            Article article = articleDAO.getArticle(id);
            if (article == null) {
                throw new CommonApiException(ApiRespPolicy.ERR_ARTICLE_NULL);
            }
            if (!article.getRegUserId().equals(username)) {
                throw new CommonApiException(ApiRespPolicy.ERR_USER_NOT_LOGGED_IN);
            }
            return articleDAO.deleteArticle(id);
        }catch (Exception e) {
            logger.error("Failed to delete article", e);
            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);
        }
    }
}
