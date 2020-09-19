package org.mi.adminui.testconfiguration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({DataTestConfiguration.class})
public class AllTestConfiguration {
}