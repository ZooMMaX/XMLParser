package ru.zoommax;

/**
 * <p>XML exception class.</p>
 *
 * @author ZooMMaX
 * @version 1.0
 */
public class XMLException extends Exception{
    /**
     * @param tagName {@link String} of tag name
     * @param tagIndex {@link Integer} of tag index
     */
    public XMLException(String tagName, int tagIndex){
        super("Tag \""+tagName+"\""+" index \""+tagIndex+"\" not found");
    }
}
