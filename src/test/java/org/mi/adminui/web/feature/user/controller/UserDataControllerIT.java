package org.mi.adminui.web.feature.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mi.adminui.ControllerITBase;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.data.feature.user.service.UserService;
import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserDataControllerIT extends ControllerITBase {

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loadUsers() throws Exception {
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setName(USER_NAME);
        user.setRole(USER_ROLE);

        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(post(AppRoutes.USERS_LOAD))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(List.of(user))));
    }
}
