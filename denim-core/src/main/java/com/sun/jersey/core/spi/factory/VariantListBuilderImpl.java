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

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Variant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An implementation of {@link VariantListBuilder}.
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class VariantListBuilderImpl extends Variant.VariantListBuilder {

    private List<Variant> variants;
    
    private final List<MediaType> mediaTypes = new ArrayList<MediaType>();
    
    private final List<Locale> languages = new ArrayList<Locale>();
   
    private final List<String> charsets = new ArrayList<String>();
    
    private final List<String> encodings = new ArrayList<String>();
    
    @Override
    public List<Variant> build() {
        if (variants == null)
            variants = new ArrayList<Variant>();
        
        return variants;
    }

    @Override
    public Variant.VariantListBuilder add() {
        if (variants == null)
            variants = new ArrayList<Variant>();

        addMediaTypes();
        
        charsets.clear();
        languages.clear();
        encodings.clear();
        mediaTypes.clear();

        return this;
    }

    private void addMediaTypes() {
        if (mediaTypes.isEmpty()) addLanguages(null);
        else for (MediaType mediaType : mediaTypes) addLanguages(mediaType);        
    }
    
    private void addLanguages(MediaType mediaType) {
        if (languages.isEmpty()) addEncodings(mediaType, null);
        else for (Locale language : languages) addEncodings(mediaType, language);        
    }
    
    private void addEncodings(MediaType mediaType, Locale language) {
        if (encodings.isEmpty()) addVariant(mediaType, language, null);
        else for (String encoding : encodings) addVariant(mediaType, language, encoding);        
    }

    private void addVariant(MediaType mediaType, Locale language, String encoding) {
        variants.add(new Variant(mediaType, language, encoding));
    }
    
    @Override
    public Variant.VariantListBuilder languages(Locale... languages) {
        for (Locale language : languages) this.languages.add(language);
        return this;
    }

    @Override
    public Variant.VariantListBuilder encodings(String... encodings) {
        for (String encoding : encodings) this.encodings.add(encoding);
        return this;
    }

    @Override
    public Variant.VariantListBuilder mediaTypes(MediaType... mediaTypes) {
        for (MediaType mediaType : mediaTypes) this.mediaTypes.add(mediaType);
        return this;
    }
}
