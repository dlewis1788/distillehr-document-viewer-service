package com.projectivesoftware.viewer.service;

import com.projectivesoftware.viewer.domain.Document;
import com.projectivesoftware.viewer.domain.DocumentStreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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

    @Autowired
    public DocumentStorageServiceClient(RestTemplate restTemplate) {
        Assert.notNull(restTemplate, "restTemplate must not be null!");
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<List<Document>> getDocumentList(String personId) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("personId", personId);
        String url = "http://distillehr-document-storage-service/document/documentsByPersonId/{personId}";
        ResponseEntity<List<Document>> documentResponseEntity = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Document>>() {}, parameterMap);
        return documentResponseEntity;
    }

    public long getDocumentCount(String personId) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("personId", personId);
        logger.info("Getting document count for " + personId);
        ResponseEntity<PagedModel<Document>> documentResponseEntity = restTemplate.exchange("http://distillehr-document-storage-service/documents/search/personId?personId={personId}&page=0&size=1", HttpMethod.GET, HttpEntity.EMPTY, new TypeReferences.PagedModelType<Document>() {
        }, parameterMap);
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