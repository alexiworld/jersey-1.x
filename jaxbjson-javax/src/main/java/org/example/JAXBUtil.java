package org.example;

import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONUnmarshaller;
import org.apache.commons.lang3.CharEncoding;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class JAXBUtil {

    private static JAXBContextRegistry jaxbContextRegistry = new JAXBContextRegistry();

    public static Object unmarshalFromJson(String jsonStr, Object object) throws JAXBException {
        JAXBContext jc = jaxbContextRegistry.getClassContext(object.getClass());
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        JSONUnmarshaller jsonUnmarshaller = JSONJAXBContext.getJSONUnmarshaller(unmarshaller, jc);
        Object obj = jsonUnmarshaller.unmarshalFromJSON(new StringReader(jsonStr),
                object.getClass());

        return obj;
    }

    public static String marshalToJson(Object entity) throws JAXBException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JAXBContext jc = jaxbContextRegistry.getClassContext(entity.getClass());

        Marshaller marshaller = jc.createMarshaller();
        JSONMarshaller jsonMarshaller = JSONJAXBContext.getJSONMarshaller(marshaller, jc);
        jsonMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jsonMarshaller.setProperty(Marshaller.JAXB_ENCODING, CharEncoding.UTF_8);
        jsonMarshaller.marshallToJSON(entity, baos);

        return String.valueOf(baos);
    }

    public static <E> E unmarshal(String xmlStr, Class<E> clazz) throws JAXBException {
        JAXBContext jc = jaxbContextRegistry.getClassContext(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(new StringReader(xmlStr));

        return clazz.cast(obj);
    }

    public static String marshal(Object entity) throws JAXBException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JAXBContext jc = jaxbContextRegistry.getClassContext(entity.getClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, CharEncoding.UTF_8);
        marshaller.marshal(entity, baos);

        return String.valueOf(baos);
    }

    @SuppressWarnings("deprecation")
    public static String marshalForCDATA(Object entity) throws JAXBException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JAXBContext jc = jaxbContextRegistry.getClassContext(entity.getClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, CharEncoding.UTF_8);

        XMLSerializer serializer = getXMLSerializer(baos);

        marshaller.marshal(entity, serializer);

        return String.valueOf(baos);
    }

    @SuppressWarnings("deprecation")
    private static XMLSerializer getXMLSerializer(ByteArrayOutputStream baos) {

        OutputFormat of = new OutputFormat();
        of.setCDataElements(new String[]{"^userPassword"}); //
        of.setPreserveSpace(true);
        of.setIndenting(true);

        XMLSerializer serializer = new XMLSerializer(of);

        serializer.setOutputByteStream(baos);

        return serializer;
    }
}
