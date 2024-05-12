package org.zerock.b01.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.zerock.b01.dto.AddressDTO;
import org.zerock.b01.dto.EmailCheckDTO;
import org.zerock.b01.dto.MemberDTO;
import org.zerock.b01.service.MailSendService;
import org.zerock.b01.service.MemberService;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/forgot")
public class MailController {
    private final MailSendService mailService;
    private final MemberService memberService;

    @PostMapping("/mailSend")
    public ModelAndView mailSend(@RequestParam String m_email, RedirectAttributes redirectAttributes) {
        try {
            mailService.joinEmail(m_email);
            redirectAttributes.addFlashAttribute("인증 번호 전송!");
            return new ModelAndView("redirect:/member/forgot");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("인증 번호 전송 실패");
            return new ModelAndView("redirect:/member/forgot");
        }
    }

    @PostMapping("/mailAuthCheck")
    public ModelAndView AuthCheck(@RequestParam String authNum, RedirectAttributes redirectAttributes) {
        Boolean Checked = mailService.CheckAuthNum(authNum);
        if (Checked) {
            redirectAttributes.addFlashAttribute("인증 번호 일치");
            return new ModelAndView("redirect:/member/changePw");
        } else {
            redirectAttributes.addFlashAttribute("인증 번호 불일치");
            return new ModelAndView("redirect:/member/forgot");
        }
    }
}
