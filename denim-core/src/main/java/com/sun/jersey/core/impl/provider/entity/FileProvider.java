/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.jersey.core.impl.provider.entity;

import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;
import com.sun.jersey.core.util.ReaderWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
@Produces({"application/octet-stream", "*/*"})
@Consumes({"application/octet-stream", "*/*"})
public final class FileProvider extends AbstractMessageReaderWriterProvider<File> {
    
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return File.class == type;
    }
    
    public File readFrom(
            Class<File> type, 
            Type genericType, 
            Annotation annotations[],
            MediaType mediaType, 
            MultivaluedMap<String, String> httpHeaders, 
            InputStream entityStream) throws IOException {
        File f = File.createTempFile("rep","tmp");        
        OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
        try {
            writeTo(entityStream, out);
        } finally {
            out.close();
        }
        return f;
    }

    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return File.class.isAssignableFrom(type);
    }
    
    public void writeTo(
            File t,
            Class<?> type, 
            Type genericType, 
            Annotation annotations[], 
            MediaType mediaType, 
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(t), ReaderWriter.BUFFER_SIZE);
        try {
            writeTo(in, entityStream);
        } finally {
            in.close();
        }
    }

    @Override
    public long getSize(File t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return t.length();
    }
}