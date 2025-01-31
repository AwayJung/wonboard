package kr.re.mydata.wonboard.service.v2;

import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.jwt.JwtUtil;
import kr.re.mydata.wonboard.dao.UserDAO;
import kr.re.mydata.wonboard.model.db.User;
import kr.re.mydata.wonboard.model.request.v2.UserV2LoginReq;
import kr.re.mydata.wonboard.model.request.v2.UserV2Req;
import kr.re.mydata.wonboard.model.response.v2.UserV2Resp;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 유저 서비스
 *
 * @author wjjung@mydata.re.kr
 */
@Service
public class UserV2Service {
    private static final Logger logger = LoggerFactory.getLogger(UserV2Service.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 회원가입
     *
     * @param userReq 유저정보 객체
     * @throws CommonApiException with {@link ApiRespPolicy#ERR_DUPLICATED_USER} 중복된 유저가 존재할 때
     */
    @Transactional
    public void createUser(UserV2Req userReq) throws Exception {
        try {
            User existUser = userDAO.getUserByEmail(userReq.getLoginEmail());
            if (existUser != null) {
                throw new CommonApiException(ApiRespPolicy.ERR_DUPLICATED_USER);
            }
            String encodedPassword = passwordEncoder.encode(userReq.getPassword());
            User user = new User();
            user.setName(userReq.getName());
            user.setLoginEmail(userReq.getLoginEmail());
            user.setPassword(encodedPassword);

            userDAO.createUser(user);
        } catch (Exception e) {
            // TODO: Logging 처리
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public UserV2Resp createUserAndReturnResponseBody(UserV2Req userReq) throws Exception {
        try {
            User existUser = userDAO.getUserByEmail(userReq.getLoginEmail());
            if (existUser != null) {
                throw new CommonApiException(ApiRespPolicy.ERR_DUPLICATED_USER);
            }
            String encodedPassword = passwordEncoder.encode(userReq.getPassword());
            User user = new User();
            user.setName(userReq.getName());
            user.setLoginEmail(userReq.getLoginEmail());
            user.setPassword(encodedPassword);

            userDAO.createUser(user);

            return modelMapper.map(user, UserV2Resp.class);
        } catch (Exception e) {
            // TODO: Logging 처리
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 로그인
     *
     * @param userReq 유저정보 객체
     * @throws CommonApiException with {@link ApiRespPolicy#ERR_NOT_AUTHENTICATED} 인증되지 않은 유저일 때
     */

    @Transactional
    public UserV2Resp login(UserV2LoginReq userReq) throws Exception {
        try {
            String loginEmail = userReq.getLoginEmail();// 현재 로그인 되어있는 사용자의 이메일임
            User userFromDB = userDAO.getUserByEmail(loginEmail); // DB에서 현재 로그인 되어있는 사용자 정보를 가져옴
            logger.info("userFromDB: {}", userFromDB);

            if (userFromDB == null || !passwordEncoder.matches(userReq.getPassword(), userFromDB.getPassword())) {
                throw new CommonApiException(ApiRespPolicy.ERR_NOT_AUTHENTICATED);
            }
            // access token, refresh token 발급
            String accessToken = jwtUtil.createAccessToken(userFromDB.getLoginEmail());
            String refreshToken = jwtUtil.createRefreshToken(userFromDB.getLoginEmail());

            // user객체에 refresh token 저장
            logger.info("전userFromDB: {}", userFromDB.getRefreshToken());
            userFromDB.setRefreshToken(refreshToken);
            logger.info("후userFromDB: {}", userFromDB.getRefreshToken());
            // DB에 저장
            int updateResult = userDAO.updateRefreshToken(userFromDB.getLoginEmail(), userFromDB.getRefreshToken());
            if(updateResult == 0) {
             logger.info("refresh token update 실패");
            }
            logger.info("userFromDB: {}", userFromDB.getLoginEmail());
            logger.info("Login Email: {}", loginEmail);
            logger.info("Access Token: {}", accessToken);
            logger.info("Refresh Token: {}", refreshToken);

            UserV2Resp userV2Resp = modelMapper.map(userFromDB, UserV2Resp.class);
            userV2Resp.setAccessToken(accessToken);
            userV2Resp.setRefreshToken(refreshToken);

            return userV2Resp;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 로그인
     *
     * @param refreshToken 리프레쉬토큰
     * @throws CommonApiException with {@link ApiRespPolicy#ERR_INVALID_REFRESH_TOKEN} 리프레쉬토큰이 유효하지 않을 때
     */

    @Transactional
    public UserV2Resp refresh(String refreshToken) throws Exception {
        try {
            String loginEmail = jwtUtil.extractLoginEmail(refreshToken);
            logger.info("Login Email: {}", loginEmail);

            String storedRefreshToken = userDAO.getStoredRefreshToken(loginEmail);
            logger.info("Stored Refresh Token: {}", storedRefreshToken);

            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                throw new CommonApiException(ApiRespPolicy.ERR_INVALID_REFRESH_TOKEN);
            }

            String newAccessToken = jwtUtil.createAccessToken(loginEmail);
            String newRefreshToken = jwtUtil.createRefreshToken(loginEmail);

            logger.info("New Access Token: {}", newAccessToken);
            logger.info("New Refresh Token: {}", newRefreshToken);

            userDAO.storeRefreshToken(loginEmail, newRefreshToken);

            UserV2Resp userV2Resp = new UserV2Resp();
            userV2Resp.setAccessToken(newAccessToken);
            userV2Resp.setRefreshToken(newRefreshToken);

            return userV2Resp;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
