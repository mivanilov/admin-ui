package org.mi.adminui.web.core.controller;

import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessController {

    private static final String AUTH_URL = "authUrl";
    private static final String GOOGLE_AUTH_URL = "/oauth2/authorization/google";

    @GetMapping(AppRoutes.ACCESS_LOGIN)
    public String login(Model model) {
        model.addAttribute(AUTH_URL, GOOGLE_AUTH_URL);
        return AppPages.ACCESS_LOGIN;
    }

    @GetMapping(AppRoutes.ACCESS_DENIED)
    public String denied() {
        return AppPages.ACCESS_DENIED;
    }
}
