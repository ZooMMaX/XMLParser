package ru.zoommax;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * <p>XML reader class.</p>
 *
 * @author ZooMMaX
 * @version 1.0
 */
public class XMLReader extends TagObject {
    List<String> dataIn;
    ExecutorService executorService;

    /**
     * @param dataIn {@link List} of xml tags and values
     */
    public XMLReader(List<String> dataIn) throws ExecutionException, InterruptedException {
        this.dataIn = dataIn;
        read();
    }

    /**
     * @param dataIn {@link List} of xml tags and values
     * @param executorService {@link ExecutorService}
     */
    public XMLReader(List<String> dataIn, ExecutorService executorService) throws ExecutionException, InterruptedException {
        this.dataIn = dataIn;
        this.executorService = executorService;
        read();
    }

    /**
     * @param xml {@link String} of xml text
     */
    public XMLReader(String xml) throws ExecutionException, InterruptedException {
        this.dataIn = xmlCleaner(xml);
        read();
    }

    /**
     * @param xml {@link String} of xml text
     * @param executorService {@link ExecutorService}
     */
    public XMLReader(String xml, ExecutorService executorService) throws ExecutionException, InterruptedException {
        this.dataIn = xmlCleaner(xml);
        this.executorService = executorService;
        read();
    }

    /**
     * @param xml {@link File} xml
     */
    public XMLReader(File xml) throws ExecutionException, InterruptedException {
        String xmlStr = "";
        try {
            xmlStr = new String(Files.readAllBytes(xml.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.dataIn = xmlCleaner(xmlStr);
        read();
    }

    /**
     * @param xml {@link File} xml
     * @param executorService {@link ExecutorService}
     */
    public XMLReader(File xml, ExecutorService executorService) throws ExecutionException, InterruptedException {
        String xmlStr = "";
        try {
            xmlStr = new String(Files.readAllBytes(xml.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.dataIn = xmlCleaner(xmlStr);
        this.executorService = executorService;
        read();
    }

    private LinkedList<String> xmlCleaner(String xml){
        String clean = xml.replaceAll("\n", "");
        String[] tmp = clean.replace(">", ">\n").replace("<", "\n<").split("\n");
        LinkedList<String> out = new LinkedList<>( Arrays.asList(tmp));
        return out;
    }

    private void read() throws ExecutionException, InterruptedException {
        LinkedList<String> anyLevel = new LinkedList<>();
        List<HashMap<String, Object>> forPomObject = new ArrayList<>();
        HashMap<String, HashMap<String, String>> param = new HashMap<>();
        String openTag = "";
        String paramTagName = "";
        boolean tag = false;
        boolean tag2 = false;
        String openTag2 = "";
        for (String s : dataIn){
            if (s.contains("<") && !s.contains("<?") && !s.contains("<!")){
                if (s.contains(" ")) {
                    paramTagName = s.substring(0, s.indexOf(" ")).replace("<", "").replace(">", "");
                    StringBuilder space = new StringBuilder();
                    boolean need = false;
                    for (char ch : s.toCharArray()){
                        String c = ch+"";
                        if (c.equals("\"")&&!need){
                            need = true;
                        } else if (c.equals("\"")&&need) {
                            need = false;
                        }

                        if (need && c.equals(" ")){
                            space.append("\n");
                        }else {
                            space.append(c);
                        }
                    }
                    String[] p = space.substring(s.indexOf(" ")+1).replace("<", "").replace(">", "").split(" ");
                    HashMap<String, String> hashMapTmp = new HashMap<>();
                    for (String pa : p) {
                        pa = pa.replace("\n", " ").replace("\"", "");
                        String[] tmp = pa.split("=");
                        hashMapTmp.put(tmp[0], tmp[1]);
                        param.put(paramTagName, hashMapTmp);
                    }
                    s = "<"+paramTagName+">";
                }
            }
            if (!s.equals("") && !s.contains("<!") && !s.contains("<?")) {
                s = s.trim();
                if (s.contains("<") && !s.contains("/") && tag && !tag2 && !s.contains(openTag)) {
                    openTag2 = s.replace("<", "").replace(">", "");
                    tag2 = true;
                }
                if (tag2) {
                    anyLevel.add(s);
                }
                if (s.contains("<") && s.contains("/") && s.replace("<","").replace("/", "").replace(">","").equals(openTag2)) {
                    HashMap<String, Object> tmpHashMap = new HashMap<>();
                    LinkedList<String> tmpList = new LinkedList<>(anyLevel);
                    tmpHashMap.put("tag", openTag2);
                    tmpHashMap.put("data", tmpList);
                    forPomObject.add(tmpHashMap);
                    anyLevel.clear();
                    tag2 = false;
                    openTag2 = "";
                }
                if (s.contains("<") && !s.contains("/") && !tag) {
                    openTag = s.replace("<", "").replace(">", "");
                    tag = true;
                    this.setTagName(openTag);
                }
                if (s.contains("<") && s.contains("/") && tag && s.contains(openTag)) {
                    openTag = "";
                    tag = false;
                }

                if (!s.contains("<") && !s.contains("/") && tag && !tag2) {
                    this.setTagName(openTag);
                    this.setValue(s);
                    this.setTagParams(param);
                }
            }
        }
        if (executorService != null) {
            for (HashMap<String, Object> i : forPomObject) {
                String tagn = (String) i.get("tag");
                LinkedList<String> data = (LinkedList<String>) i.get("data");
                TagObject xo = (TagObject) executorService.submit(new MultithreadRead(data, executorService)).get();
                this.setData(tagn, xo);
            }
        }else {
            for (HashMap<String, Object> i : forPomObject) {
                String tagn = (String) i.get("tag");
                LinkedList<String> data = (LinkedList<String>) i.get("data");
                this.setData(tagn, new XMLReader(data));
            }
        }
    }
}

class MultithreadRead implements Callable {

    List<String> dataIn;
    ExecutorService executorService;
    public MultithreadRead(List<String> dataIn, ExecutorService executorService){
        this.dataIn = dataIn;
        this.executorService = executorService;
    }
    @Override
    public TagObject call() throws Exception {
        return new XMLReader(dataIn, executorService);
    }
}
