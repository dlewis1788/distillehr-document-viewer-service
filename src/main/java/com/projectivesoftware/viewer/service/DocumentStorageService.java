package com.projectivesoftware.viewer.service;

import com.projectivesoftware.viewer.domain.Document;
import com.projectivesoftware.viewer.domain.DocumentSort;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class DocumentStorageService {

    protected static final Log logger = LogFactory.getLog(DocumentStorageService.class);

    private final RestTemplate restTemplate;

    private final Integer pageSize;

    @Autowired
    public DocumentStorageService(RestTemplate restTemplate,
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

        String url = "http://172.16.81.151:8081/documents/search/patientId?patientId={patientId}&page={page}&limit={limit}&size={pageSize}";

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
        ResponseEntity<PagedResources<Document>> documentResponseEntity = restTemplate.exchange("http://172.16.81.151:8081/documents/search/patientId?patientId={patientId}&page=0&size=1", HttpMethod.GET, HttpEntity.EMPTY, new TypeReferences.PagedResourcesType<Document>() {}, parameterMap);
        long documentCount = documentResponseEntity.getBody().getMetadata().getTotalElements();
        logger.info("Returned document count is " + documentCount);
        return documentCount;
    }
}
