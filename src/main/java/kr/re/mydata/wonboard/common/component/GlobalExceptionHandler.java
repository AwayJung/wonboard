package kr.re.mydata.wonboard.common.component;

import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.model.response.ApiV2Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 예외 처리 핸들러
 *
 * @author yrlee@mydata.re.kr
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 특정되지 않은 모든 예외를 처리한다.
     *
     * @param e 예외 객체
     * @return HTTP 에러 응답
     * @see ApiRespPolicy
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiV2Resp> handleAllException(final Exception e) {
        return ResponseEntity.status(ApiRespPolicy.ERR_SYSTEM.getHttpStatus()).body(ApiV2Resp.withError(ApiRespPolicy.ERR_SYSTEM));
    }

    /**
     * API 요청 처리 시 발생하는 예외를 처리한다.
     *
     * @param e {@link CommonApiException} API 요청 처리 예외
     * @return HTTP 에러 응답
     * @see ApiRespPolicy
     */
    @ExceptionHandler(CommonApiException.class)
    public ResponseEntity<ApiV2Resp> handleCommonApiException(final CommonApiException e) {
        ApiRespPolicy apiRespPolicy = e.getApiRespPolicy();
        return ResponseEntity.status(apiRespPolicy.getHttpStatus()).body(ApiV2Resp.withError(apiRespPolicy));
    }

    /**
     * 매개변수 검증 예외를 처리한다.
     *
     * @param e {@link MethodArgumentNotValidException} 매개변수 검증 예외
     * @return HTTP 에러 응답
     * @see ApiRespPolicy#ERR_INVALID_PARAMS
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiV2Resp> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ResponseEntity.status(ApiRespPolicy.ERR_INVALID_PARAMS.getHttpStatus())
                .body(ApiV2Resp.withFieldErrors(ApiRespPolicy.ERR_INVALID_PARAMS, e.getBindingResult()));
    }

}
