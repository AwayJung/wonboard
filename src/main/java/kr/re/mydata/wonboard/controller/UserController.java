//package kr.re.mydata.wonboard.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import kr.re.mydata.wonboard.common.util.EmailValidator;
//import kr.re.mydata.wonboard.model.common.ApiResp;
//import kr.re.mydata.wonboard.model.db.User;
//import kr.re.mydata.wonboard.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    // 회원가입
//    @PostMapping("/signup")
//    public ResponseEntity<ApiResp> signup(@RequestBody User user)  {
//        String email = user.getLoginEmail();
//        if(!EmailValidator.isValidEmail(email)){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResp(null, "error", "이메일형식이 아닙니다.", HttpStatus.BAD_REQUEST));
//        }
//        try{
//        User createdUser = userService.createUser(user);
//            return ResponseEntity.ok(new ApiResp(createdUser, "success", "회원가입 성공", HttpStatus.OK));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResp(null, "error", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
//        }
//    }
//    // 로그인
//    @PostMapping("/login")
//    public ResponseEntity<ApiResp> login(@RequestBody User user) {
//        String loginEmail = user.getLoginEmail();
//        if(!EmailValidator.isValidEmail(loginEmail)){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResp(null, "error", "이메일형식이 아닙니다.", HttpStatus.BAD_REQUEST));
//        }
//        try {
//            Map<String, String> tokens = userService.login(user);
//            return ResponseEntity.ok(new ApiResp(tokens, "success", "로그인 성공", HttpStatus.OK));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResp(null, "error", "아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED));
//        }
//    }
//
//    // 토큰 재발급
//    @PostMapping("/refresh")
//    public ResponseEntity<ApiResp> refresh(HttpServletRequest request) {
//        try {
//            String accessToken = request.getHeader("Authorization");
//            String refreshToken = request.getHeader("refreshToken");
//
//            if (accessToken != null && accessToken.startsWith("Bearer ")) {
//                accessToken = accessToken.substring(7);
//            }
//
//            Map<String, String> tokens = new HashMap<>();
//            tokens.put("accessToken", accessToken);
//            tokens.put("refreshToken", refreshToken);
//
//            Map<String, String> newTokens = userService.refresh(tokens);
//            return ResponseEntity.ok(new ApiResp(newTokens, "success", "토큰 재발급 성공", HttpStatus.OK));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResp(null, "error", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED));
//        }
//    }
//}