/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.sun.jersey.json.impl.AttrAndCharDataBean;
import com.sun.jersey.json.impl.ComplexBeanWithAttributes;
import com.sun.jersey.json.impl.ComplexBeanWithAttributes2;
import com.sun.jersey.json.impl.ComplexBeanWithAttributes3;
import com.sun.jersey.json.impl.EncodedContentBean;
import com.sun.jersey.json.impl.JSONHelper;
import com.sun.jersey.json.impl.ListAndNonListBean;
import com.sun.jersey.json.impl.ListEmptyBean;
import com.sun.jersey.json.impl.ListWrapperBean;
import com.sun.jersey.json.impl.PureCharDataBean;
import com.sun.jersey.json.impl.SimpleBean;
import com.sun.jersey.json.impl.SimpleBeanWithAttributes;
import com.sun.jersey.json.impl.SimpleBeanWithJustOneAttribute;
import com.sun.jersey.json.impl.SimpleBeanWithJustOneAttributeAndValue;
import com.sun.jersey.json.impl.TreeModel;
import com.sun.jersey.json.impl.TwoListsWrapperBean;
import com.sun.jersey.json.impl.User;
import com.sun.jersey.json.impl.UserTable;

import junit.framework.TestCase;

/**
 * @author Jakub Podlesak (jakub.podlesak at oracle.com)
 * @author Michal Gajdos (michal.gajdos at oracle.com)
 */
public class Stax2JacksonWriterTest extends TestCase {

    public Stax2JacksonWriterTest(String testName) {
        super(testName);
    }

    public void testAttrAndCharData() throws Exception {
        _testBean(AttrAndCharDataBean.class, AttrAndCharDataBean.createTestInstance());
    }

    public void testComplexBeanWithAttributes() throws Exception {
        _testBean(ComplexBeanWithAttributes.class, ComplexBeanWithAttributes.createTestInstance());
    }

    public void testComplexBeanWithAttributes2() throws Exception {
        _testBean(ComplexBeanWithAttributes2.class, ComplexBeanWithAttributes2.createTestInstance());
    }

    public void testComplexBeanWithAttributes3() throws Exception {
        _testBean(ComplexBeanWithAttributes3.class, ComplexBeanWithAttributes3.createTestInstance());
    }

    public void testEncodedContentBean() throws Exception {
        _testBean(EncodedContentBean.class, EncodedContentBean.createTestInstance());
    }

    public void testListAndNonListBean() throws Exception {
        _testBean(ListAndNonListBean.class, ListAndNonListBean.createTestInstance());
    }

    public void testListEmptyBean() throws Exception {
        _testBean(ListEmptyBean.class, ListEmptyBean.createTestInstance());
    }

    public void testListWrapperBean() throws Exception {
        _testBean(ListWrapperBean.class, ListWrapperBean.createTestInstance());
    }

    public void testPureCharDataBean() throws Exception {
        _testBean(PureCharDataBean.class, PureCharDataBean.createTestInstance());
    }

    public void testSimpleBean() throws Exception {
        _testBean(SimpleBean.class, SimpleBean.createTestInstance());
    }

    public void testSimpleBeanWithAttributes() throws Exception {
        _testBean(SimpleBeanWithAttributes.class, SimpleBeanWithAttributes.createTestInstance());
    }

    public void testSimpleBeanWithJustOneAttribute() throws Exception {
        _testBean(SimpleBeanWithJustOneAttribute.class, SimpleBeanWithJustOneAttribute.createTestInstance());
    }

    public void testSimpleBeanWithJustOneAttributeAndValue() throws Exception {
        _testBean(SimpleBeanWithJustOneAttributeAndValue.class, SimpleBeanWithJustOneAttributeAndValue.createTestInstance());
    }

    public void testTreeModelBean() throws Exception {
        _testBean(TreeModel.class, TreeModel.createTestInstance());
    }

    public void testTreeModelRootWithOneChild() throws Exception {
        _testBean(TreeModel.class, TreeModel.createTestInstanceWithRootAndOneChildNode());
    }

    public void testTreeModelRootWithMultipleChildren() throws Exception {
        _testBean(TreeModel.class, TreeModel.createTestInstanceWithRootAndMultipleChildNodes());
    }

    public void testTwoListsWrapperBean() throws Exception {
        _testBean(TwoListsWrapperBean.class, TwoListsWrapperBean.createTestInstance());
    }

    public void testUser() throws Exception {
        _testBean(User.class, User.createTestInstance());
    }

    public void testUserTable() throws Exception {
        _testBean(UserTable.class, UserTable.createTestInstance());
    }

    private void _testBean(Class clazz, Object bean) throws Exception {
        Map<String, Object> props = JSONHelper.createPropertiesForJaxbContext(Collections.<String, Object>emptyMap());
        Class[] classes = new Class[]{clazz};

        JAXBContext ctx = JAXBContext.newInstance(classes, props);

        JsonFactory factory = new JsonFactory();
        Writer osWriter = new OutputStreamWriter(System.out);
        JsonGenerator g;

        g = factory.createJsonGenerator(osWriter);

        Marshaller marshaller = ctx.createMarshaller();
        marshaller.marshal(bean, new Stax2JacksonWriter(g, clazz, ctx));

        g.flush();
        System.out.println("");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
