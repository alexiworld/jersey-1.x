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

package com.sun.jersey.json.impl.provider.entity;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentInjector;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProviderContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;

/**
 *
 * @author Jakub.Podlesak@Sun.COM
 */
public class JacksonProviderProxy implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
    
    JacksonJsonProvider pojoProvider = new JacksonJsonProvider();
    JacksonJaxbJsonProvider jaxbProvider = new JacksonJaxbJsonProvider();

    boolean jacksonEntityProviderFeatureSet = false;

    @Context
    public void setFeaturesAndProperties(FeaturesAndProperties fp) {
        jacksonEntityProviderFeatureSet = fp.getFeature(JSONConfiguration.FEATURE_POJO_MAPPING);
    }

    private static class ProvidersInjectableProviderContext implements InjectableProviderContext {

        final Providers p;
        final Injectable i;

        private ProvidersInjectableProviderContext(final Providers p) {
            this.p = p;
            this.i = new Injectable() {

                @Override
                public Object getValue() {
                    return p;
                }
            };
        }

        @Override
        public boolean isAnnotationRegistered(Class<? extends Annotation> ac, Class<?> cc) {
            return ac == Context.class;
        }

        @Override
        public boolean isInjectableProviderRegistered(Class<? extends Annotation> ac, Class<?> cc, ComponentScope s) {
            return isAnnotationRegistered(ac, cc);
        }

        @Override
        public <A extends Annotation, C> Injectable getInjectable(Class<? extends Annotation> ac, ComponentContext ic, A a, C c, ComponentScope s) {
            return  (c == Providers.class) ? i : null;
        }

        @Override
        public <A extends Annotation, C> Injectable getInjectable(Class<? extends Annotation> ac, ComponentContext ic, A a, C c, List<ComponentScope> ls) {
            return  (c == Providers.class) ? i : null;
        }

        @Override
        public <A extends Annotation, C> InjectableScopePair getInjectableWithScope(Class<? extends Annotation> ac, ComponentContext ic, A a, C c, List<ComponentScope> ls) {
            return (c == Providers.class) ?  new InjectableScopePair(i, ls.get(0)) : null;
        }

    }

    @Context
    public void setProviders(Providers p) {
        new ComponentInjector<JacksonJsonProvider>(new ProvidersInjectableProviderContext(p), JacksonJsonProvider.class).inject(pojoProvider);
        new ComponentInjector<JacksonJaxbJsonProvider>(new ProvidersInjectableProviderContext(p), JacksonJaxbJsonProvider.class).inject(jaxbProvider);
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return jacksonEntityProviderFeatureSet && 
                (jaxbProvider.isReadable(type, genericType, annotations, mediaType) || pojoProvider.isReadable(type, genericType, annotations, mediaType));
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        return jaxbProvider.isReadable(type, genericType, annotations, mediaType) ?
            jaxbProvider.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream) :
            pojoProvider.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream);
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return jacksonEntityProviderFeatureSet && 
                (jaxbProvider.isWriteable(type, genericType, annotations, mediaType) || pojoProvider.isWriteable(type, genericType, annotations, mediaType));
    }

    @Override
    public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return jaxbProvider.isWriteable(type, genericType, annotations, mediaType) ?
            jaxbProvider.getSize(t, type, genericType, annotations, mediaType) :
            pojoProvider.getSize(t, type, genericType, annotations, mediaType);
    }

    @Override
    public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        if (jaxbProvider.isWriteable(type, genericType, annotations, mediaType)) {
            jaxbProvider.writeTo(t, type, genericType, annotations, mediaType, httpHeaders, entityStream);
        } else {
            pojoProvider.writeTo(t, type, genericType, annotations, mediaType, httpHeaders, entityStream);
        }
    }
}
