package org.mi.adminui.web.feature.exampleconfig.crud.eager.configuration;

import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.core.configuration.page.CrudPageConfig;

public class ExampleCrudEagerPageConfig extends CrudPageConfig {

    public final DtoFields dtoFields;
    public final SelectOptions selectOptions;
    public final Urls urls;

    private static ExampleCrudEagerPageConfig config = null;

    private ExampleCrudEagerPageConfig() {
        super();
        dtoFields = new DtoFields();
        selectOptions = new SelectOptions();
        urls = new Urls();
    }

    public static ExampleCrudEagerPageConfig get() {
        if (config == null) {
            config = new ExampleCrudEagerPageConfig();
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
        public final String edit = AppRoutes.EXAMPLE_CRUD_EAGER_EDIT;
        public final String cancelEdit = AppRoutes.EXAMPLE_CRUD_EAGER_EDIT_CANCEL;
        public final String delete = AppRoutes.EXAMPLE_CRUD_EAGER_DELETE;
        public final String tableRows = AppRoutes.EXAMPLE_CRUD_EAGER_LOAD_ROWS;
    }
}
