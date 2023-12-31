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
package com.sun.jersey.json.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author japod
 */
@XmlRootElement
public class SimpleBeanWithAttributes {

    @XmlAttribute public URI uri;
    public String s1;
    @XmlAttribute public int i;
    @XmlAttribute public String j;

    public SimpleBeanWithAttributes() {
    }

    public static Object createTestInstance() {
        SimpleBeanWithAttributes instance = new SimpleBeanWithAttributes();
        instance.s1 = "hi there";
        instance.i = 312;
        instance.j = "bumper";
        try {
            instance.uri = new URI("http://localhost:8080/jedna/bedna/");

        } catch (URISyntaxException ex) {
            Logger.getLogger(SimpleBeanWithAttributes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleBeanWithAttributes other = (SimpleBeanWithAttributes) obj;
        if (this.s1 != other.s1 && (this.s1 == null || !this.s1.equals(other.s1))) {
            return false;
        }
        if (this.j != other.j && (this.j == null || !this.j.equals(other.j))) {
            return false;
        }
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        if (this.i != other.i) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (null != s1) {
            hash += 17 * s1.hashCode();
        }
        if (null != j) {
            hash += 17 * j.hashCode();
        }
        if (null != uri) {
            hash += 17 * uri.hashCode();
        }
        hash += 13 * i;
        return hash;
    }

    @Override
    public String toString() {
        return (new Formatter()).format("SBWA(%s,%d,%s,%s)", s1, i, j, uri).toString();
    }
}
