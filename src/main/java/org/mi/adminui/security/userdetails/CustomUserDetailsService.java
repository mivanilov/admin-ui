package org.mi.adminui.security.userdetails;

import org.mi.adminui.data.feature.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userService.find(email)
                          .map(CustomUserDetails::create)
                          .orElseThrow(() -> new UsernameNotFoundException("User not found by email: " + email));
    }
}
