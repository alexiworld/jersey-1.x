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

import com.sun.jersey.core.provider.jaxb.AbstractJAXBProvider;
import com.sun.jersey.impl.ImplMessages;
import com.sun.jersey.spi.inject.Injectable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Providers;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class XMLRootObjectProvider extends AbstractJAXBProvider<Object> {

    // Delay construction of factory
    private final Injectable<SAXParserFactory> spf;
    
    XMLRootObjectProvider(Injectable<SAXParserFactory> spf, Providers ps) {
        super(ps);

        this.spf = spf;
    }
    
    XMLRootObjectProvider(Injectable<SAXParserFactory> spf, Providers ps, MediaType mt) {
        super(ps, mt);

        this.spf = spf;
    }
    
    @Override
    protected JAXBContext getStoredJAXBContext(Class type) throws JAXBException {
        return null;
    }
    
    @Produces("application/xml")
    @Consumes("application/xml")
    public static final class App extends XMLRootObjectProvider {
        public App(@Context Injectable<SAXParserFactory> spf, @Context Providers ps) {
            super(spf, ps , MediaType.APPLICATION_XML_TYPE);
        }
    }
    
    @Produces("text/xml")
    @Consumes("text/xml")
    public static final class Text extends XMLRootObjectProvider {
        public Text(@Context Injectable<SAXParserFactory> spf, @Context Providers ps) {
            super(spf, ps , MediaType.TEXT_XML_TYPE);
        }
    }
    
    @Produces("*/*")
    @Consumes("*/*")
    public static final class General extends XMLRootObjectProvider {
        public General(@Context Injectable<SAXParserFactory> spf, @Context Providers ps) {
            super(spf, ps);
        }
        
        @Override
        protected boolean isSupported(MediaType m) {
            return m.getSubtype().endsWith("+xml");
        }
    }
    
    public boolean isReadable(Class<?> type, Type genericType, Annotation annotations[], MediaType mediaType) {
        try {
            return Object.class == type && isSupported(mediaType) && getUnmarshaller(type, mediaType) != null;
        } catch (JAXBException cause) {
            throw new RuntimeException(ImplMessages.ERROR_UNMARSHALLING_JAXB(type), cause);
        }
    }
    
    public Object readFrom(
            Class<Object> type, 
            Type genericType, 
            Annotation annotations[],
            MediaType mediaType, 
            MultivaluedMap<String, String> httpHeaders, 
            InputStream entityStream) throws IOException {        
        try {
            return getUnmarshaller(type, mediaType).
                    unmarshal(getSAXSource(spf.getValue(), entityStream));
        } catch (UnmarshalException ex) {
            throw new WebApplicationException(ex, Status.BAD_REQUEST);
        } catch (JAXBException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType mediaType) {
        return false;
    }

    public void writeTo(Object arg0, Class<?> arg1, Type arg2, Annotation[] arg3, 
            MediaType arg4, MultivaluedMap<String, Object> arg5, OutputStream arg6) 
            throws IOException, WebApplicationException {
        throw new IllegalArgumentException();
    }
}