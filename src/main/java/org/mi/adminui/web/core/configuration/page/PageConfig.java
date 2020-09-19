package org.mi.adminui.web.core.configuration.page;

public class PageConfig {

    public final String tableRows;
    public final Fragments fragments;

    protected PageConfig() {
        tableRows = "tableRows";
        fragments = new Fragments();
    }

    public static class Fragments {
        public final String page = "pageFragment";
        public final String form = "formFragment";
        public final String table = "tableFragment";
    }
}
