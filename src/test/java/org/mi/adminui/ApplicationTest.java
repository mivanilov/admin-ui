package org.mi.adminui;

import org.junit.jupiter.api.Test;
import org.mi.adminui.testconfiguration.AllTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(AllTestConfiguration.class)
class ApplicationTest {

    @Test
    void startApplication() {
        // app startup smoke test
    }
}
