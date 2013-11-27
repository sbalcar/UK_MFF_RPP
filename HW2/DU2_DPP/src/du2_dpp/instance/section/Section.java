/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.instance.section;

import du2_dpp.instance.InstanceException;
import du2_dpp.instance.element.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * Trida reprezentujici Instanci Sekce konfuguracniho souboru
 * @author stepan
 */
public class Section {

    private String name;
    private String comment;

    private String whitespaceBeforeName = "";
    private String whitespaceBeforeComment = "";

    private List<String> whiteSpaceLineComment = new ArrayList<String>();

    private List<Element> elements = new ArrayList<Element>();

    public Section(String name) {

        this.name = name;
        this.comment = "";
    }

    /** Funce vrati jmeno Sekce */
    public String getName() {
        return name;
    }

    /** Funce prida Element */
    public void addElement(Element element) throws InstanceException {

        this.addingTest(element);
        this.elements.add(element);
    }

    /** Funce vlozi Element na zadany index */
    public void addElement(int index, Element element) throws InstanceException {

        this.addingTest(element);
        this.elements.add(index, element);
    }

    /** Funce otestuje element pred vlozenim */
    private void addingTest(Element element) throws InstanceException {

        if (element == null) {
            String message = "Element is null";
            throw new InstanceException(message,-1);
        }

        for ( Element elemenetI : elements) {
            if (elemenetI.getName().equals(element.getName())){
                String message = "Element with name " + element.getName() +
                        "is contain yet";
                throw new InstanceException(message,-1);
            }
        }
    }

    /** Funkce prida komentar na nove radce */
    public void addWhiteSpaceLineComment(String line) {
        this.whiteSpaceLineComment.add(line);
    }

    /** Funce vrati Element podle zadaneho jmena */
    public Element getElement(String name) {

        for (Element element : elements) {

            if (name.equals(element.getName()) ) {
                return element;
            }
        }
        return null;
    }

    /** Funkce nastavi komentar na aktualni radce */
    public void setComment(String comment) {

        this.comment = comment;
    }

    /** Funkce vrati komentar na aktualni radce */
    public String getComment() {

        return this.comment;
    }

    /** Funkce nastavi bile misto pred jmenem sekce */
    public void setWhiteSpaceBeforeName(String space) {

        this.whitespaceBeforeName = space;
    }

    /** Funkce nastavi bile misto pred komentarem */
    public void setWhiteSpaceBeforeComment(String space) {

        this.whitespaceBeforeComment = space;
    }


    /** Funce vrati vsechny Elementy */
    public List<Element> getAllElements() {

        return this.elements;
    }

    /** Funce vrati vsechny jmena elementu teto sekce */
    public List<String> getElementNames() {

        List<String> elementNames = new ArrayList<String>();

        for (Element element : elements) {

            elementNames.add(element.getName());
        }

        return elementNames;
    }

    /** Funce vrati pocet Elementu v cele Sekci */
    public int getNumOfElements(){

        int counter = 0;
        for ( Element elementI : elements) {

            counter++;
        }

        return counter;
    }

    /** Funce vrati radky cele Sekci */
    public List<String> toStringLines() {
        
            String elementBody = whitespaceBeforeName + "[" + name +  "]" +
                    whitespaceBeforeComment + comment;
            
            List<String> resultLines = new ArrayList<String>();
            resultLines.add(elementBody);
            
            for (int commentI = 0;
                commentI < whiteSpaceLineComment.size(); commentI++) {
                resultLines.add(whiteSpaceLineComment.get(commentI));
            }

            for (int elementI = 0; elementI < elements.size(); elementI++) {
                List<String> elementLines
                        = elements.get(elementI).toStringLines();
                resultLines.addAll(elementLines);
            }
            return resultLines;
    }

}
