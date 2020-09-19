package org.mi.adminui.data.feature.user.service;

import org.mi.adminui.data.core.service.AbstractCrudDataService;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.data.feature.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserService extends AbstractCrudDataService<User, String> {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
}
