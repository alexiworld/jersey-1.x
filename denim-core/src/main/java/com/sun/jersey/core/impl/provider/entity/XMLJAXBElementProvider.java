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

import com.sun.jersey.core.provider.jaxb.AbstractJAXBElementProvider;
import com.sun.jersey.spi.inject.Injectable;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Providers;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class XMLJAXBElementProvider extends AbstractJAXBElementProvider {
    
    // Delay construction of factory
    private final Injectable<SAXParserFactory> spf;
    
    public XMLJAXBElementProvider(Injectable<SAXParserFactory> spf, Providers ps) {
        super(ps);

        this.spf = spf;
    }
    
    public XMLJAXBElementProvider(Injectable<SAXParserFactory> spf, Providers ps, MediaType mt) {
        super(ps, mt);        

        this.spf = spf;
    }
    
    @Produces("application/xml")
    @Consumes("application/xml")
    public static final class App extends XMLJAXBElementProvider {
        public App(@Context Injectable<SAXParserFactory> spf, @Context Providers ps) {
            super(spf, ps , MediaType.APPLICATION_XML_TYPE);
        }
    }
    
    @Produces("text/xml")
    @Consumes("text/xml")
    public static final class Text extends XMLJAXBElementProvider {
        public Text(@Context Injectable<SAXParserFactory> spf, @Context Providers ps) {
            super(spf, ps , MediaType.TEXT_XML_TYPE);
        }
    }
    
    @Produces("*/*")
    @Consumes("*/*")
    public static final class General extends XMLJAXBElementProvider {
        public General(@Context Injectable<SAXParserFactory> spf, @Context Providers ps) {
            super(spf, ps);
        }

        @Override
        protected boolean isSupported(MediaType m) {
            return m.getSubtype().endsWith("+xml");
        }
    }
    
    protected final JAXBElement<?> readFrom(Class<?> type, MediaType mediaType,
            Unmarshaller u, InputStream entityStream)
            throws JAXBException {
        return u.unmarshal(getSAXSource(spf.getValue(), entityStream), type);
    }

    protected final void writeTo(JAXBElement<?> t, MediaType mediaType, Charset c,
            Marshaller m, OutputStream entityStream)
            throws JAXBException {        
        m.marshal(t, entityStream);
    }
}