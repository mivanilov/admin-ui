package org.mi.adminui.web.feature.exampleconfig.crud.eager.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;
import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfigType;
import org.mi.adminui.web.feature.exampleconfig.crud.eager.configuration.ExampleCrudEagerPageConfig;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExampleCrudEagerValidatorTest {

    private static final String EXAMPLE_CONFIG_NAME = ExampleCrudEagerPageConfig.get().dtoFields.name;
    private static final String EXAMPLE_CONFIG_VISIBILITY = ExampleCrudEagerPageConfig.get().dtoFields.visibility;
    private static final String EXAMPLE_CONFIG_CREATE_DATE = ExampleCrudEagerPageConfig.get().dtoFields.createDate;
    private static final String EXAMPLE_CONFIG_TYPE = ExampleCrudEagerPageConfig.get().dtoFields.exampleConfigTypeValue;

    private static final String INPUT_REQUIRED = "common.form-error.input.required";
    private static final String EXAMPLE_CONFIG_NAME_MAX_LENGTH_255 = "common.form-error.input.max-length-255";

    @Mock
    private Errors errors;

    private final ExampleCrudEagerValidator exampleCrudEagerValidator = new ExampleCrudEagerValidator();

    @Test
    void validateRequiredInput() {
        ExampleConfig exampleConfig = new ExampleConfig();

        exampleCrudEagerValidator.validate(exampleConfig, errors);

        assertAll(
                () -> verify(errors).rejectValue(EXAMPLE_CONFIG_NAME, INPUT_REQUIRED, null, null),
                () -> verify(errors).rejectValue(EXAMPLE_CONFIG_VISIBILITY, INPUT_REQUIRED, null, null),
                () -> verify(errors).rejectValue(EXAMPLE_CONFIG_CREATE_DATE, INPUT_REQUIRED, null, null),
                () -> verify(errors).rejectValue(EXAMPLE_CONFIG_TYPE, INPUT_REQUIRED, null, null)
        );
    }

    @Test
    void validateNameMaxLength() {
        ExampleConfigType exampleConfigType = new ExampleConfigType();
        exampleConfigType.setId(1L);

        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.setName(StringUtils.repeat("n", 256));
        exampleConfig.setVisibility(ExampleConfig.Visibility.PRIVATE);
        exampleConfig.setCreateDate(LocalDate.now());
        exampleConfig.setExampleConfigType(exampleConfigType);

        exampleCrudEagerValidator.validate(exampleConfig, errors);

        verify(errors).rejectValue(EXAMPLE_CONFIG_NAME, EXAMPLE_CONFIG_NAME_MAX_LENGTH_255);
    }
}
