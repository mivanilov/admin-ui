package org.mi.adminui.web.feature.exampleconfig.core.model;

import org.mi.adminui.data.feature.exampleconfig.model.ExampleConfig.Visibility;

public class ExampleConfigPageReq {

    private int pageNumber;
    private int pageSize;
    private Visibility visibility;

    public ExampleConfigPageReq(int pageNumber, int pageSize, Visibility visibility) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.visibility = visibility;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(final Visibility visibility) {
        this.visibility = visibility;
    }
}
