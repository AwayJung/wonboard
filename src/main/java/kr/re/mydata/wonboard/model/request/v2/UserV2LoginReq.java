package kr.re.mydata.wonboard.model.request.v2;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserV2LoginReq {
    @NotBlank
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String loginEmail;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
}
