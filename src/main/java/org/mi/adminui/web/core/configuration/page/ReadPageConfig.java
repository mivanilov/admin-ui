package org.mi.adminui.web.core.configuration.page;

public class ReadPageConfig extends PageConfig {

    public final Components components;
    public final Buttons buttons;

    protected ReadPageConfig() {
        super();
        components = new Components();
        buttons = new Buttons();
    }

    public static class Components {
        public final String form = "formComponent";
        public final String table = "tableComponent";
    }

    public static class Buttons {
        public final String load = "loadButton";
    }
}
