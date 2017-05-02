/*
 * Copyright (C) Projective Software LLC, 2017 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.projectivesoftware.viewer.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
        "sourceSystemType",
        "targetSystemType",
        "sourceIdentifierType",
        "targetIdentifierType",
        "sourceIdentifierValue",
        "targetIdentifierValue"}))
public class IdentifierMapping implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long identifierMappingId;

    @Enumerated(EnumType.ORDINAL)
    private SystemType sourceSystemType;

    @Enumerated(EnumType.ORDINAL)
    private SystemType targetSystemType;

    @Enumerated(EnumType.ORDINAL)
    private IdentifierType sourceIdentifierType;

    @Enumerated(EnumType.ORDINAL)
    private IdentifierType targetIdentifierType;

    private Long sourceIdentifierValue;

    private Long targetIdentifierValue;

    public Long getIdentifierMappingId() {
        return identifierMappingId;
    }

    public void setIdentifierMappingId(Long identifierMappingId) {
        this.identifierMappingId = identifierMappingId;
    }

    public SystemType getSourceSystemType() {
        return sourceSystemType;
    }

    public void setSourceSystemType(SystemType sourceSystemType) {
        this.sourceSystemType = sourceSystemType;
    }

    public SystemType getTargetSystemType() {
        return targetSystemType;
    }

    public void setTargetSystemType(SystemType targetSystemType) {
        this.targetSystemType = targetSystemType;
    }

    public IdentifierType getSourceIdentifierType() {
        return sourceIdentifierType;
    }

    public void setSourceIdentifierType(IdentifierType sourceIdentifierType) {
        this.sourceIdentifierType = sourceIdentifierType;
    }

    public IdentifierType getTargetIdentifierType() {
        return targetIdentifierType;
    }

    public void setTargetIdentifierType(IdentifierType targetIdentifierType) {
        this.targetIdentifierType = targetIdentifierType;
    }

    public Long getSourceIdentifierValue() {
        return sourceIdentifierValue;
    }

    public void setSourceIdentifierValue(Long sourceIdentifierValue) {
        this.sourceIdentifierValue = sourceIdentifierValue;
    }

    public Long getTargetIdentifierValue() {
        return targetIdentifierValue;
    }

    public void setTargetIdentifierValue(Long targetIdentifierValue) {
        this.targetIdentifierValue = targetIdentifierValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifierMappingId", identifierMappingId)
                .append("sourceSystemType", sourceSystemType)
                .append("targetSystemType", targetSystemType)
                .append("sourceIdentifierType", sourceIdentifierType)
                .append("targetIdentifierType", targetIdentifierType)
                .append("sourceIdentifierValue", sourceIdentifierValue)
                .append("targetIdentifierValue", targetIdentifierValue)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        IdentifierMapping that = (IdentifierMapping) o;

        return new EqualsBuilder()
                .append(identifierMappingId, that.identifierMappingId)
                .append(sourceSystemType, that.sourceSystemType)
                .append(targetSystemType, that.targetSystemType)
                .append(sourceIdentifierType, that.sourceIdentifierType)
                .append(targetIdentifierType, that.targetIdentifierType)
                .append(sourceIdentifierValue, that.sourceIdentifierValue)
                .append(targetIdentifierValue, that.targetIdentifierValue)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifierMappingId)
                .append(sourceSystemType)
                .append(targetSystemType)
                .append(sourceIdentifierType)
                .append(targetIdentifierType)
                .append(sourceIdentifierValue)
                .append(targetIdentifierValue)
                .toHashCode();
    }
}
