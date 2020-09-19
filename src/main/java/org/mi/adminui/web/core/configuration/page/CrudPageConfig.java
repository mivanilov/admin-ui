package org.mi.adminui.web.core.configuration.page;

public class CrudPageConfig extends PageConfig {

    public final Components components;
    public final Buttons buttons;

    protected CrudPageConfig() {
        super();
        components = new Components();
        buttons = new Buttons();
    }

    public static class Components {
        public final String form = "formComponent";
        public final String table = "tableComponent";
        public final String confirmationModal = "confirmationModalComponent";
    }

    public static class Buttons {
        public final String create = "createButton";
        public final String saveEdit = "saveEditButton";
        public final String cancelEdit = "cancelEditButton";
    }
}
