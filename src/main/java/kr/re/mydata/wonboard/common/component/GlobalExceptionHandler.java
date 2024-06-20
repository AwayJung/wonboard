package kr.re.mydata.wonboard.common.component;

import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.model.response.ApiV2Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiV2Resp> handleAllException(final Exception e) {
        return ResponseEntity.status(ApiRespPolicy.ERR_SYSTEM.getHttpStatus()).body(ApiV2Resp.withError(ApiRespPolicy.ERR_SYSTEM));
    }

    @ExceptionHandler(CommonApiException.class)
    public ResponseEntity<ApiV2Resp> handleCommonApiException(final CommonApiException e) {
        ApiRespPolicy apiRespPolicy = e.getApiRespPolicy();
        return ResponseEntity.status(apiRespPolicy.getHttpStatus()).body(ApiV2Resp.withError(apiRespPolicy));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiV2Resp> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ResponseEntity.status(ApiRespPolicy.ERR_INVALID_PARAMS.getHttpStatus())
                .body(ApiV2Resp.withFieldErrors(ApiRespPolicy.ERR_INVALID_PARAMS, e.getBindingResult()));
    }

}
