package org.mi.adminui.web.feature.exampleconfig.read.eager.configuration;

import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.core.configuration.page.ReadPageConfig;

public class ExampleReadEagerPageConfig extends ReadPageConfig {

    public final DtoFields dtoFields;
    public final SelectOptions selectOptions;
    public final Urls urls;

    private static ExampleReadEagerPageConfig config = null;

    private ExampleReadEagerPageConfig() {
        super();
        dtoFields = new DtoFields();
        selectOptions = new SelectOptions();
        urls = new Urls();
    }

    public static ExampleReadEagerPageConfig get() {
        if (config == null) {
            config = new ExampleReadEagerPageConfig();
        }
        return config;
    }

    public static class DtoFields {
        public final String id = "id";
        public final String name = "name";
        public final String visibility = "visibility";
        public final String createDate = "createDate";
        public final String active = "active";
        public final String exampleConfigTypeValue = "exampleConfigTypeValue";
        public final String exampleConfigTypeText = "exampleConfigTypeText";
    }

    public static class SelectOptions {
        public final String visibility = "visibilitySelectOptions";
    }

    public static class Urls {
        public final String tableRows = AppRoutes.EXAMPLE_READ_EAGER_LOAD_ROWS;
    }
}
