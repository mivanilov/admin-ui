package org.mi.adminui.web.feature.user.controller;

import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.data.feature.user.service.UserService;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserDataController {

    private final UserService userService;

    public UserDataController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(AppRoutes.USERS_LOAD)
    public List<User> loadUsers() {
        return userService.findAll();
    }
}
