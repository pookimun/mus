package org.zerock.b01.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.MemberDTO;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.service.MemberService;
import org.zerock.b01.service.MemberServiceImpl;

import java.util.Random;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    public void joinGET(){

        log.info("join get...");

    }
    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes) {

        log.info("join post...");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.midExistException e) {

            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/member/login"; //회원 가입 후 로그인
    }


    @GetMapping("/login")
    public void loginGET(String error, String logout) {
        log.info("login get..............");
        log.info("logout: " + logout);
        log.info("error: " + error);

        if(logout != null) {
            log.info("user logout..........");
        }
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/member/login";
    }

    @GetMapping("/edit")
    public void editGet(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername(); // 현재 인증된 사용자의 아이디를 가져옴
        MemberDTO memberDTO = memberService.readMember(username);

        log.info(username);
        log.info(memberDTO);

        model.addAttribute("mid", username);
        model.addAttribute("dto", memberDTO);
    }

    @PostMapping("/edit")
    public void editPost(String id, Model model) {

/*        var checkDiv = document.getElementById("check_pw");

        var passwordInput = document.getElementById("current_pw");
            const confirmPasswordInput = [[${dto.m_pw}]]

        console.log(confirmPasswordInput)

        var password = passwordInput.value.trim();*/
    }

    @GetMapping("/forgot")
    public void forgotGet() {
    }
}
