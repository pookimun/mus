package org.zerock.b01.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.zerock.b01.dto.EmailCheckDTO;
import org.zerock.b01.service.MailSendService;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/forgot")
public class MailController {
    private final MailSendService mailService;

    @PostMapping("/mailSend")
    public ModelAndView mailSend(@RequestParam String m_email) {
        try {
            mailService.joinEmail(m_email);
            return new ModelAndView("redirect:/member/forgot");
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/member/forgot");
        }
    }

    @PostMapping("/mailAuthCheck")
    public String AuthCheck(@RequestParam @Valid EmailCheckDTO emailCheckDTO) {
        Boolean Checked = mailService.CheckAuthNum(emailCheckDTO.getM_email(), emailCheckDTO.getAuthNum());
        if (Checked) {
            return "ok";
        } else {
            throw new NullPointerException("뭔가 잘못!");
        }
    }
}
