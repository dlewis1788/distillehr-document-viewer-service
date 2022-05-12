package com.projectivesoftware.viewer.domain.empi;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Gender {

    private String genderCode;

    public Gender() {
    }

    public Gender(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("genderCode", genderCode)
                .toString();
    }
}
