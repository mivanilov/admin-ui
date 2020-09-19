package org.mi.adminui.data.feature.user.repository;

import org.junit.jupiter.api.Test;
import org.mi.adminui.Application;
import org.mi.adminui.data.feature.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {Application.class})
@ActiveProfiles("test")
@Transactional
class UserRepositoryIT {

    @Autowired
    private UserRepository repository;

    @Test
    void findAllUsers() {
        User adminUser = insertUser("admin@gmail.com", "admin", User.RoleType.ADMIN);
        User businessUser = insertUser("business@gmail.com", "business", User.RoleType.BUSINESS);

        List<User> users = repository.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertThat(users).contains(adminUser, businessUser);
    }

    @Test
    void noUsersFound() {
        List<User> users = repository.findAll();

        assertTrue(users.isEmpty());
    }

    @Test
    void findUserById() {
        User adminUser = insertUser("admin@gmail.com", "admin", User.RoleType.ADMIN);

        Optional<User> foundUser = repository.findById("admin@gmail.com");

        assertTrue(foundUser.isPresent());
        assertEquals(adminUser, foundUser.get());
    }

    @Test
    void userNotFound() {
        Optional<User> foundUser = repository.findById("admin@gmail.com");

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void createUser() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setName("admin");
        user.setRole(User.RoleType.ADMIN);

        User createdUser = repository.save(user);

        assertNotNull(createdUser);
        assertEquals(user, createdUser);
    }

    @Test
    void updateUser() {
        User user = insertUser("user@gmail.com", "admin", User.RoleType.ADMIN);
        String name = "business";
        user.setName(name);
        user.setRole(User.RoleType.BUSINESS);

        User updatedUser = repository.save(user);

        assertNotNull(updatedUser);
        assertAll("User data",
                  () -> assertEquals(user.getEmail(), updatedUser.getEmail()),
                  () -> assertEquals(name, updatedUser.getName()),
                  () -> assertEquals(User.RoleType.BUSINESS, updatedUser.getRole())
        );
    }

    @Test
    void deleteUser() {
        User user = insertUser("admin@gmail.com", "admin", User.RoleType.ADMIN);

        repository.deleteById(user.getId());
        Optional<User> noUser = repository.findById(user.getId());

        assertTrue(noUser.isEmpty());
    }

    private User insertUser(String email, String name, User.RoleType role) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setRole(role);

        return repository.save(user);
    }
}
