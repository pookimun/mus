package org.zerock.b01.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.*;
import org.zerock.b01.service.AddressService;
import org.zerock.b01.service.CartService;
import org.zerock.b01.service.MemberService;
import org.zerock.b01.service.OrdersService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class DownloadController {

    @Value("${org.zerock.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    // 파일 다운로드 처리(파일명에 한글이 포함된 파일을 다운로드하면 콘솔에 오류는 출력되지만, 다운로드는 된다 ..
    @GetMapping("/download/{fileName}")
    public void fileDownload(@PathVariable String fileName,
                             HttpServletResponse response) throws IOException {
        File f = new File(uploadPath,  fileName);
        // file 다운로드 설정
        response.setContentType("application/download");
        response.setContentLength((int)f.length());
        response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + "\"");
        // response 객체를 통해서 서버로부터 파일 다운로드
        OutputStream os = response.getOutputStream();
        // 파일 입력 객체 생성
        FileInputStream fis = new FileInputStream(f); // 파일을 찾음
        FileCopyUtils.copy(fis, os);
        fis.close();
        os.close();
        log.info("다운로드 컨트롤러 끝");
    }

}
