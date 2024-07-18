package kr.re.mydata.wonboard.common.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.util.MessageUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * API 공통 응답 모델
 *
 * @author yrlee@mydata.re.kr
 */
@Data
public class ApiV2Resp {

    // 처리 결과 코드
    private int code;
    // 처리 결과 코드명
    private String codeName;
    // 메시지
    private String message;
    // 응답 데이터
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;
    // 에러 목록
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Object> errors;

    public static ApiV2Resp of(final ApiRespPolicy apiRespPolicy) {
        return new ApiV2Resp(apiRespPolicy, null);
    }
    // 성공 시
    public static ApiV2Resp of(final ApiRespPolicy apiRespPolicy, final Object data) {
        return new ApiV2Resp(apiRespPolicy, data);
    }

    // 오류 발생 시
    public static ApiV2Resp withError(final ApiRespPolicy apiRespPolicy) {
        return new ApiV2Resp(apiRespPolicy);
    }

    // 오류 발생 시 (with 다중 에러)
    public static ApiV2Resp withFieldErrors(final ApiRespPolicy apiRespPolicy, final BindingResult bindingResult) {
        return new ApiV2Resp(apiRespPolicy, null, FieldError.of(bindingResult));
    }


    private ApiV2Resp(final ApiRespPolicy apiRespPolicy) {
        this(apiRespPolicy, null);
    }

    private ApiV2Resp(final ApiRespPolicy apiRespPolicy, final Object data) {
        this(apiRespPolicy, data, null);
    }

    private ApiV2Resp(final ApiRespPolicy apiRespPolicy, final Object data, final List<FieldError> errors) {
        this.code = apiRespPolicy.getCode();
        this.codeName = apiRespPolicy.name();
        this.message = MessageUtil.getMessage(apiRespPolicy.getMessageKey());
        this.data = data;
        this.errors = CollectionUtils.isEmpty(errors) ? null : Collections.singletonList(errors);
    }

    @Getter
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

        @Builder
        FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}