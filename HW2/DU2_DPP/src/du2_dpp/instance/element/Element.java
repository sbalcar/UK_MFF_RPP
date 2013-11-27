/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.instance.element;

import java.util.ArrayList;
import java.util.List;

/**
 * Trida Element pro Instanci konfigurace
 * @author stepan
 */
public class Element {

    private String name = null;
    private String value = null;
    private String comment = "";

    private String whitespaceBeforeIdentifier = "";
    private String whitespaceAfterIdentifier = "";
    private String whitespaceBeforeValue = "";
    private String whitespaceAfterValue = "";

    List<String> whiteSpaceLineComment = new ArrayList<String>();

    /* Konstruktor se jmenem Elementu */
    public Element(String name) {
        this.setName(name);
    }

    public Element() {
    }

    /** Funkce nastavi jmeno */
    public void setName(String name) {
        this.name = name;
    }

    /** Funkce vrati jmeno */
    public String getName() {
        return this.name;
    }

    /** Funkce nastavi hodnotu */
    public void setValue(String value) {
        this.value = value;
    }

    /** Funkce vrati hodnotu */
    public String getValue() {
        return value;
    }

    /** Funkce nastavi bile misto pred identifikatorem */
    public void setWhitespaceBeforeIdentifier(String space) {
        this.whitespaceBeforeIdentifier = space;
    }

    /** Funkce nastavi bile misto za identifikatorem */
    public void setWhitespaceAfterIdentifier(String space) {
        this.whitespaceAfterIdentifier = space;
    }

    /** Funkce nastavi bile misto pred hodnotou */
    public void setWhitespaceBeforeValue(String space) {
        this.whitespaceBeforeValue = space;
    }

    /** Funkce nastavi bile misto za hodnotou */
    public void setWhitespaceAfterValue(String space) {
        this.whitespaceAfterValue = space;
    }

    /** Funkce prida komentar na nove radce */
    public void addWhiteSpaceLineComment(String line) {
        this.whiteSpaceLineComment.add(line);
    }

    /** Funkce nastavi komentar na aktualni radce */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /** Funkce vrati komentar na aktualni radce */
    public String getComment() {
        return comment;
    }

    /** Funkce vrati radky instance*/
    public List<String> toStringLines() {

        String elementBody = whitespaceBeforeIdentifier + name +
            whitespaceAfterIdentifier + "=" +
            whitespaceBeforeValue + value +
            whitespaceAfterValue + comment;

        List<String> resultLines = new ArrayList<String>();
        resultLines.add(elementBody);
        resultLines.addAll(whiteSpaceLineComment);

        return resultLines;
    }
    
}
