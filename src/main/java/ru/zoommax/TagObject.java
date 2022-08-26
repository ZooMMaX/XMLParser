package ru.zoommax;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * <p>XML object class.</p>
 *
 * @author ZooMMaX
 * @version 1.0
 */
public class TagObject {
    private LinkedHashMap<String, TagObject> data = new LinkedHashMap<>();
    private String tagName = "";
    private String value = "";

    private HashMap<String, HashMap<String, String>> tagParams = new HashMap<>();

    /**
     * @return {@link HashMap<String,HashMap<String,String>}
     * (tag name, HashMap(param name, value))
     */
    public HashMap<String, HashMap<String, String>> getTagParams() {
        return tagParams;
    }

    public void setTagParams(HashMap<String, HashMap<String, String>> params) {
        this.tagParams = params;
    }

    /**
     * @param tagName {@link String} tag name
     * @return {@link TagObject}
     * @throws {@link XMLException}
     */
    public TagObject getTagData(String tagName) throws XMLException {
        if (containsTagName(tagName)) {
            return data.get(tagName + 0);
        }else {
            throw new XMLException(tagName, 0);
        }
    }

    /**
     * @param tagName {@link String} tag name
     * @param tagIndex {@link Integer} tag index
     * @return {@link TagObject}
     * @throws {@link XMLException}
     */
    public TagObject getTagData(String tagName, int tagIndex) throws XMLException {
        if (containsTagName(tagName)) {
            return data.get(tagName + tagIndex);
        }else {
            throw new XMLException(tagName, tagIndex);
        }
    }

    /**
     * @param tagName {@link String} tag name
     * @return {@link Boolean} true if {@link TagObject} contains {@link String} tag name
     */
    public boolean containsTagName(String tagName){
        return data.containsKey(tagName+0);

    }


    /**
     * @param tagName {@link String} tag name
     * @param tagIndex {@link Integer} tag index
     * @return {@link Boolean} true if {@link TagObject} contains {@link String} tag name of {@link Integer} tag index
     */
    public boolean containsTagName(String tagName, int tagIndex){
        return data.containsKey(tagName+tagIndex);

    }

    /**
     * @return {@link LinkedHashMap} of nested tags
     */
    public LinkedHashMap<String, TagObject> getData() {
        return data;
    }

    public void setData(String key, TagObject pomObject){
        int x = 0;
        while (data.containsKey(key+x)){
            x++;
        }
        data.put(key+x, pomObject);
    }

    /**
     * @return {@link String} of current tag name
     */
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * @return {@link String} of current tag value
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
