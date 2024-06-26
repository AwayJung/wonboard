package kr.re.mydata.wonboard.controller.v2;

import jakarta.validation.Valid;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.model.response.ApiV2Resp;
import kr.re.mydata.wonboard.model.request.v2.UserV2LoginReq;
import kr.re.mydata.wonboard.model.request.v2.UserV2Req;
import kr.re.mydata.wonboard.model.response.v2.UserV2Resp;
import kr.re.mydata.wonboard.service.v2.UserV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/v2/users")
public class UserV2Controller {

    @Autowired
    private UserV2Service userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiV2Resp> signup(@RequestBody @Valid UserV2Req userReq) throws Exception {
        userService.createUser(userReq);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS_CREATED.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS_CREATED));
    }

    // Example: Return Response body
    @PostMapping("/signup-example")
    public ResponseEntity<ApiV2Resp> signupAndReturnResponseBody(@RequestBody @Valid UserV2Req userReq) throws Exception {
        UserV2Resp result = userService.createUserAndReturnResponseBody(userReq);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS_CREATED.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS_CREATED, result));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiV2Resp> login(@RequestBody @Valid UserV2LoginReq userReq) throws Exception {
        UserV2Resp result = userService.login(userReq);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS, result));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiV2Resp> refresh(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        UserV2Resp result = userService.refresh(refreshToken);
        return ResponseEntity.status(ApiRespPolicy.SUCCESS_ISSUE_TOKEN.getHttpStatus()).body(ApiV2Resp.of(ApiRespPolicy.SUCCESS_ISSUE_TOKEN, result));
    }

}