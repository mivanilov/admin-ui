package org.mi.adminui.web.core.controller;

import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
public class AccessController {

    private static final String AUTH_URL = "authUrl";
    private static final String GOOGLE_AUTH_URL = "/oauth2/authorization/google";

    @RequestMapping(path = AppRoutes.ACCESS_LOGIN, method = {GET, POST, PUT, PATCH, DELETE})
    public String login(Model model) {
        model.addAttribute(AUTH_URL, GOOGLE_AUTH_URL);
        return AppPages.ACCESS_LOGIN;
    }

    @GetMapping(AppRoutes.ACCESS_DENIED)
    public String denied() {
        return AppPages.ACCESS_DENIED;
    }
}
