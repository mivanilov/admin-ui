package org.mi.adminui.web.feature.exampleconfig.crud.lazy.validator;

import org.apache.commons.lang3.StringUtils;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.web.feature.exampleconfig.crud.lazy.configuration.ExampleCrudLazyPageConfig;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ExampleCrudLazyValidator implements Validator {

    private static final String EXAMPLE_CONFIG_NAME = ExampleCrudLazyPageConfig.get().dtoFields.name;
    private static final String EXAMPLE_CONFIG_VISIBILITY = ExampleCrudLazyPageConfig.get().dtoFields.visibility;
    private static final String EXAMPLE_CONFIG_CREATE_DATE = ExampleCrudLazyPageConfig.get().dtoFields.createDate;
    private static final String EXAMPLE_CONFIG_TYPE = ExampleCrudLazyPageConfig.get().dtoFields.exampleConfigTypeValue;

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

        if (StringUtils.isBlank(exampleConfig.getName())
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