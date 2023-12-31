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

package com.sun.jersey.core.spi.factory;

import com.sun.jersey.core.header.OutBoundHeaders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status.Family;
import jakarta.ws.rs.ext.MessageBodyWriter;

/**
 * An implementation of {@link Response}.
 * <p>
 * This implementation supports the declaration of an entity type that will be
 * utilized when a {@link MessageBodyWriter} is selected to write out the
 * entity.
 * 
 * @author Paul.Sandoz@Sun.Com
 */
public class ResponseImpl extends Response {

    private final StatusType statusType;
    
    private final MultivaluedMap<String, Object> headers;

    private final Object entity;

    private final Type entityType;

    /**
     * Construct given a status type, entity and metadata.
     *
     * @param statusType the status type
     * @param headers the metadata, it is the callers responsibility to copy
     *        the metadata if necessary.
     * @param entity the entity
     * @param entityType the entity type, it is the callers responsibility to
     *        ensure the entity type is compatible with the entity.
     */
    protected ResponseImpl(StatusType statusType, OutBoundHeaders headers, Object entity, Type entityType) {
        this.statusType = statusType;
        this.headers = headers;
        this.entity = entity;
        this.entityType = entityType;
    }

    /**
     * Construct given a status, entity and metadata.
     *
     * @param status the status
     * @param headers the metadata, it is the callers responsibility to copy
     *        the metadata if necessary.
     * @param entity the entity
     * @param entityType the entity type, it is the callers responsibility to
     *        ensure the entity type is compatible with the entity.
     */
    protected ResponseImpl(int status, OutBoundHeaders headers, Object entity, Type entityType) {
        this.statusType = toStatusType(status);
        this.headers = headers;
        this.entity = entity;
        this.entityType = entityType;
    }

    /**
     * Get the status type.
     *
     * @return the status type.
     */
    public StatusType getStatusType() {
        return statusType;
    }

    /**
     * Get the entity type.
     *
     * @return the entity type.
     */
    public Type getEntityType() {
        return entityType;
    }

    // Response 
    
    public int getStatus() {
        return statusType.getStatusCode();
    }

    @Override
    public StatusType getStatusInfo() {
        return null;
    }

    public MultivaluedMap<String, Object> getMetadata() {
        return headers;
    }

    @Override
    public MultivaluedMap<String, String> getStringHeaders() {
        return null;
    }

    @Override
    public String getHeaderString(String s) {
        return null;
    }

    public Object getEntity() {
        return entity;
    }

    @Override
    public <T> T readEntity(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T readEntity(GenericType<T> genericType) {
        return null;
    }

    @Override
    public <T> T readEntity(Class<T> aClass, Annotation[] annotations) {
        return null;
    }

    @Override
    public <T> T readEntity(GenericType<T> genericType, Annotation[] annotations) {
        return null;
    }

    @Override
    public boolean hasEntity() {
        return false;
    }

    @Override
    public boolean bufferEntity() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public MediaType getMediaType() {
        return null;
    }

    @Override
    public Locale getLanguage() {
        return null;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public Set<String> getAllowedMethods() {
        return null;
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        return null;
    }

    @Override
    public EntityTag getEntityTag() {
        return null;
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public Date getLastModified() {
        return null;
    }

    @Override
    public URI getLocation() {
        return null;
    }

    @Override
    public Set<Link> getLinks() {
        return null;
    }

    @Override
    public boolean hasLink(String s) {
        return false;
    }

    @Override
    public Link getLink(String s) {
        return null;
    }

    @Override
    public Link.Builder getLinkBuilder(String s) {
        return null;
    }

    public static StatusType toStatusType(final int statusCode) {
        switch(statusCode) {
            case 200: return Status.OK;
            case 201: return Status.CREATED;
            case 202: return Status.ACCEPTED;
            case 204: return Status.NO_CONTENT;
            case 205: return Status.RESET_CONTENT;
            case 206: return Status.PARTIAL_CONTENT;

            case 300: return Status.MULTIPLE_CHOICES;
            case 301: return Status.MOVED_PERMANENTLY;
            case 302: return Status.FOUND;
            case 303: return Status.SEE_OTHER;
            case 304: return Status.NOT_MODIFIED;
            case 305: return Status.USE_PROXY;
            case 307: return Status.TEMPORARY_REDIRECT;
            case 308: return Status.PERMANENT_REDIRECT;

            case 400: return Status.BAD_REQUEST;
            case 401: return Status.UNAUTHORIZED;
            case 402: return Status.PAYMENT_REQUIRED;
            case 403: return Status.FORBIDDEN;
            case 404: return Status.NOT_FOUND;
            case 405: return Status.METHOD_NOT_ALLOWED;
            case 406: return Status.NOT_ACCEPTABLE;
            case 407: return Status.PROXY_AUTHENTICATION_REQUIRED;
            case 408: return Status.REQUEST_TIMEOUT;
            case 409: return Status.CONFLICT;
            case 410: return Status.GONE;
            case 411: return Status.LENGTH_REQUIRED;
            case 412: return Status.PRECONDITION_FAILED;
            case 413: return Status.REQUEST_ENTITY_TOO_LARGE;
            case 414: return Status.REQUEST_URI_TOO_LONG;
            case 415: return Status.UNSUPPORTED_MEDIA_TYPE;
            case 416: return Status.REQUESTED_RANGE_NOT_SATISFIABLE;
            case 417: return Status.EXPECTATION_FAILED;
            case 428: return Status.PRECONDITION_REQUIRED;
            case 429: return Status.TOO_MANY_REQUESTS;
            case 431: return Status.REQUEST_HEADER_FIELDS_TOO_LARGE;
            case 451: return Status.UNAVAILABLE_FOR_LEGAL_REASONS;

            case 500: return Status.INTERNAL_SERVER_ERROR;
            case 501: return Status.NOT_IMPLEMENTED;
            case 502: return Status.BAD_GATEWAY;
            case 503: return Status.SERVICE_UNAVAILABLE;
            case 504: return Status.GATEWAY_TIMEOUT;
            case 505: return Status.HTTP_VERSION_NOT_SUPPORTED;
            case 511: return Status.NETWORK_AUTHENTICATION_REQUIRED;

            default: {
                return new StatusType() {
                    @Override
                    public int getStatusCode() {
                        return statusCode;
                    }

                    @Override
                    public Family getFamily() {
                        return toFamilyCode(statusCode);
                    }

                    @Override
                    public String getReasonPhrase() {
                        return "";
                    }
                };
            }
        }
    }

    public static Family toFamilyCode(final int statusCode) {
        switch(statusCode / 100) {
            case 1: return Family.INFORMATIONAL;
            case 2: return Family.SUCCESSFUL;
            case 3: return Family.REDIRECTION;
            case 4: return Family.CLIENT_ERROR;
            case 5: return Family.SERVER_ERROR;
            default: return Family.OTHER;
        }
    }
}