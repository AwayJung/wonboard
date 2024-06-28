//package kr.re.mydata.wonboard.service;
//
//import kr.re.mydata.wonboard.common.jwt.JwtUtil;
//import kr.re.mydata.wonboard.model.db.User;
//import kr.re.mydata.wonboard.dao.UserDAO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserDAO userDAO;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    // 회원가입
//    public User createUser(User user) throws Exception {
//        String password = user.getPassword();
//        String encodedPassword = passwordEncoder.encode(password);
//
//        user.setPassword(encodedPassword);
//
//        userDAO.createUser(user);
//
//        return user;
//    }
//
//    // 로그인
////    public Map<String, String> login(User user) throws Exception {
////        User userFromDB = userDAO.getUserByEmail(user.getLoginEmail());
////
////        if (userFromDB == null || !passwordEncoder.matches(user.getPassword(), userFromDB.getPassword())) {
////            throw new Exception("유효하지 않은 비밀번호 입니다.");
////        }
////
////        String accessToken = jwtUtil.createAccessToken(userFromDB.getLoginEmail());
////        String refreshToken = jwtUtil.createRefreshToken(userFromDB.getLoginEmail());
////
////        userFromDB.setRefreshToken(refreshToken);
////        userDAO.updateRefreshToken(userFromDB);
////
////        Map<String, String> tokens = new HashMap<>();
////        tokens.put("loginEmail", userFromDB.getLoginEmail());
////        tokens.put("accessToken", accessToken);
////        tokens.put("refreshToken", refreshToken);
////
////        return tokens;
////    }
//
//    // 토큰 재발급
//    public Map<String, String> refresh(Map<String, String> tokens) throws Exception {
//        String refreshToken = tokens.get("refreshToken");
//
//        // refreshToken 유효성 검사
//        if (!jwtUtil.isTokenValid(refreshToken)) {
//            throw new Exception("Invalid refresh token");
//        }
//
//        // refreshToken으로부터 loginEmail 가져오기
//        String loginEmail = jwtUtil.extractLoginEmail(refreshToken);
//
//        String storedRefreshToken = userDAO.getStoredRefreshToken(loginEmail);;
//
//        if(!refreshToken.equals(storedRefreshToken)){
//            throw new Exception("Invalid refresh token");
//        }
//        // 새로운 accessToken과 refreshToken 발급
//        String newAccessToken = jwtUtil.createAccessToken(loginEmail);
//        String newRefreshToken = jwtUtil.createRefreshToken(loginEmail);
//
//        // 새로 발급 받은 refreshToken을 DB에 저장
//        userDAO.storeRefreshToken(loginEmail, newRefreshToken);
//
//        // 새로 발급 받은 토큰들을 맵에 담아 반환
//        Map<String, String> newTokens = new HashMap<>();
//        newTokens.put("accessToken", newAccessToken);
//        newTokens.put("refreshToken", newRefreshToken);
//        return newTokens;
//    }
//
//    public User getUserByEmail(String loginEmail) {
//        return userDAO.getUserByEmail(loginEmail);
//    }
//}