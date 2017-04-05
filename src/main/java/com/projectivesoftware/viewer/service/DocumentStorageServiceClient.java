package com.projectivesoftware.viewer.service;

import com.projectivesoftware.viewer.domain.Document;
import com.projectivesoftware.viewer.domain.DocumentSort;
import com.projectivesoftware.viewer.domain.DocumentStreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeReferences;
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
import java.util.stream.Stream;

@Service
public class DocumentStorageServiceClient {

    protected static final Log logger = LogFactory.getLog(DocumentStorageServiceClient.class);

    private final RestTemplate restTemplate;

    private final Integer pageSize;

    @Autowired
    public DocumentStorageServiceClient(RestTemplate restTemplate,
                                        @Value("${com.projectivesoftware.viewer.service.document-storage-service.page-size}")
                                          Integer pageSize) {
        Assert.notNull(restTemplate, "restTemplate must not be null!");
        Assert.notNull(restTemplate, "pageSize must not be null!");
        this.restTemplate = restTemplate;
        this.pageSize = pageSize;
    }

    public Stream<Document> getPagedDocumentList(int offset, int limit, Long patientId, List<DocumentSort> documentSortList) {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("page", Integer.toString(offset / pageSize));
        parameterMap.put("limit", Long.toString(limit));
        parameterMap.put("pageSize", Integer.toString(pageSize));
        parameterMap.put("patientId", Long.toString(patientId));

        logger.info("Page is " + offset / pageSize);
        logger.info("Limit is " + limit);

        String url = "http://distillehr-document-storage-service/documents/search/patientId?patientId={patientId}&page={page}&limit={limit}&size={pageSize}&projection=full";

        for (DocumentSort documentSort : documentSortList) {
            logger.info("Processing " + documentSort.toString());
            String direction = "asc";
            if (documentSort.isDescending()) {
                direction = "desc";
            }
            url = url + "&sort=" + documentSort.getPropertyName() + "," + direction;
        }

        ResponseEntity<PagedResources<Resource<Document>>> documentResponseEntity = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new TypeReferences.PagedResourcesType<Resource<Document>>() {}, parameterMap);
        return documentResponseEntity.getBody().getContent().stream().map(Resource::getContent);
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
