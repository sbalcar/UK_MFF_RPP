/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.configuration;

import du2_dpp.instance.Instance;
import du2_dpp.instance.element.Element;
import du2_dpp.instance.reader.RegexpExpression;
import du2_dpp.instance.section.Section;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Trida urcuje format instance
 * @author stepan
 */
public class Format {

    public static boolean IS_ARRAY = true;
    public static boolean IS_NOT_ARRAY = false;

    public static boolean IS_REQUIRED = true;
    public static boolean IS_NOT_REQUIRED = false;


    private String delimiter = ",";

    private List<SectionFormat> sectionsFormat = new ArrayList<SectionFormat>();

    public static boolean DEBUG = false;

    /** Funkce nastavi oddelovac */
    public void setDelimiter(String delimiter) throws FormatException {

        if (delimiter.length() != 1) {
            throw new FormatException("Delimiter is not corerect");
        }
        this.delimiter = delimiter;
    }

    /** Funkce vrati oddelovac */
    public String getDelimiter() {

        return delimiter;
    }

    /** Funkce prida sekci */
    public void addSection(String sectionName,
            boolean isRequired) throws FormatException {

        Pattern patternSection
                = Pattern.compile(RegexpExpression.nameSectionRegexp);
        Matcher matcherSection = patternSection.matcher(sectionName);

        boolean sectionOK =  matcherSection.find(0) &&
                matcherSection.group().equals(sectionName);
        if (! sectionOK) {
            String message = "Name of Section " + sectionName +
                    " don't have correct Format";
            throw new FormatException(message);
        }

        SectionFormat section = new SectionFormat(sectionName, isRequired);
        sectionsFormat.add(section);
    }

    /** Funkce vrati sekci jmenem */
    public SectionFormat getSectionFormats(String sectionName) {

        for ( SectionFormat sectionFormat : sectionsFormat) {

            if (sectionFormat.name.equals(sectionName)) {
                return sectionFormat;
            }
        }

        return null;
    }

    /** Funkce vrati Formaty vsech sekci */
    public List<SectionFormat> getSectionFormat() {

        return sectionsFormat;
    }

    /** Funkce prida element jmenem do pojmenovane sekce */
    public void addElement(String element, String sectionName,
            ElementTypes type, boolean isArrayType, String defaultValue,
            boolean isRequired ) throws FormatException {

        // Testovani
        // jestli existuje SectionName
        // jestli sectionName odpovida Regexpu
        // jestli defaultValue odpovida Regexpu

        ElementFormat elementFormat = new ElementFormat(element, type,
                isArrayType, defaultValue, isRequired);

        SectionFormat sectionFormat = null;
        for ( SectionFormat sectionFormatI : sectionsFormat) {

            if (sectionFormatI.name.equals(sectionName)){
                sectionFormat = sectionFormatI;
                break;
            }
        }

        if (sectionFormat == null) {
            String message = "Section with name " + sectionName +
                    " doesn't exist in format";
            throw new FormatException(message);
        }


        Pattern patternElement
                = Pattern.compile(RegexpExpression.identifierRegexp);
        Matcher matcherElement = patternElement.matcher(element);

        boolean elementOK =  matcherElement.find(0) &&
                matcherElement.group().equals(element);
        if (! elementOK) {
            String message = "Name of Elemenet " + element +
                    " don't have correct Format";
            throw new FormatException(message);
        }

        Pattern patternValue = Pattern.compile(RegexpExpression.valueRegexp);
        Matcher matcherValue = patternValue.matcher(defaultValue);

        boolean valueOK  = matcherValue.find(0) &&
                matcherValue.group().equals(defaultValue);
        if (! valueOK) {
            String message = "Default value " + defaultValue + " dont have " +
                    "correct format";
            throw new FormatException(message);
        }

        // rad bych otestoval i jestli odpovida defaultni hodnota formatu,
        // to ale nejde protoze v hodnote muze obsahovat i odkaz na neco co neni
        // jeste zadano

        sectionFormat.elements.add(elementFormat);
    }

    /** Funkce otestuje jestli instance odpovida aktualnimu Formatu */
    public boolean matchToFormat(Instance instance) throws FormatException {
 
        Match match = new Match(instance, this);
        match.instanceToFormatAndFormatToInstance();

        return true;
    }

    /** Funkce vypise defaultni konfiguraci Formatu do zadaneho souboru */
    public void writeDefaultConfiguration(File outputFile) throws IOException {

        List<String> lines = toStringLines();

        FileWriter fstream = new FileWriter(outputFile);
        BufferedWriter out = new BufferedWriter(fstream);

        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {

            out.write(lines.get(lineIndex));
            if (lineIndex < (lines.size() -1) ) {
                out.newLine();
            }
        }

        out.close();
        fstream.close();
    }

    /** Funkce vrati list radek defaultni instance*/
    protected List<String> toStringLines() {

        List<String> resultLines = new ArrayList<String>();

        for ( SectionFormat  sectionFormatI : sectionsFormat) {

            resultLines.addAll(sectionFormatI.toStringLines());
        }

        return resultLines;
    }


}


/**
 * Trida urcuje format Sekce
 * @author stepan
 */
class SectionFormat {

        protected String name;
        protected boolean isRequired;

        protected List<ElementFormat> elements = new ArrayList<ElementFormat>();

        protected SectionFormat(String sectionName, boolean isRequired) {

            this.name = sectionName;
            this.isRequired = isRequired;
        }

        /** Funkce vrati format Elementu podle zadaneho jmena elementu */
        protected ElementFormat getElementFormat(String elementName) {

            for ( ElementFormat  elementFormatI : elements) {

                if (elementFormatI.elementName.equals(elementName)) {
                    return elementFormatI;
                }
            }

            return null;
        }

        /** Funce vrati vsechny foraty Elemetu */
        protected List<ElementFormat> getElementFormats() {
            
            return elements;
        }

        /** Funkce vrati list radek defaultni instance */
        protected List<String> toStringLines() {

            String header = "[" + name + "]";

            List<String> resultLines = new ArrayList<String>();
            resultLines.add(header);

            for ( ElementFormat elementI : elements) {

                resultLines.add(elementI.toStringLine());
            }

            return resultLines;
        }
}

/**
 * Trida urcuje format Elementu
 * @author stepan
 */
class ElementFormat {

        protected String elementName;
        protected ElementTypes type;
        protected boolean isArrayType;
        protected String defaultValue;
        protected boolean isRequired;

        protected ElementFormat(String elementName, ElementTypes type,
                boolean isArrayType, String defaultValue, boolean isRequired ) {

            this.elementName = elementName;
            this.type = type;
            this.isArrayType = isArrayType;
            this.defaultValue = defaultValue;
            this.isRequired = isRequired;
        }

        /** Funkce vrati list radku deafaultni instance*/
        protected String toStringLine(){

            return elementName + " = " + defaultValue;
        }
}

/**
 * Trida rozhoduje jestli jde namapovat instance na format a format na instanci
 * @author stepan
 */
class Match {

    private Instance instance;
    private Format format;
    
    private Match()
    {}
    
    protected Match(Instance instance, Format format) {

        this.instance = instance;
        this.format = format;
    }

    /** Funkce rozhoduje jestli jde namapovat zadana instance na zadny format
     *  a zaroven zadany format na zadanou instanci.
     */
    protected boolean instanceToFormatAndFormatToInstance()
            throws FormatException {
        
        List<Section> sectionsInstance = instance.getSections();
        for ( Section sectionI : sectionsInstance) {

            SectionFormat sectionFormat
                    = format.getSectionFormats(sectionI.getName());

            if (sectionFormat == null) {
                String message = "For the Section defined in Instance " +
                        "doesn't exist any section in Format";
                System.out.println(message);
                throw new FormatException(message);
            }

            sectionInstanceToFormat(sectionI, sectionFormat);
        }

        List<SectionFormat> sectionsFormat = format.getSectionFormat();
        for ( SectionFormat sectionFormatI : sectionsFormat) {

            Section section = instance.getSection(sectionFormatI.name);

            if (section == null) {
                String message = "For the Section defined in Format doesn't " +
                        "exist any section in Instance";
                System.out.println(message);
                throw new FormatException(message);
            }

            sectionFormatToInstance(sectionFormatI, section);
        }

        return true;
    }

    /** Funkce rozhoduje jestli jde namapovat Instance Sekce na Format Sekce */
    private boolean sectionInstanceToFormat(Section section,
            SectionFormat sectionFormat) throws FormatException {

        List<Element> elements = section.getAllElements();

        for ( Element elementI : elements) {

            ElementFormat elementFormat =
                    sectionFormat.getElementFormat(elementI.getName());

            if (elementFormat == null) {
                String message = "For Element " + elementI.getName() +
                        " defined in Instance doesn't exist any Element" +
                        " in Format";
                System.out.println(message);
                throw new FormatException(message);
            }

            elementInstanceToFormat(elementI, elementFormat);
        }

        return true;
    }

    /** Funkce rozhoduje jestli jde namapovat Format Sekce na Instanci Sekce */
    private boolean elementInstanceToFormat(Element element,
            ElementFormat elementFormat) throws FormatException {

        List<String> values = parseValues(element.getValue(),
                format.getDelimiter(), instance.getNumOfElements());
        
        hasValuesGoodDimension(values, elementFormat.isArrayType);
        hasValuesGoodType(values, elementFormat.type);

        return true;
    }

    /** Funce rozparsuje jeden retezec na seznam hodnot podele oddelovace */
    private List<String> parseValues(String value, String delimiter,
            int decrementCounter) throws FormatException {

        // rozdeli podle zadaneho oddelovace
        String[] values = value.split(delimiter);
        List<String> lines = new ArrayList<String>();

        //projde cele pole hodnot
        for ( String valueI : values) {

            // oriznuti bilych znaku na zacatku a na konci
            String valueIWithOutWhiteSpace = valueI.trim();
            if (value.substring(value.length() -1).equals("\\")) {
                valueIWithOutWhiteSpace += " ";
            }

            List<String> valuesI = valuesFromPointer(valueIWithOutWhiteSpace,
                    delimiter, decrementCounter);
            lines.addAll(valuesI);
        }

        return lines;
    }

    /** Funce vraci seznam hodnot z pointu */
    private List<String> valuesFromPointer(String configPointer,
            String delimiter, int decrementCounter) throws FormatException {

        if ((configPointer.indexOf("{") < 0) &&
                (configPointer.indexOf("}") < 0) &&
                (configPointer.indexOf("$") < 0) &&
                (configPointer.indexOf("#") < 0)) {

            List<String> values = new ArrayList<String>();
            values.add(configPointer);
            return values;
        }

        //format odkazu  ${Sekce 1#Option 1}
        Pattern patternRererence =
                Pattern.compile(RegexpExpression.configurationReference);
        Matcher matcherRererence = patternRererence.matcher(configPointer);

        if (Format.DEBUG) {
            System.out.println("configPointer:" + configPointer);
        }
        
        String sectionName = "";
        String elementName = "";
        if (matcherRererence.find() &&
                matcherRererence.group().equals(configPointer) ) {

            if (Format.DEBUG) {
                System.out.println("ConfigPointer --" + configPointer + "--");
            }
            String sectionNameRegexp = RegexpExpression.nameSectionRegexp;
            Pattern sectionPattern = Pattern.compile(sectionNameRegexp);
            Matcher sectionRererence = sectionPattern.matcher(configPointer);
            sectionRererence.find(configPointer.indexOf("{"));
            sectionName = sectionRererence.group();

            String elementNameRegexp = RegexpExpression.identifierRegexp;
            Pattern elementPattern = Pattern.compile(elementNameRegexp);
            Matcher elementRererence = elementPattern.matcher(configPointer);
            elementRererence.find(configPointer.indexOf("#"));
            elementName = elementRererence.group();

            if (Format.DEBUG) {
                System.out.println("SECTION: --" + sectionName + "--");
                System.out.println("ELEMENT: --" + elementName + "--");
            }
        } else {
            String message = "Bad value of reference --" + configPointer + "--";
            System.out.println(message);
            throw new FormatException(message);
        }

 
        Section section = instance.getSection(sectionName);
        if(section == null) {
            String message = "Section ,," + sectionName + ",, doesnt exist";
            System.out.println(message);
            throw new FormatException(message);
        }

        Element element = section.getElement(elementName);
        if(element == null) {
            String message = "Element ,," + elementName + ",, doesnt exist";
            System.out.println(message);
            throw new FormatException(message);
        }

        String nextValue = element.getValue();

        if (decrementCounter == 0) {

            String message = "Referneces created cycle";
            System.out.println(message);
            throw new FormatException(message);
        }

        List<String> resultLines
                = parseValues(nextValue, delimiter, decrementCounter--);
        return resultLines;
    }


    /** Funce kontroluje jestli maji hodnoty elemementu spravnou dimenzi */
    private void hasValuesGoodDimension(List<String> values,
            boolean isArrayType) throws FormatException {

        boolean isArray = values.size() > 1;

        if ((isArray) && (! isArrayType)) {
            String message = "Element has different dimension as Format";
            System.out.println(message);
            throw new FormatException(message);
        }

        if ((!isArray) && (isArrayType)) {
            String message = "Element has different dimension as Format";
            System.out.println(message);
            throw new FormatException(message);
        }
    }

    /** Funkce otestuje jestli seznam hodnot muze nabyvat zadaneho typu */
    private void hasValuesGoodType(List<String> values,
            ElementTypes elementType) throws FormatException {
    
        for ( String valueI : values) {

            if (!elementType.checkCorrectValue(valueI)) {
                String message = "Bad format of value --" + valueI + "--";
                System.out.println(message);
                throw new FormatException(message);
            }
            //System.out.println("Good format of value --" + valueI + "--");
        }
    }


    /** Funkce rozhoduje jestli jde namapovat Instance Formatu na
     * Instanci Sekce
     */
    private boolean sectionFormatToInstance(SectionFormat sectionFormat,
            Section section) throws FormatException {

        List<ElementFormat> elementsFormat = sectionFormat.getElementFormats();

        for ( ElementFormat elementFormatI : elementsFormat) {

            if (! elementFormatI.isRequired) {
                continue;
            }

            Element element = section.getElement(elementFormatI.elementName);

            if (element == null) {
                String message = "For Section " + elementFormatI.elementName +
                        " defined in Format doesn't exist any section " +
                        "in instance";
                System.out.println(message);
                throw new FormatException(message);
            }
        }

        return true;
    }

}