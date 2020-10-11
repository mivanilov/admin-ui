package org.mi.adminui.security.userdetails;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.data.feature.user.service.UserService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    private static final String EMAIL = "EMAIL";
    private static final String NAME = "NAME";
    private static final User.RoleType ROLE = User.RoleType.ADMIN;

    @Mock
    private UserService userService;

    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByEmail() {
        User user = new User(EMAIL, NAME, ROLE);
        when(userService.find(EMAIL)).thenReturn(Optional.of(user));

        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(EMAIL);

        assertAll(
                () -> assertSame(NAME, customUserDetails.getName()),
                () -> assertSame(EMAIL, customUserDetails.getEmail()),
                () -> assertEquals(1, customUserDetails.getAuthorities().size()),
                () -> assertSame(ROLE.getSecurityName(), customUserDetails.getAuthorities().iterator().next().getAuthority())
        );
    }

    @Test
    void throwExceptionWhenUserNotFoundByEmail() {
        when(userService.find(EMAIL)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(EMAIL));
    }
}
