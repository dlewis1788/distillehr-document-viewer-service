/*
 * Copyright (C) Projective Software LLC, 2017 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.projectivesoftware.viewer.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class DocumentStreamSource {

    private ByteArrayOutputStream byteArrayOutputStream = null;

    public DocumentStreamSource(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public InputStream getStream() {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
