package org.mi.adminui.web.core.controller;

import org.mi.adminui.web.core.configuration.constant.AppPages;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_MSG = "errorMsg";
    private static final String ERROR_UNKNOWN = "error.unknown";
    private static final String ERROR_404 = "error.404";

    @Override
    public String getErrorPath() {
        return AppRoutes.ERROR;
    }

    @RequestMapping(AppRoutes.ERROR)
    public String handleError(HttpServletRequest request, Model model) {
        model.addAttribute(ERROR_MSG, HttpStatus.NOT_FOUND.value() == getHttpStatusCode(request) ? ERROR_404 : ERROR_UNKNOWN);
        return AppPages.ERROR;
    }

    private int getHttpStatusCode(HttpServletRequest request) {
        return Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
                       .map(Object::toString)
                       .map(Integer::valueOf)
                       .orElse(0);
    }
}
