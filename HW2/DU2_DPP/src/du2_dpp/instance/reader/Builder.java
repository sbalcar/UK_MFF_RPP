/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.instance.reader;


import du2_dpp.instance.Instance;
import du2_dpp.instance.element.Element;
import du2_dpp.instance.section.Section;

/**
 *
 * @author stepan
 */
class Builder {

    private Instance instance;

    private Section lastUsedSection;
    private Element lastUsedElement;

    private String whiteSpaceBeforeElementIdentifier;
    private String whiteSpaceBeforeSectionName;

    public Builder() {
        instance = new Instance();
    }

    protected void addSectionName(String sectionName) {

        String nameWithoutLeftBracket =
                sectionName.substring(1, sectionName.length() -1);
        String nameWithoutRightBracket =
                nameWithoutLeftBracket.substring(0, sectionName.length() -2);
        Section section = new Section(nameWithoutRightBracket);

        instance.addSection(section);
        lastUsedSection = section;
    }

    protected void addSectionComment(String sectionComment) {

        lastUsedSection.setComment(sectionComment);
    }

    protected void addSectionWhiteSpaceBeforeName(String space) {
        whiteSpaceBeforeSectionName = space;
    }

    protected void addSectionWhiteSpaceBeforeComment(String space) {
        lastUsedSection.setWhiteSpaceBeforeName(whiteSpaceBeforeSectionName);
        lastUsedSection.setWhiteSpaceBeforeComment(space);
    }




    protected void addElementName(String elementName) {

        Element element = new Element(elementName);

        lastUsedSection.addElement(element);
        lastUsedElement = element;
    }

    protected void addElementValue(String elementValue) {

        lastUsedElement.setValue(elementValue);
    }

    protected void addElementComment(String elementComment) {

        lastUsedElement.setComment(elementComment);
    }


    protected void addElementWhiteSpaceBeforeIdentifier(String space) {
        this.whiteSpaceBeforeElementIdentifier = space;
    }

    protected void addElementWhiteSpaceAfterIdentifier(String space) {
        lastUsedElement.setWhitespaceBeforeIdentifier
                (whiteSpaceBeforeElementIdentifier);
        lastUsedElement.setWhitespaceAfterIdentifier(space);
    }

    protected void addElementWhiteSpaceBeforeValue(String space) {
        lastUsedElement.setWhitespaceBeforeValue(space);
    }

    protected void addElementWhiteSpaceAfterValue(String space) {
        lastUsedElement.setWhitespaceAfterValue(space);
    }


    protected void addWhiteSpaceLineComment(String whiteSpace, String comment) {

        Section lastSection = instance.getLastSection();

        if (lastSection == null) {

            instance.addCommentLinesBeforeFirstSection(whiteSpace + comment);
            return;
        }

        if (lastSection.getAllElements().isEmpty()) {

            lastUsedSection.addWhiteSpaceLineComment(whiteSpace + comment);
        } else {

            lastUsedElement.addWhiteSpaceLineComment(whiteSpace + comment);
        }
    }

    protected void myPrint() {

        this.instance.myPrint();
    }

    protected Instance getInstance() {

        return this.instance;
    }

}