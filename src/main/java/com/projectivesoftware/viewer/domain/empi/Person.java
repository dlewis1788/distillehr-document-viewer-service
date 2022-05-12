package com.projectivesoftware.viewer.domain.empi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@JsonRootName("person")
public class Person {

    private String familyName;

    private String givenName;

    private String address1;

    private String address2;

    private String city;

    private String state;

    private String postalCode;

    private Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    private String ssn;

    @JacksonXmlElementWrapper(useWrapping = false)
    private PersonIdentifier[] personIdentifiers;

    public Person() {

    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public PersonIdentifier[] getPersonIdentifiers() {
        return personIdentifiers;
    }

    public void setPersonIdentifiers(PersonIdentifier[] personIdentifiers) {
        this.personIdentifiers = personIdentifiers;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("familyName", familyName)
                .append("givenName", givenName)
                .append("address1", address1)
                .append("address2", address2)
                .append("city", city)
                .append("state", state)
                .append("postalCode", postalCode)
                .append("gender", gender)
                .append("dateOfBirth", dateOfBirth)
                .append("ssn", ssn)
                .append("personIdentifiers", personIdentifiers)
                .toString();
    }
}