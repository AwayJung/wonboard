package kr.re.mydata.wonboard.service.v2;

import kr.re.mydata.wonboard.common.config.UserDetail;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;

import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.jwt.JwtUtil;
import kr.re.mydata.wonboard.dao.ArticleDAO;
import kr.re.mydata.wonboard.dao.AttachDAO;
import kr.re.mydata.wonboard.dao.UserDAO;
import kr.re.mydata.wonboard.model.db.Article;
import kr.re.mydata.wonboard.model.db.Attach;
import kr.re.mydata.wonboard.model.db.User;
import kr.re.mydata.wonboard.model.response.v2.DeleteV2Resp;
import kr.re.mydata.wonboard.model.response.v2.DetailV2Resp;
import kr.re.mydata.wonboard.model.response.v2.ListV2Resp;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
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
    private UserDAO userDAO;
    @Autowired
    private AttachDAO attachDAO;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public void post(Article article, MultipartFile file) throws CommonApiException {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logger.info("Principal: " + principal);
            if (principal instanceof UserDetail) {
                UserDetail userDetail = (UserDetail) principal;
                logger.info("userDetail: " + userDetail.getUsername());
                if (userDetail != null && userDetail.getUsername() != null) {
                    String loginEmail = userDetail.getUsername();
                    User user = userDAO.getUserByEmail(loginEmail);
                    if (user == null) {
                        throw new CommonApiException(ApiRespPolicy.ERR_USERDETAIL_NULL);
                    }
                    int userId = user.getId();
                    logger.info("User ID: " + userId);
                    if (article != null) {
                        article.setRegUserId(userId);
                        article.setUpdUserId(userId);
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
//            return article;
        } catch (Exception e) {
            logger.error("Failed to post article", e);
            e.printStackTrace();
            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);
        }
    }

    @Transactional(readOnly = true)
    public List<ListV2Resp> getList() throws CommonApiException {
        try {
            return articleDAO.getList();

        } catch (Exception e) {
            logger.error("Failed to get article list", e);
            e.printStackTrace();
            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);

        }
    }

    @Transactional(readOnly = true)
    public DetailV2Resp getDetail(int id) throws CommonApiException {
        try {
            return articleDAO.getDetail(id);
        } catch (Exception e) {
            logger.error("Failed to get article", e);
            e.printStackTrace();
            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);
        }
    }

    @Transactional
    public void update(int postId, @RequestBody Article article, MultipartFile newFile) throws CommonApiException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                String currentUserId = userDetails.getUsername();
                logger.info("Current user id: " + currentUserId);
                User user = userDAO.getUserByEmail(currentUserId);

                int modUserId = user.getId();

                article.setUpdUserId(modUserId);
            } else {
                throw new CommonApiException(ApiRespPolicy.ERR_USER_NOT_LOGGED_IN);
            }

            // newFile이 있으면 기존 첨부파일 삭제 후 새로운 첨부파일 저장
            if (newFile != null && !newFile.isEmpty()) {
                // 기존 첨부파일 삭제
                Attach existingAttach = attachDAO.getAttach(postId);
                if (existingAttach != null) {
                    File existingFile = new File(existingAttach.getPath());
                    if (existingFile.exists()) {
                        existingFile.delete();
                    }
                    attachDAO.deleteAttach(postId);
                }

                // 새로운 첨부파일 저장
                String fileName = UUID.randomUUID().toString() + "_" + newFile.getOriginalFilename();
                File serverFile = new File("D:/workspaces/spring/images/" + fileName);
                newFile.transferTo(serverFile);

                Attach newAttach = new Attach();
                newAttach.setRealName(newFile.getOriginalFilename());
                newAttach.setName(fileName);
                newAttach.setPath(serverFile.getAbsolutePath());
                newAttach.setPostId(postId);

                attachDAO.postAttach(newAttach);
                logger.info("Attach posted successfully" + newAttach);
            }

            articleDAO.update(postId, article);
        } catch (CommonApiException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update article", e);
            throw new CommonApiException(ApiRespPolicy.ERR_DATABASE_NULL);
        }
    }

    @Transactional
    public void delete(int id) throws CommonApiException {
        try {
            String userName = null;
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                userName = userDetails.getUsername();
                logger.info("userName: " + userName);

            } else {
                throw new CommonApiException(ApiRespPolicy.ERR_USER_NOT_LOGGED_IN);
            }
            DeleteV2Resp article = articleDAO.getDeleteDetail(id);
            logger.info("writer: " + article.getLoginEmail());

            if (article == null) {
                throw new CommonApiException(ApiRespPolicy.ERR_ARTICLE_NULL);
            }
            if (!article.getLoginEmail().equals(userName)) {
                throw new CommonApiException(ApiRespPolicy.ERR_USER_NOT_LOGGED_IN);
            }
            articleDAO.delete(id);
        }catch (Exception e) {
            logger.error("Failed to delete article", e);
            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);
        }
    }
}
