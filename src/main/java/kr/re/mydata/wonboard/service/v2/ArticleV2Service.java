package kr.re.mydata.wonboard.service.v2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;

import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.util.AuthUtil;
import kr.re.mydata.wonboard.dao.ArticleDAO;
import kr.re.mydata.wonboard.dao.AttachDAO;
import kr.re.mydata.wonboard.dao.UserDAO;
import kr.re.mydata.wonboard.model.db.Article;
import kr.re.mydata.wonboard.model.db.Attach;
import kr.re.mydata.wonboard.model.request.v2.ArticleV2Req;
import kr.re.mydata.wonboard.model.response.v2.DeleteV2Resp;
import kr.re.mydata.wonboard.model.response.v2.DetailV2Resp;
import kr.re.mydata.wonboard.model.response.v2.ListV2Resp;
import kr.re.mydata.wonboard.model.response.v2.PageV2Resp;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;
/**
 * 게시글 서비스
 *
 * @author wjjung@mydata.re.kr
 */
@Service
public class ArticleV2Service {
    private static final Logger logger = LoggerFactory.getLogger(ArticleV2Service.class);

    @Autowired
    private ArticleDAO articleDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private AttachDAO attachDAO;

    // 경로는 분리해줘야함 -> application.properties에 설정한 경로를 가져와서 사용
    @Value("${app.config.file.path}")
    private String filePath;

    /**
     * 게시글 작성
     *
     * @param articleReq 게시글 정보
     * @param file 첨부파일(필수)
     * @throws Exception 게시 실패 시 예외 발생
     */
    @Transactional
    public void post(ArticleV2Req articleReq, MultipartFile file) throws Exception {
        try {
            int userId = AuthUtil.getUserId();

            Article article = new Article();
            article.setTitle(articleReq.getTitle());
            article.setContent(articleReq.getContent());
            article.setRegUserId(userId);
            article.setUpdUserId(userId);

            // Article 저장
            articleDAO.postArticle(article);

            // Attach 저장
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID().toString();
                File fileObject = new File(filePath + fileName);
                file.transferTo(fileObject);

                Attach attach = new Attach();
                attach.setRealName(file.getOriginalFilename());
                attach.setName(fileName);
                attach.setPath(filePath);
                attach.setPostId(article.getId());

                attachDAO.postAttach(attach);
                logger.info("Attach posted successfully: " + attach);
            }
        } catch (Exception e) {
            logger.error("Failed to post article", e);
            e.printStackTrace();
            throw e;
        }
    }

/*    @Transactional
    // CommonApiException은 한정적이기 때문에 Ex
    public void post(Article article, MultipartFile file) throws CommonApiException {
        // AuthUtil 을 사용해서 로그인한 사용자 정보를 가져와야함-> 분리해줘야함
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
                        throw new CommonApiException(ApiRespPolicy.ERR_USERDETAIL_NOT_FOUND);
                    }
                    int userId = user.getId();
                    logger.info("User ID: " + userId);

                    // 컨트롤러에서 @NotNull을 사용해서 null이 들어가지 않도록 하기 때문에 여기서는 체크하지 않음
                    if (article != null) {
                        article.setRegUserId(userId);
                        article.setUpdUserId(userId);
                    } else {
                        throw new CommonApiException(ApiRespPolicy.ERR_ARTICLE_NOT_FOUND);
                    }
                } else {
                    throw new CommonApiException(ApiRespPolicy.ERR_USERDETAIL_NOT_FOUND);
                }
                // 필요 없는 예외처리/ 로그인 되지 않은 사용자는 필터에서 처리(서비스단에서는 필요없음)
            } else {
                throw new CommonApiException(ApiRespPolicy.ERR_USER_NOT_LOGGED_IN);

            }

            // Article 저장
            // 필요없는 예외처리 -> 에러가 나면 여기서 걸리지 않고 바로 catch로 넘어감
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
    }*/
    /**
     * 게시글 목록 조회
     * 페이지 번호와 페이지당 항목 수를 기반으로 게시글 목록을 페이징하여 조회.
     * @param page 조회할 페이지 번호
     * @param size 페이지당 게시글 수
     * @return 게시글 목록 응답 객체
     */
    @Transactional(readOnly = true)
    public PageV2Resp getList(int page, int size) {
        try {
            int offset = (page - 1) * size;
            RowBounds rowBounds = new RowBounds(offset, size);
            List<ListV2Resp> articles = articleDAO.getList(rowBounds);
            int totalArticleCount = articleDAO.getTotalArticleCount();
            int totalPages = (int) Math.ceil((double) totalArticleCount / size);
            int limit = size;
            logger.info("Paging query executed with offset: {}, limit: {}, totalArticleCount: {}, totalPages: {} ", offset, limit, totalArticleCount, totalPages);

            return new PageV2Resp(articles, totalPages, limit, totalArticleCount);
        } catch (RuntimeException e) {
            logger.error("Failed to get article list", e);
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * 게시글 상세 조회
     * @param id 조회할 게시글의 ID
     * @return 게시글 상세 정보 응답 객체
     * @throws CommonApiException 조회 실패 시 예외 발생
     */
    @Transactional(readOnly = true)
    public DetailV2Resp getDetail(int id) throws CommonApiException {
        try {
            DetailV2Resp detail = articleDAO.getDetail(id);
//           Attach attach = attachDAO.getAttach(id);
//            if (attach != null) {
//                detail.setPath(attach.getPath());
//            }
            return detail;
//        try {
//            return articleDAO.getDetail(id);
        } catch (Exception e) {
            logger.error("Failed to get article", e);
            e.printStackTrace();
            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);
        }
    }
    /**
     * 게시글 업데이트
     * 새로운 첨부파일이 제공될 경우 기존 첨부파일을 대체.
     * @param postId 업데이트할 게시글의 ID
     * @param articleV2Req 업데이트할 게시글 정보
     * @param newFile 새로운 첨부파일 (선택적)
     */

    @Transactional
    public void update(int postId, @Valid @NotNull ArticleV2Req articleV2Req, MultipartFile newFile) throws Exception {
        try {
            int modUserId = AuthUtil.getUserId();

                Article article = new Article();
                article.setTitle(articleV2Req.getTitle());
                article.setContent(articleV2Req.getContent());
                article.setRegUserId(modUserId);
                article.setUpdUserId(modUserId);

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
                File serverFile = new File(filePath + fileName);
                newFile.transferTo(serverFile);

                Attach newAttach = new Attach();
                newAttach.setRealName(newFile.getOriginalFilename());
                newAttach.setName(fileName);
                newAttach.setPath(filePath);
                newAttach.setPostId(postId);

                attachDAO.postAttach(newAttach);
                logger.info("Attach posted successfully" + newAttach);
            }

            articleDAO.update(postId, article);
            logger.info("Article updated successfully: " + article);
        } catch (Exception e) {
            logger.error("Failed to update article", e);
            throw e;
        }
    }
    /**
     * 게시글 삭제
     * @param id 삭제할 게시글의 ID
     * @throws CommonApiException
     *
     */
    @Transactional
    public void delete(int id) throws Exception {
        try {
            String currentUserId = AuthUtil.getUserName();
            DeleteV2Resp article = articleDAO.getDeleteDetail(id);
//            if (article == null) {
//                throw new CommonApiException(ApiRespPolicy.ERR_ARTICLE_NOT_FOUND);
//            }
            if (article.getLoginEmail().equals(currentUserId)) {
                articleDAO.delete(id);
            }
        }catch (Exception e) {
            logger.error("Failed to delete article", e);
            e.printStackTrace();
            throw e;
//            throw new CommonApiException(ApiRespPolicy.ERR_SYSTEM);
        }
    }
//    public Resource loadAsResource(String filename) {
//        try {
//            Path file = Paths.get("D:/workspaces/spring/images/" + filename);
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new RuntimeException("Could not read file: " + filename);
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Could not read file: " + filename, e);
//        }
//    }
}
