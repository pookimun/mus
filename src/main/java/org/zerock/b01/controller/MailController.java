package org.zerock.b01.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.b01.dto.EmailCheckDTO;
import org.zerock.b01.dto.EmailRequestDTO;
import org.zerock.b01.service.MailSendService;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailSendService mailService;

    @PostMapping("/mailSend")
/*    public String mailSend(@RequestBody @Valid EmailRequestDTO emailDTO) {
        System.out.println("이메일 인증 이메일 :" + emailDTO.getM_email());
        return mailService.joinEmail(emailDTO.getM_email());
    }*/
    public ResponseEntity<String> mailSend(@RequestBody @Validated EmailRequestDTO emailDTO) {
        try {
            mailService.joinEmail(emailDTO.getM_email());
            return ResponseEntity.ok("이메일 전송 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이메일 전송 중 오류가 발생하였습니다.");
        }
    }

    @PostMapping("/mailauthCheck")
    public String AuthCheck(@RequestBody @Valid EmailCheckDTO emailCheckDTO) {
        Boolean Checked = mailService.CheckAuthNum(emailCheckDTO.getM_email(), emailCheckDTO.getAuthNum());
        if (Checked) {
            return "ok";
        } else {
            throw new NullPointerException("뭔가 잘못!");
        }
    }
}
