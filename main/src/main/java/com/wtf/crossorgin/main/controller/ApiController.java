package com.wtf.crossorgin.main.controller;

import com.wtf.crossorgin.main.model.ResultModel;
import com.wtf.crossorgin.main.model.SaCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author wtf
 * @date 2023/8/17 10:58
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("set-cookie")
    public ResultModel setCookie(HttpServletResponse response) {
        SaCookie cookie = SaCookie.builder().name("token").value(UUID.randomUUID().toString()).httpOnly(true).maxAge(60 * 60 * 24 * 31).build();
        response.setHeader("Set-Cookie", cookie.toHeaderValue());
        return ResultModel.ok();
    }

    @GetMapping("set-cookie-same-site")
    public ResultModel setCookieSameSite(HttpServletResponse response) {
        SaCookie cookie = SaCookie.builder().name("token").value(UUID.randomUUID().toString()).httpOnly(true).maxAge(60 * 60 * 24 * 31).build();
        cookie.setSecure(true);
        cookie.setSameSite("None");
        response.setHeader("Set-Cookie", cookie.toHeaderValue());
        return ResultModel.ok();
    }

    @GetMapping("get-cookie")
    public ResultModel getCookie(@CookieValue(name = "token", required = false) String cookie) {
        return ResultModel.success(cookie);
    }
}
