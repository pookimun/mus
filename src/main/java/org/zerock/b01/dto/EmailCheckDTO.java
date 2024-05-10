package org.zerock.b01.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailCheckDTO {
    @Email
    @NotEmpty(message = "이메일을 입력해 주세요 쩨바류~")
    private String m_email;

    @NotEmpty(message = "인증 번호를 입력해 주세요 쩨바류~")
    private String authNum;
}
