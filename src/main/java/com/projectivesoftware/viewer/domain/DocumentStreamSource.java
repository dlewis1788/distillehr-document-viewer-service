package com.projectivesoftware.viewer.domain;

import com.vaadin.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class DocumentStreamSource implements StreamResource.StreamSource {

    private ByteArrayOutputStream byteArrayOutputStream = null;

    public DocumentStreamSource(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
