package org.mi.adminui.web.feature.user.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.security.model.UserPrincipal;
import org.mi.adminui.web.feature.user.configuration.UserPageConfig;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import org.thymeleaf.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    private static final String USER_EMAIL = UserPageConfig.get().dtoFields.email;
    private static final String USER_NAME = UserPageConfig.get().dtoFields.name;
    private static final String USER_ROLE = UserPageConfig.get().dtoFields.role;

    private static final String INPUT_REQUIRED = "common.form-error.input.required";
    private static final String USER_EMAIL_FORMAT_INCORRECT = "user.error.email-format-incorrect";
    private static final String USER_FIELDS_INPUT_MAX_LENGTH_255 = "common.form-error.input.max-length-255";
    private static final String USER_SELF_ROLE_CHANGE_RESTRICTED = "user.error.self-role-change-restricted";
    private static final String USER_SELF_DELETE_RESTRICTED = "user.error.self-delete-restricted";

    @Mock
    private Errors errors;

    private final UserValidator userValidator = new UserValidator();

    @Test
    void validateUserFieldsRequiredInput() {
        User user = new User();

        userValidator.validate(user, errors);

        assertAll(
                () -> verify(errors).rejectValue(USER_EMAIL, INPUT_REQUIRED, null, null),
                () -> verify(errors).rejectValue(USER_NAME, INPUT_REQUIRED, null, null),
                () -> verify(errors).rejectValue(USER_ROLE, INPUT_REQUIRED, null, null)
        );
    }

    @Test
    void validateUserEmailFormat() {
        User user = new User();
        user.setEmail("user@bad.com");
        user.setName("name");
        user.setRole(User.RoleType.ADMIN);

        userValidator.validate(user, errors);

        verify(errors).rejectValue(USER_EMAIL, USER_EMAIL_FORMAT_INCORRECT);
    }

    @Test
    void validateUserEmailMaxLength() {
        User user = new User();
        user.setEmail(StringUtils.repeat("u", 246) + "@gmail.com");
        user.setName("name");
        user.setRole(User.RoleType.ADMIN);

        userValidator.validate(user, errors);

        verify(errors).rejectValue(USER_EMAIL, USER_FIELDS_INPUT_MAX_LENGTH_255);
    }

    @Test
    void validateUserNameMaxLength() {
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setName(StringUtils.repeat("n", 256));
        user.setRole(User.RoleType.ADMIN);

        userValidator.validate(user, errors);

        verify(errors).rejectValue(USER_NAME, USER_FIELDS_INPUT_MAX_LENGTH_255);
    }

    @Test
    void validateUserSelfRoleChangeRestricted() {
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setName("name");
        user.setRole(User.RoleType.ADMIN);
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        User userToEdit = new User();
        userToEdit.setEmail("user@gmail.com");
        userToEdit.setName("name");
        userToEdit.setRole(User.RoleType.BUSINESS);

        userValidator.validateEdit(userToEdit, errors, userPrincipal);

        verify(errors).rejectValue(USER_ROLE, USER_SELF_ROLE_CHANGE_RESTRICTED);
    }

    @Test
    void validateUserSelfDeleteRestricted() {
        User userToDelete = new User();
        userToDelete.setEmail("user@gmail.com");
        userToDelete.setName("name");
        userToDelete.setRole(User.RoleType.ADMIN);

        UserPrincipal userPrincipal = UserPrincipal.create(userToDelete);

        userValidator.validateDelete(userToDelete, errors, userPrincipal);

        verify(errors).rejectValue(USER_EMAIL, USER_SELF_DELETE_RESTRICTED);
    }
}
