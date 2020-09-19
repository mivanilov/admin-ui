package org.mi.adminui.web.feature.exampleconfig.read.lazy.configuration;

import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.core.configuration.page.ReadPageConfig;

public class ExampleReadLazyPageConfig extends ReadPageConfig {

    public final DtoFields dtoFields;
    public final SelectOptions selectOptions;
    public final Urls urls;

    private static ExampleReadLazyPageConfig config = null;

    private ExampleReadLazyPageConfig() {
        super();
        dtoFields = new DtoFields();
        selectOptions = new SelectOptions();
        urls = new Urls();
    }

    public static ExampleReadLazyPageConfig get() {
        if (config == null) {
            config = new ExampleReadLazyPageConfig();
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
        public final String tableRows = AppRoutes.EXAMPLE_READ_LAZY_LOAD_ROWS_PAGE;
    }
}
