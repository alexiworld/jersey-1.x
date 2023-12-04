# jersey-1.x
A fork of com.sun.jersey:jersey-json:1.19.4 that uses JakartaEE10 and Jackson2, can be used with Spring 3+.

This repository is dedicated on jersey-1.x, more specifically on jersey-1.x/jersey-json used to (un)marshal JAXB and JSON. The original source code depends on javax.xml.bind and other obsolete packages (e.g. Sun's internal packages), which are not to be found or suported in most recent versions of JDK versions. Furthermore, Spring 3+ has moved from javax.xml.bind, javax.persistence, and other javax packages to Jakarta based packages. However, not all of the frameworks, libraries, and packages have been migrated to Jakarta and therefore, some of the older code cannot seemlessly migrated from Spring 2+ to 3+. The work on jersey-json falls under the latter category and hopefully can help others with their upgrade plans.

```
JAXBContext context = JAXBContext.newInstance(type);
Unmarshaller unmarshaller = context.createUnmarshaller();
JSONUnmarshaller jsonUnmarshaller = JSONJAXBContext.getJSONUnmarshaller(unmarshaller, context);
Object object = jsonUnmarshaller.unmarshalFromJSON(new StringReader(jsonString), type);
```

# Modules 

- **jersey-core** : Contains the original code of jersey-core sparse checked out from [Jersey-1.x](https://github.com/javaee/jersey-1.x/tree/master) repository
- **jersey-json** : Contains the original code of jersey-json sparse checked out from [PJ Fanning](https://github.com/pjfanning/jersey-1.x/tree/master/jersey-json) repository
- **denim-core** : Contains the modified code of jersey-core integrated with JakartaEE10 
- **denim-json** : Contains the original code of jersey-json integrated with JakartaEE10 (it uses Jakarta, Glassfish JAXB - the "good" old com.sun.xml.bind/jaxb-impl packages, Jackson 2)
- **jaxbjson-javax** : Contains a test class that marshal and unmarshal XML and JSON using jersey-json library based on javax.xml.bind
- **jaxbjson-javax** : Contains a test class that marshal and unmarshal XML and JSON using denim-json library based on jakarta.xml.bind
