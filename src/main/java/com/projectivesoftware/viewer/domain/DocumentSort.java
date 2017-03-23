package com.projectivesoftware.viewer.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DocumentSort {

    String propertyName;

    boolean descending;

    public DocumentSort(String propertyName, boolean descending) {
        this.propertyName = propertyName;
        this.descending = descending;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("propertyName", propertyName)
                .append("descending", descending)
                .toString();
    }
}
