package org.example;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Map;
import java.util.WeakHashMap;

public class JAXBContextRegistry {
    private final Map<Class<?>, JAXBContext> classContexts = new WeakHashMap<Class<?>, JAXBContext>();

    JAXBContext getClassContext(Class<?> type) throws JAXBException {
        synchronized (classContexts) {
            JAXBContext context = classContexts.get(type);
            if (context == null) {
                context = JAXBContext.newInstance(type);
                classContexts.put(type, context);
            }
            return context;
        }
    }
}
