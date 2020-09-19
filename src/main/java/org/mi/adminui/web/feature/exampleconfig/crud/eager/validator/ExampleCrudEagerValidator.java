package org.mi.adminui.web.feature.exampleconfig.crud.eager.validator;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.web.feature.exampleconfig.crud.eager.configuration.ExampleCrudEagerPageConfig;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ExampleCrudEagerValidator implements Validator {

    private static final String EXAMPLE_CONFIG_NAME = ExampleCrudEagerPageConfig.get().dtoFields.name;
    private static final String EXAMPLE_CONFIG_VISIBILITY = ExampleCrudEagerPageConfig.get().dtoFields.visibility;
    private static final String EXAMPLE_CONFIG_CREATE_DATE = ExampleCrudEagerPageConfig.get().dtoFields.createDate;
    private static final String EXAMPLE_CONFIG_TYPE = ExampleCrudEagerPageConfig.get().dtoFields.exampleConfigTypeValue;

    private static final int EXAMPLE_CONFIG_NAME_MAX_LENGTH = 255;

    private static final String INPUT_REQUIRED = "common.form-error.input.required";
    private static final String EXAMPLE_CONFIG_NAME_MAX_LENGTH_255 = "common.form-error.input.max-length-255";

    @Override
    public boolean supports(Class clazz) {
        return ExampleConfig.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, EXAMPLE_CONFIG_NAME, INPUT_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, EXAMPLE_CONFIG_VISIBILITY, INPUT_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, EXAMPLE_CONFIG_CREATE_DATE, INPUT_REQUIRED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, EXAMPLE_CONFIG_TYPE, INPUT_REQUIRED);

        ExampleConfig exampleConfig = (ExampleConfig) target;

        if (exampleConfig.getName() == null
                || exampleConfig.getVisibility() == null
                || exampleConfig.getCreateDate() == null
                || exampleConfig.getExampleConfigType() == null) {
            return;
        }

        if (exampleConfig.getName().length() > EXAMPLE_CONFIG_NAME_MAX_LENGTH) {
            errors.rejectValue(EXAMPLE_CONFIG_NAME, EXAMPLE_CONFIG_NAME_MAX_LENGTH_255);
        }
    }
}