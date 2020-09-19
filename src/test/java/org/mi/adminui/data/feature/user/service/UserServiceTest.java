package org.mi.adminui.data.feature.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.data.core.exception.RecordCreateException;
import org.mi.adminui.data.core.exception.RecordNotFoundException;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.data.feature.user.repository.UserRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void findAllUsers() {
        User user = getUser();

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.findAll();

        assertEquals(List.of(user), users);
    }

    @Test
    void findUserById() {
        User user = getUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.find(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void userNotFound() {
        User user = getUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Optional<User> noUser = userService.find(user.getId());

        assertTrue(noUser.isEmpty());
    }

    @Test
    void createUser() {
        User user = getUser();

        when(userRepository.existsById(user.getId())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.create(user);

        assertEquals(user, createdUser);
    }

    @Test
    void userToCreateAlreadyExists() {
        User user = getUser();

        when(userRepository.existsById(user.getId())).thenReturn(true);

        RecordCreateException exception = assertThrows(RecordCreateException.class, () -> userService.create(user));

        assertEquals("Record to insert already exists", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    void updateUser() {
        User user = getUser();

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.update(user);

        assertEquals(user, updatedUser);
    }

    @Test
    void userToUpdateNotFound() {
        User user = getUser();

        when(userRepository.existsById(user.getId())).thenReturn(false);

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> userService.update(user));

        assertEquals("Record to update not found", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    void deleteUser() {
        User user = getUser();

        userService.delete(user.getId());

        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void userToDeleteNotFound() {
        User user = getUser();

        doThrow(new EmptyResultDataAccessException(1)).when(userRepository).deleteById(user.getId());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> userService.delete(user.getId()));

        assertEquals("Record to delete not found", exception.getMessage());
    }

    private User getUser() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setName("admin");
        user.setRole(User.RoleType.ADMIN);

        return user;
    }
}
