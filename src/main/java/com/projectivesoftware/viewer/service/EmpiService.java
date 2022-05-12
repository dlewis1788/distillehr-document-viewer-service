package com.projectivesoftware.viewer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.projectivesoftware.viewer.domain.empi.IdentifierDomain;
import com.projectivesoftware.viewer.domain.empi.Person;
import com.projectivesoftware.viewer.domain.empi.PersonIdentifier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class EmpiService {

    protected static final Log logger = LogFactory.getLog(EmpiService.class);

    private final String empiServiceUrl;

    private final String empiSessionKey;

    private final String empiTargetIdentifierDomainName;

    public EmpiService(@Value("${com.projectivesoftware.distillehr-document-viewer-service.empi-service-url}") String empiServiceUrl,
                       @Value("${com.projectivesoftware.distillehr-document-viewer-service.empi-session-key}") String empiSessionKey,
                       @Value("${com.projectivesoftware.distillehr-document-viewer-service.empi-target-identifier-domain-name}") String empiTargetIdentifierDomainName) {
        Assert.notNull(empiServiceUrl, "empiServiceUrl must not be null!");
        Assert.notNull(empiSessionKey, "empiSessionKey must not be null!");
        Assert.notNull(empiTargetIdentifierDomainName, "empiTargetIdentifierDomainName must not be null!");
        this.empiServiceUrl = empiServiceUrl;
        this.empiSessionKey = empiSessionKey;
        this.empiTargetIdentifierDomainName = empiTargetIdentifierDomainName;
    }

    public Person personUpload(Person person) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();

        logger.info("Uploading " + person.toString());
        logger.info("Xml = " + xmlMapper.writeValueAsString(person));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("OPENEMPI_SESSION_KEY", empiSessionKey);
        httpHeaders.setContentType(MediaType.APPLICATION_XML);

        HttpEntity<String> httpEntity = new HttpEntity<>(xmlMapper.writeValueAsString(person), httpHeaders);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        HttpEntity<Person> personResponseHttpEntity = restTemplate.exchange(empiServiceUrl + "/person-manager-resource/addPerson", HttpMethod.PUT, httpEntity, Person.class);
        Person personResponse = personResponseHttpEntity.getBody();

        logger.info("Response is " + xmlMapper.writeValueAsString(personResponse));

        return personResponse;
    }

    public Person findByPersonId(String personId) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("OPENEMPI_SESSION_KEY", empiSessionKey);
        httpHeaders.setContentType(MediaType.APPLICATION_XML);

        PersonIdentifier personIdentifier = new PersonIdentifier();
        IdentifierDomain identifierDomain = new IdentifierDomain();
        identifierDomain.setIdentifierDomainName(empiTargetIdentifierDomainName);
        identifierDomain.setNamespaceIdentifier(empiTargetIdentifierDomainName);
        identifierDomain.setUniversalIdentifier(empiTargetIdentifierDomainName);
        identifierDomain.setUniversalIdentifierTypeCode(empiTargetIdentifierDomainName);
        personIdentifier.setIdentifierDomain(identifierDomain);
        personIdentifier.setIdentifier(personId);

        HttpEntity<String> httpEntity = new HttpEntity<>(xmlMapper.writeValueAsString(personIdentifier), httpHeaders);

        logger.info("Xml = " + xmlMapper.writeValueAsString(personIdentifier));

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        HttpEntity<Person> personResponseHttpEntity = restTemplate.exchange(empiServiceUrl + "/person-query-resource/findPersonById", HttpMethod.POST, httpEntity, Person.class);

        logger.info("Response is " + xmlMapper.writeValueAsString(personResponseHttpEntity.getBody()));

        return personResponseHttpEntity.getBody();
    }

    public Person findByIdentifier(String identifier) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("OPENEMPI_SESSION_KEY", empiSessionKey);
        httpHeaders.setContentType(MediaType.APPLICATION_XML);

        PersonIdentifier personIdentifier = new PersonIdentifier();
        IdentifierDomain identifierDomain = new IdentifierDomain();
        identifierDomain.setIdentifierDomainName(empiTargetIdentifierDomainName);
        identifierDomain.setNamespaceIdentifier(empiTargetIdentifierDomainName);
        identifierDomain.setUniversalIdentifier(empiTargetIdentifierDomainName);
        identifierDomain.setUniversalIdentifierTypeCode(empiTargetIdentifierDomainName);
        personIdentifier.setIdentifierDomain(identifierDomain);
        personIdentifier.setIdentifier(identifier);

        HttpEntity<String> httpEntity = new HttpEntity<>(xmlMapper.writeValueAsString(personIdentifier), httpHeaders);

        logger.info("Xml = " + xmlMapper.writeValueAsString(personIdentifier));

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        HttpEntity<Person> personResponseHttpEntity = restTemplate.exchange(empiServiceUrl + "/person-query-resource/findPersonById", HttpMethod.POST, httpEntity, Person.class);

        logger.info("Response is " + xmlMapper.writeValueAsString(personResponseHttpEntity.getBody()));

        return personResponseHttpEntity.getBody();
    }

    public Boolean isValidPerson(Person person) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("OPENEMPI_SESSION_KEY", empiSessionKey);
        httpHeaders.setContentType(MediaType.APPLICATION_XML);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        PersonIdentifier[] personIdentifiers = person.getPersonIdentifiers();

        for (PersonIdentifier personIdentifier : personIdentifiers) {
            HttpEntity<String> httpEntity = new HttpEntity<>(xmlMapper.writeValueAsString(personIdentifier), httpHeaders);
            HttpEntity<Person> personResponseHttpEntity = restTemplate.exchange(empiServiceUrl + "/person-query-resource/findPersonById", HttpMethod.POST, httpEntity, Person.class);
            Person personResponse = personResponseHttpEntity.getBody();

            if (personResponse == null) {
                return false;
            } else {
                for (PersonIdentifier personResponseIdentifier : personResponse.getPersonIdentifiers()) {
                    if (Objects.equals(personResponseIdentifier.getIdentifierDomain().getIdentifierDomainName(), empiTargetIdentifierDomainName)) {
                        return Objects.equals(personResponseIdentifier.getIdentifier(), personIdentifier.getIdentifier());
                    }
                }
            }
        }

        return false;
    }
}
