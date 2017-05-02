package com.projectivesoftware.viewer.service;

import com.projectivesoftware.viewer.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentStorageServiceClient {

    protected static final Log logger = LogFactory.getLog(DocumentStorageServiceClient.class);

    private final RestTemplate restTemplate;

    private final IdentifierMappingRepository identifierMappingRepository;

    @Autowired
    public DocumentStorageServiceClient(RestTemplate restTemplate,
                                        IdentifierMappingRepository identifierMappingRepository) {
        Assert.notNull(restTemplate, "restTemplate must not be null!");
        Assert.notNull(identifierMappingRepository, "identifierMappingRepository must not be null!");
        this.restTemplate = restTemplate;
        this.identifierMappingRepository = identifierMappingRepository;
    }

    public ResponseEntity<List<Document>> getDocumentList(Long patientId) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("patientId", Long.toString(patientId));
        String url = "http://distillehr-document-storage-service/document/documentsByPatientId/{patientId}";
        ResponseEntity<List<Document>> documentResponseEntity = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Document>>() {}, parameterMap);
        return documentResponseEntity;
    }

    public ResponseEntity<List<Document>> retrieveDocumentList(@PathVariable(value = "personId") String personId) throws Exception {

        ResponseEntity<List<Document>> documentResponseEntity = null;

        IdentifierMapping identifierMapping =
                identifierMappingRepository.
                        findByTargetSystemTypeAndTargetIdentifierTypeAndTargetIdentifierValue(
                                SystemType.CERNER_MILLENNIUM,
                                IdentifierType.CERNER_MILLENNIUM_PERSON_NUMBER,
                                Long.parseLong(personId));

        if (identifierMapping == null) {
            logger.warn("No identifierMapping found for personId " + personId + ". Returning empty documentMappingList.");
            return documentResponseEntity;
        }

        if ((identifierMapping.getSourceSystemType() == SystemType.HPF) &&
                (identifierMapping.getSourceIdentifierType() == IdentifierType.HPF_PATIENT_NUMBER)) {
            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put("patientId", identifierMapping.getSourceIdentifierValue().toString());
            String url = "http://distillehr-document-storage-service/document/documentsByPatientId/{patientId}";
            documentResponseEntity = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Document>>() {}, parameterMap);
        }

        return documentResponseEntity;
    }

    public long getDocumentCount(Long patientId) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("patientId", Long.toString(patientId));
        logger.info("Getting document count for " + patientId);
        ResponseEntity<PagedResources<Document>> documentResponseEntity = restTemplate.exchange("http://distillehr-document-storage-service/documents/search/patientId?patientId={patientId}&page=0&size=1", HttpMethod.GET, HttpEntity.EMPTY, new TypeReferences.PagedResourcesType<Document>() {}, parameterMap);
        long documentCount = documentResponseEntity.getBody().getMetadata().getTotalElements();
        logger.info("Returned document count is " + documentCount);
        return documentCount;
    }

    public DocumentStreamSource getDocumentContent(Long documentId) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("documentId", documentId.toString());
        logger.info("Getting document content for " + documentId);
        ResponseEntity<byte[]> documentContentEntity = restTemplate.exchange("http://distillehr-document-storage-service/document/content/{documentId}", HttpMethod.GET, HttpEntity.EMPTY, byte[].class, parameterMap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(documentContentEntity.getBody());
            return new DocumentStreamSource(byteArrayOutputStream);
        } catch (IOException e) {
            return null;
        }
    }
}