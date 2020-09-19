package org.mi.adminui.web.core.controller;

import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(AppRoutes.HOME)
    public String home() {
        return AppPages.HOME;
    }
}
