package org.mi.adminui.web.feature.exampleconfig.core.model;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig;

import java.util.List;

public class ExampleConfigPageRes {

    private final long total;
    private final List<ExampleConfig> rows;

    public ExampleConfigPageRes(long total, List<ExampleConfig> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public List<ExampleConfig> getRows() {
        return rows;
    }
}
