package org.zerock.b01.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.EmailCheckDTO;
import org.zerock.b01.dto.EmailRequestDTO;
import org.zerock.b01.service.MailSendService;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/forgot")
public class MailController {
    private final MailSendService mailService;

    @PostMapping("/mailSend")
    public ResponseEntity<String> mailSend(@RequestParam String m_email) {
        log.info("@============메일 보내기 시도");
        try {
            mailService.joinEmail(m_email);
            return ResponseEntity.ok("이메일 전송 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이메일 전송 중 오류가 발생하였습니다.");
        }
    }

    @PostMapping("/mailAuthCheck")
    public String AuthCheck(@RequestBody @Valid EmailCheckDTO emailCheckDTO) {
        Boolean Checked = mailService.CheckAuthNum(emailCheckDTO.getM_email(), emailCheckDTO.getAuthNum());
        if (Checked) {
            return "ok";
        } else {
            throw new NullPointerException("뭔가 잘못!");
        }
    }
}
