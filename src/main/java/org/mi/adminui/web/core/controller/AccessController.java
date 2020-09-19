package org.mi.adminui.web.core.controller;

import org.mi.adminui.util.CookieUtils;
import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.core.configuration.property.AppAuthProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

import static org.mi.adminui.web.core.configuration.constant.AppSecurity.TOKEN;
import static org.mi.adminui.web.core.configuration.constant.AppSecurity.USER_JWT_COOKIE;

@Controller
public class AccessController {

    static final String AUTH_URL = "authUrl";
    static final String GOOGLE_AUTH_URL = "/oauth2/authorization/google";

    private final AppAuthProperties appAuthProperties;
    private final CookieUtils cookieUtils;

    public AccessController(AppAuthProperties appAuthProperties,
                            CookieUtils cookieUtils) {
        this.appAuthProperties = appAuthProperties;
        this.cookieUtils = cookieUtils;
    }

    @GetMapping(AppRoutes.ACCESS_LOGIN)
    public String login(Model model) {
        model.addAttribute(AUTH_URL, GOOGLE_AUTH_URL);

        return AppPages.ACCESS_LOGIN;
    }

    @GetMapping(AppRoutes.ACCESS_DENIED)
    public String denied() {
        return AppPages.ACCESS_DENIED;
    }

    @GetMapping(AppRoutes.ACCESS_LOGIN_SUCCESS)
    public String loginSuccess(@RequestParam(TOKEN) String token, HttpServletResponse response) {
        cookieUtils.addCookie(response,
                              USER_JWT_COOKIE,
                              cookieUtils.serialize(token),
                              appAuthProperties.getCookie().getExpireSec());

        return "redirect:" + AppRoutes.HOME;
    }
}
