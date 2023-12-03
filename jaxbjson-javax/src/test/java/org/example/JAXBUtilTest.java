package org.example;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JAXBUtilTest {
    private static final JAXBUtilTestClass TEST_CLASS = new JAXBUtilTestClass("value1", "value2");
    private static final String JSON_TEST_CLASS = "{\"field1\":\"value1\",\"field2\":\"value2\"}";
    private static final String XML_TEST_CLASS = "<TestClass>\n" +
            "    <field1>value1</field1>\n" +
            "    <field2>value2</field2>\n" +
            "</TestClass>\n";

    @Test
    void unmarshalFromJson_CorrectInputData_Success() throws JAXBException {
        JAXBUtilTestClass result = (JAXBUtilTestClass) JAXBUtil.unmarshalFromJson(JSON_TEST_CLASS, new JAXBUtilTestClass());

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(TEST_CLASS);
    }

    @Test
    void marshalToJson_CorrectInputData_Success() throws JAXBException, IOException {
        String result = JAXBUtil.marshalToJson(TEST_CLASS);

        assertEquals(result, JSON_TEST_CLASS);
    }

    @Test
    void unmarshal_CorrectInputData_Success() throws JAXBException {
        JAXBUtilTestClass result = JAXBUtil.unmarshal(XML_TEST_CLASS, JAXBUtilTestClass.class);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(TEST_CLASS);
    }

    @Test
    void marshal_CorrectInputData_Success() throws JAXBException, IOException {
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + XML_TEST_CLASS;

        String result = JAXBUtil.marshal(TEST_CLASS);

        assertEquals(result, expectedOutput);
    }

    @Test
    void marshalForCDATA_CorrectInputData_Success() throws JAXBException, IOException {
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + deleteWhitespace(XML_TEST_CLASS) + "\n";
        String result = JAXBUtil.marshalForCDATA(TEST_CLASS);

        assertEquals(result, expectedOutput);
    }
}

@XmlRootElement(name = "TestClass")
class JAXBUtilTestClass {
    @XmlElement(name = "field1")
    private String field1;
    @XmlElement(name = "field2")
    private String field2;

    JAXBUtilTestClass() {
    }

    JAXBUtilTestClass(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }
}

