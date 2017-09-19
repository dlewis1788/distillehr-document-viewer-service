/*
 * Copyright (C) Projective Software LLC, 2017 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.projectivesoftware.viewer.web;

import com.projectivesoftware.viewer.domain.Document;
import com.projectivesoftware.viewer.domain.empi.Person;
import com.projectivesoftware.viewer.domain.empi.PersonIdentifier;
import com.projectivesoftware.viewer.service.DocumentStorageServiceClient;
import com.projectivesoftware.viewer.service.EmpiService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/documentViewer")
public class DocumentViewerController {

    protected static final Log logger = LogFactory.getLog(DocumentViewerController.class);

    private final DocumentStorageServiceClient documentStorageServiceClient;

    private final EmpiService empiService;

    @Autowired
    public DocumentViewerController(DocumentStorageServiceClient documentStorageServiceClient,
                                    EmpiService empiService) {
        Assert.notNull(documentStorageServiceClient, "documentStorageService must not be null!");
        Assert.notNull(empiService, "empiService must not be null!");
        this.documentStorageServiceClient = documentStorageServiceClient;
        this.empiService = empiService;
    }

    @RequestMapping(path = "/document/{documentId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDocumentContent(@PathVariable("documentId") Long documentId) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setCacheControl("must-validate, post-check=0, pre-check=0");

        return new ResponseEntity<>(IOUtils.toByteArray(documentStorageServiceClient.getDocumentContent(documentId).getStream()), httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "/documentList/{personId}", method = RequestMethod.GET)
    public ResponseEntity<List<Document>> getDocumentList(@PathVariable("personId") String personId) throws Exception {
        return documentStorageServiceClient.getDocumentList(personId);
    }

    @RequestMapping(path = "/retrieveDocumentList/cernerMillennium/person/{personId}", method = RequestMethod.GET)
    public ResponseEntity<List<Document>> retrieveDocumentList(@PathVariable("personId") String cernerPersonId) throws Exception {
        Person person = empiService.findByIdentifier(cernerPersonId);
        for (PersonIdentifier personIdentifier : person.getPersonIdentifiers()) {
            if (Objects.equals(personIdentifier.getIdentifierDomain().getIdentifierDomainName(), "ECID")) {
                return documentStorageServiceClient.getDocumentList(personIdentifier.getIdentifier());
            }
        }

        return null;
    }
}