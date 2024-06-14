package kr.re.mydata.wonboard.model.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResp {

    private final Object data;
    private final String result;
    private final String msg;
    private final HttpStatus code;

    public ApiResp(Object data, String result, String msg, HttpStatus code) {
        this.data = data;
        this.result = result;
        this.msg = msg;
        this.code = code;
    }
}