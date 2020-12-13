package org.mi.adminui.web.feature.user.validator;

import org.apache.commons.lang3.StringUtils;
import org.mi.adminui.data.feature.user.model.User;
import org.mi.adminui.security.userdetails.CustomUserDetails;
import org.mi.adminui.web.feature.user.configuration.UserPageConfig;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    private static final String USER_EMAIL = UserPageConfig.get().dtoFields.email;
    private static final String USER_NAME = UserPageConfig.get().dtoFields.name;
    private static final String USER_ROLE = UserPageConfig.get().dtoFields.role;

    private static final int USER_FIELDS_MAX_LENGTH = 255;

    private static final String GMAIL_REGEX = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:(gmail)+\\.)+[a-zA-Z]{2,6}$";
    private static final Pattern GMAIL_PATTERN = Pattern.compile(GMAIL_REGEX);

    private static final String INPUT_REQUIRED = "common.form-error.input.required";
    private static final String USER_EMAIL_FORMAT_INCORRECT = "user.error.email-format-incorrect";
    private static final String USER_FIELDS_INPUT_MAX_LENGTH_255 = "common.form-error.input.max-length-255";
    private static final String USER_SELF_ROLE_CHANGE_RESTRICTED = "user.error.self-role-change-restricted";

    @Override
    public boolean supports(Class clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USER_EMAIL, INPUT_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USER_NAME, INPUT_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USER_ROLE, INPUT_REQUIRED);

        User user = (User) target;

        if (StringUtils.isBlank(user.getEmail()) || StringUtils.isBlank(user.getName())) {
            return;
        }

        if (!GMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            errors.rejectValue(USER_EMAIL, USER_EMAIL_FORMAT_INCORRECT);
        }

        if (user.getEmail().length() >= USER_FIELDS_MAX_LENGTH) {
            errors.rejectValue(USER_EMAIL, USER_FIELDS_INPUT_MAX_LENGTH_255);
        }

        if (user.getName().length() >= USER_FIELDS_MAX_LENGTH) {
            errors.rejectValue(USER_NAME, USER_FIELDS_INPUT_MAX_LENGTH_255);
        }
    }

    public void validateEdit(User userToEdit, Errors errors, CustomUserDetails customUserDetails) {
        validate(userToEdit, errors);

        if (userToEdit.getEmail().equals(customUserDetails.getEmail())
                && !userToEdit.getRole().getSecurityName().equals(customUserDetails.getAuthorities().iterator().next().getAuthority())) {
            errors.rejectValue(USER_ROLE, USER_SELF_ROLE_CHANGE_RESTRICTED);
        }
    }

    public boolean isSelfDelete(User userToDelete, CustomUserDetails customUserDetails) {
        return userToDelete.getEmail().equals(customUserDetails.getEmail());
    }
}
