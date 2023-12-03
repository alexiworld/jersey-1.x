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
package com.sun.jersey.json.impl.writer;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author japod
 */
public class JacksonStringMergingGenerator extends JsonGenerator {

    JsonGenerator generator;
    boolean isClosed;
    String previousString;

    private JacksonStringMergingGenerator() {
    }

    private JacksonStringMergingGenerator(JsonGenerator generator) {
        this.generator = generator;
    }

    public static JacksonStringMergingGenerator createGenerator(JsonGenerator g) {
        return new JacksonStringMergingGenerator(g);
    }

    @Deprecated
    public void enableFeature(Feature feature) {
        enable(feature);
    }

    @Override
    public JsonGenerator enable(Feature feature) {
        return generator.enable(feature);
    }

    @Deprecated
    public void disableFeature(Feature feature) {
        disable(feature);
    }

    @Override
    public JsonGenerator disable(Feature feature) {
        return generator.disable(feature);
    }

    @Deprecated
    public void setFeature(Feature feature, boolean enabled) {
        if (enabled) {
            enable(feature);
        } else {
            disable(feature);
        }
    }

    @Deprecated
    public boolean isFeatureEnabled(Feature feature) {
        return isEnabled(feature);
    }

    @Override
    public boolean isEnabled(Feature f) {
        return generator.isEnabled(f);
    }

    @Override
    public int getFeatureMask() {
        return 0;
    }

    @Override
    public JsonGenerator setFeatureMask(int i) {
        return null;
    }

    @Override
    public JsonGenerator useDefaultPrettyPrinter() {
        return generator.useDefaultPrettyPrinter();
    }

    @Override
    public void writeStartArray() throws IOException, JsonGenerationException {
        generator.writeStartArray();
    }

    @Override
    public void writeEndArray() throws IOException, JsonGenerationException {
        flushPreviousString();
        generator.writeEndArray();
    }

    @Override
    public void writeStartObject() throws IOException, JsonGenerationException {
        generator.writeStartObject();
    }

    @Override
    public void writeEndObject() throws IOException, JsonGenerationException {
        flushPreviousString();
        generator.writeEndObject();
    }

    @Override
    public void writeFieldName(String name) throws IOException, JsonGenerationException {
        flushPreviousString();
        generator.writeFieldName(name);
    }

    @Override
    public void writeFieldName(SerializableString serializableString) throws IOException {

    }

    @Override
    public void writeString(String s) throws IOException, JsonGenerationException {
        generator.writeString(s);
    }

    public void writeStringToMerge(String s) throws IOException, JsonGenerationException {
        if (previousString == null) {
            previousString = s;
        } else {
            previousString += s;
        }
    }

    @Override
    public void writeString(char[] text, int start, int length) throws IOException, JsonGenerationException {
        generator.writeString(text, start, length);
    }

    @Override
    public void writeString(SerializableString serializableString) throws IOException {

    }

    @Override
    public void writeRawUTF8String(byte[] bytes, int start, int length) throws IOException, JsonGenerationException {
        generator.writeRawUTF8String(bytes, start, length);
    }

    @Override
    public void writeUTF8String(byte[] bytes, int start, int length) throws IOException, JsonGenerationException {
        generator.writeUTF8String(bytes, start, length);
    }

    @Override
    public void writeRaw(String raw) throws IOException, JsonGenerationException {
        generator.writeRaw(raw);
    }

    @Override
    public void writeRaw(String raw, int start, int length) throws IOException, JsonGenerationException {
        generator.writeRaw(raw, start, length);
    }

    @Override
    public void writeRaw(char[] raw, int start, int count) throws IOException, JsonGenerationException {
        generator.writeRaw(raw, start, count);
    }

    @Override
    public void writeRaw(char c) throws IOException, JsonGenerationException {
        generator.writeRaw(c);
    }

    @Override
    public void writeBinary(Base64Variant variant, byte[] bytes, int start, int count) throws IOException, JsonGenerationException {
        generator.writeBinary(variant, bytes, start, count);
    }

    @Override
    public int writeBinary(Base64Variant base64Variant, InputStream inputStream, int i) throws IOException {
        return 0;
    }

    @Override
    public void writeNumber(int i) throws IOException, JsonGenerationException {
        generator.writeNumber(i);
    }

    @Override
    public void writeNumber(long l) throws IOException, JsonGenerationException {
        generator.writeNumber(l);
    }

    @Override
    public void writeNumber(double d) throws IOException, JsonGenerationException {
        generator.writeNumber(d);
    }

    @Override
    public void writeNumber(float f) throws IOException, JsonGenerationException {
        generator.writeNumber(f);
    }

    @Override
    public void writeNumber(BigDecimal bd) throws IOException, JsonGenerationException {
        generator.writeNumber(bd);
    }

    @Override
    public void writeNumber(String number) throws IOException, JsonGenerationException, UnsupportedOperationException {
        generator.writeNumber(number);
    }

    @Override
    public void writeBoolean(boolean b) throws IOException, JsonGenerationException {
        generator.writeBoolean(b);
    }

    @Override
    public void writeNull() throws IOException, JsonGenerationException {
        generator.writeNull();
    }

    @Override
    public void copyCurrentEvent(JsonParser parser) throws IOException, JsonProcessingException {
        flushPreviousString();
        generator.copyCurrentEvent(parser);
    }

    @Override
    public void copyCurrentStructure(JsonParser parser) throws IOException, JsonProcessingException {
        flushPreviousString();
        generator.copyCurrentStructure(parser);
    }

    @Override
    public void flush() throws IOException {
        generator.flush();
    }

    @Override
    public void close() throws IOException {
        generator.close();
        isClosed = true;
    }

    @Override
    public JsonGenerator setCodec(ObjectCodec codec) {
        return generator.setCodec(codec);
    }

    @Override
    public ObjectCodec getCodec() {
        return generator.getCodec();
    }

    @Override
    public Version version() {
        return null;
    }

    @Override
    public void writeRawValue(String rawString) throws IOException, JsonGenerationException {
        generator.writeRawValue(rawString);
    }

    @Override
    public void writeRawValue(String rawString, int startIndex, int length) throws IOException, JsonGenerationException {
        generator.writeRawValue(rawString, startIndex, length);
    }

    @Override
    public void writeRawValue(char[] rawChars, int startIndex, int length) throws IOException, JsonGenerationException {
        generator.writeRawValue(rawChars, startIndex, length);
    }

    @Override
    public void writeNumber(BigInteger number) throws IOException, JsonGenerationException {
        generator.writeNumber(number);
    }

    @Override
    public void writeObject(Object o) throws IOException, JsonProcessingException {
        generator.writeObject(o);
    }

    @Override
    public void writeTree(TreeNode node) throws IOException, JsonProcessingException {
        generator.writeTree(node);
    }

    @Override
    public JsonStreamContext getOutputContext() {
        return generator.getOutputContext();
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    private void flushPreviousString() throws IOException {
        if (previousString != null) {
            generator.writeString(previousString);
            previousString = null;
        }
    }
}
