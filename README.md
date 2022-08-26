# XML PARSER

---

![Maven Central](https://img.shields.io/maven-central/v/ru.zoommax/XMLParser?style=plastic)
![GitHub](https://img.shields.io/github/license/ZooMMaX/XMLParser?style=plastic)
[![GitHub issues](https://img.shields.io/github/issues/ZooMMaX/XMLParser?style=plastic)](https://github.com/ZooMMaX/XMLParser/issues)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/ZooMMaX/XMLParser/Build?style=plastic)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/ZooMMaX/XMLParser/Maven%20Central%20deploy?style=plastic)

- Parses xml as nested XMLObjects
- Runs on one or more threads
- Getting tag parameters

An example of usage is available in src/tests/java/Main.java

### parse xml one thread
```java
public class Main {
    public static void main(String[] args) {
        String xml = "xml format string";
        XMLReader xmlReader = new XMLReader(xml);
    }
}
```

### parse xml multi thread
```java
public class Main {
    public static void main(String[] args) {
        String xml = "xml format string";
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        XMLReader xmlReader = new XMLReader(xml, service);
        service.shutdown();
    }
}
```

### get Tag object
XMLReader extends TagObject.
```java
public class Main {
    public static void main(String[] args) {
        String xml = "<root>" +
                "       <data>value1</data>" + //parsed as data0
                "       <data>value2</data>" + //parsed as data1
                "   </root>";
        XMLReader xmlReader = new XMLReader(xml);
        
        System.out.println(xmlReader.getTagName());
        //print: root
        
        //get data tags
        System.out.println(xmlReader.getTagData("data").getValue);
        System.out.println(xmlReader.getTagData("data", 0).getValue);
        //print: value1

        System.out.println(xmlReader.getTagData("data", 1).getValue);
        //print: value2

        System.out.println(xmlReader.getData());
        //print HashMap
        //{data0=ru.zoommax.XMLReader@282ba1e, data1=ru.zoommax.XMLReader@13b6d03}
    }
}
```

### get tag params

```java
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        String xml = "<root param1=\"a\" param2=\"b\">" +
                "       <data>value1</data>" + //parsed as data0
                "       <data>value2</data>" + //parsed as data1
                "   </root>";
        XMLReader xmlReader = new XMLReader(xml);
        System.out.println(xmlReader.getTagParams());
        //print HashMap<String, HashMap<String, String>
        //             <tag name,    <param name, value>
        //{root={param1=a, param2=b}}
        HashMap<String, HashMap<String, String>> hm = xmlReader.getTagParams();
        System.out.println(hm.get("root").get("param1"));
        //print: a
    }
}
```

### Dependecy
![dependency maven](https://img.shields.io/badge/DEPENDENCY-Maven-C71A36?style=plastic&logo=apachemaven)
```xml
<dependencies>
    <dependency>
        <groupId>ru.zoommax</groupId>
        <artifactId>XMLReader</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

![dependency gradle](https://img.shields.io/badge/DEPENDENCY-Gradle-02303A?style=plastic&logo=gradle)
```groovy
implementation 'ru.zoommax:XMLReader:1.0'
```
