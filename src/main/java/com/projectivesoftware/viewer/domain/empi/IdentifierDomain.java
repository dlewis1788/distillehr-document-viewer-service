package com.projectivesoftware.viewer.domain.empi;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class IdentifierDomain {

    private String identifierDomainName;

    private String namespaceIdentifier;

    private String universalIdentifier;

    private String universalIdentifierTypeCode;

    public IdentifierDomain() {

    }

    public IdentifierDomain(String identifierDomainName, String namespaceIdentifier, String universalIdentifier, String universalIdentifierTypeCode) {
        this.identifierDomainName = identifierDomainName;
        this.namespaceIdentifier = namespaceIdentifier;
        this.universalIdentifier = universalIdentifier;
        this.universalIdentifierTypeCode = universalIdentifierTypeCode;
    }

    public String getIdentifierDomainName() {
        return identifierDomainName;
    }

    public void setIdentifierDomainName(String identifierDomainName) {
        this.identifierDomainName = identifierDomainName;
    }

    public String getNamespaceIdentifier() {
        return namespaceIdentifier;
    }

    public void setNamespaceIdentifier(String namespaceIdentifier) {
        this.namespaceIdentifier = namespaceIdentifier;
    }

    public String getUniversalIdentifier() {
        return universalIdentifier;
    }

    public void setUniversalIdentifier(String universalIdentifier) {
        this.universalIdentifier = universalIdentifier;
    }

    public String getUniversalIdentifierTypeCode() {
        return universalIdentifierTypeCode;
    }

    public void setUniversalIdentifierTypeCode(String universalIdentifierTypeCode) {
        this.universalIdentifierTypeCode = universalIdentifierTypeCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifierDomainName", identifierDomainName)
                .append("namespaceIdentifier", namespaceIdentifier)
                .append("universalIdentifier", universalIdentifier)
                .append("universalIdentifierTypeCode", universalIdentifierTypeCode)
                .toString();
    }
}
