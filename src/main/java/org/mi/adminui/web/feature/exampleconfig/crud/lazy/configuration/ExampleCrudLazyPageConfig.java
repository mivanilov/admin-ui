package org.mi.adminui.web.feature.exampleconfig.crud.lazy.configuration;

import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.core.configuration.page.CrudPageConfig;

public class ExampleCrudLazyPageConfig extends CrudPageConfig {

    public final DtoFields dtoFields;
    public final SelectOptions selectOptions;
    public final Urls urls;

    private static ExampleCrudLazyPageConfig config = null;

    private ExampleCrudLazyPageConfig() {
        super();
        dtoFields = new DtoFields();
        selectOptions = new SelectOptions();
        urls = new Urls();
    }

    public static ExampleCrudLazyPageConfig get() {
        if (config == null) {
            config = new ExampleCrudLazyPageConfig();
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
        public final String exampleConfigType = "exampleConfigTypeSelectOptions";
    }

    public static class Urls {
        public final String edit = AppRoutes.EXAMPLE_CRUD_LAZY_EDIT;
        public final String cancelEdit = AppRoutes.EXAMPLE_CRUD_LAZY_EDIT_CANCEL;
        public final String delete = AppRoutes.EXAMPLE_CRUD_LAZY_DELETE;
        public final String tableRows = AppRoutes.EXAMPLE_CRUD_LAZY_LOAD_ROWS_PAGE;
    }
}
