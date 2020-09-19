package org.mi.adminui.testconfiguration;

import org.mi.adminui.data.feature.exampleconfig.repository.ExampleConfigRepository;
import org.mi.adminui.data.feature.exampleconfig.repository.ExampleConfigTypeRepository;
import org.mi.adminui.data.feature.user.repository.UserRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.sql.DataSource;

@TestConfiguration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
public class DataTestConfiguration {

    @MockBean
    private DataSource dataSource;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ExampleConfigRepository exampleConfigRepository;

    @MockBean
    private ExampleConfigTypeRepository exampleConfigTypeRepository;
}