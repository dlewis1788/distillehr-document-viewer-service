package com.projectivesoftware.viewer.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class Document implements Serializable {

    public static final long serialVersionUID = 1L;

    private Long documentId;

    private String documentTitle;

    private String documentSubtitle;

    private Long patientId;

    private Long encounterId;

    private String documentType;

    private Date serviceDate;

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentSubtitle() {
        return documentSubtitle;
    }

    public void setDocumentSubtitle(String documentSubtitle) {
        this.documentSubtitle = documentSubtitle;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Long encounterId) {
        this.encounterId = encounterId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("documentId", documentId)
                .append("documentTitle", documentTitle)
                .append("documentSubtitle", documentSubtitle)
                .append("patientId", patientId)
                .append("encounterId", encounterId)
                .append("documentType", documentType)
                .append("serviceDate", serviceDate)
                .toString();
    }
}
