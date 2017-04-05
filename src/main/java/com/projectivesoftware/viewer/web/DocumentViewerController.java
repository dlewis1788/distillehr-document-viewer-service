package com.projectivesoftware.viewer.web;

import com.projectivesoftware.viewer.service.DocumentStorageServiceClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("/documentViewer")
public class DocumentViewerController {

    private final DocumentStorageServiceClient documentStorageServiceClient;

    @Autowired
    public DocumentViewerController(DocumentStorageServiceClient documentStorageServiceClient) {
        Assert.notNull(documentStorageServiceClient, "documentStorageService must not be null!");
        this.documentStorageServiceClient = documentStorageServiceClient;
    }

    @RequestMapping(path = "/document/{documentId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDocumentContent(@PathVariable("documentId") Long documentId) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setCacheControl("must-validate, post-check=0, pre-check=0");

        return new ResponseEntity<>(IOUtils.toByteArray(documentStorageServiceClient.getDocumentContent(documentId).getStream()), httpHeaders, HttpStatus.OK);
    }
}
