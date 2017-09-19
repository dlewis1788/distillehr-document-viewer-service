/*
 * Copyright (C) Projective Software LLC, 2017 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.projectivesoftware.viewer.domain.empi;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonRootName("personIdentifier")
public class PersonIdentifier {

    private String identifier;

    private IdentifierDomain identifierDomain;

    public PersonIdentifier() {

    }

    public PersonIdentifier(String identifier, IdentifierDomain identifierDomain) {
        this.identifier = identifier;
        this.identifierDomain = identifierDomain;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public IdentifierDomain getIdentifierDomain() {
        return identifierDomain;
    }

    public void setIdentifierDomain(IdentifierDomain identifierDomain) {
        this.identifierDomain = identifierDomain;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifier", identifier)
                .append("identifierDomain", identifierDomain)
                .toString();
    }
}
