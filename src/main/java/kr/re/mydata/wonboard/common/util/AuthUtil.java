package kr.re.mydata.wonboard.common.util;

import kr.re.mydata.wonboard.common.config.UserDetail;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.exception.CommonApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 인증된 유저정보 유틸
 *
 * @author yrlee@mydata.re.kr
 */
public class AuthUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthUtil.class);

    /**
     * SecurityContext에 저장된 유저정보를 반환한다.
     *
     * @return SecurityContext에 저장된 UserDetails 객체
     * @throws CommonApiException with {@link ApiRespPolicy#ERR_USERDETAIL_NOT_FOUND}
     *              SecurityContext에 저장된 UserDetails 객체가 없을 때
     */
    public static UserDetail getUserDetail() throws Exception {
        UserDetail userDetail = null;
        try {
            userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userDetail == null) {
                throw new CommonApiException(ApiRespPolicy.ERR_USERDETAIL_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return userDetail;
    }

    /**
     * userName을 반환한다.
     *
     * @return 유저 로그인 이메일
     */
    public static String getUserName() throws Exception {
        String userName = null;
        try {
            userName = getUserDetail().getUsername();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return userName;
    }

    /**
     * userId를 반환한다.
     *
     * @return 유저 ID (데이터베이스 Primary Key)
     */
    public static int getUserId() throws Exception {
        Integer userId = null;
        try {
            userId = getUserDetail().getUserId();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return userId;
    }

}
