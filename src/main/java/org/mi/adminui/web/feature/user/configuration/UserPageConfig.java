package org.mi.adminui.web.feature.user.configuration;

import org.mi.adminui.web.core.configuration.constant.AppRoutes;
import org.mi.adminui.web.core.configuration.page.CrudPageConfig;

public class UserPageConfig extends CrudPageConfig {

    public final DtoFields dtoFields;
    public final SelectOptions selectOptions;
    public final Urls urls;
    public final SubmitErrorMessageKeys submitErrorMessageKeys;

    private static UserPageConfig config = null;

    private UserPageConfig() {
        super();
        dtoFields = new DtoFields();
        selectOptions = new SelectOptions();
        urls = new Urls();
        submitErrorMessageKeys = new SubmitErrorMessageKeys();
    }

    public static UserPageConfig get() {
        if (config == null) {
            config = new UserPageConfig();
        }
        return config;
    }

    public static class DtoFields {
        public final String email = "email";
        public final String name = "name";
        public final String role = "role";
    }

    public static class SelectOptions {
        public final String roleType = "roleTypeSelectOptions";
    }

    public static class Urls {
        public final String edit = AppRoutes.USERS_EDIT;
        public final String cancelEdit = AppRoutes.USERS_EDIT_CANCEL;
        public final String delete = AppRoutes.USERS_DELETE;
        public final String tableRows = AppRoutes.USERS_LOAD;
    }

    public static class SubmitErrorMessageKeys {
        public final String errorCreating = "user.error.creating";
        public final String errorUpdating = "user.error.updating";
        public final String errorDeletingNotFound = "user.error.deleting.not-found";
        public final String errorDeletingSelf = "user.error.deleting.self";
    }
}

