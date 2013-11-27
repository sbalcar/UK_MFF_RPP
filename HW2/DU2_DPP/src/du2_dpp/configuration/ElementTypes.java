/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.configuration;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Trida repreznetujici datove typy elemetu
 * @author stepan
 */
public enum ElementTypes {

    BOOLEAN (new BooleanChecker()),
    SIGNED (new SignedChecker()),
    UNSIGNED (new UnSignedChecker()),
    FLOAT (new FloatChecker()),
    STRING (new StringChecker()),
    ENUM (new EnumChecker());

    private TypeChecker checker;
    private List<String> values = null;

    ElementTypes(TypeChecker checker) {

        this.checker = checker;
    }

    public void setCoorectValues(List<String> values) {

        this.values = values;
    }

    /** Funce otestuje jestli je hodnota zadaneho datoveho typu validni */
    public boolean checkCorrectValue(String value) throws FormatException {

        if (values != null) {
            return values.indexOf(value) > -1;
        }

        return checker.check(value);
    }

}

/**
 * Abstraktni trida urcena pro testovani datovych typu
 * @author stepan
 */
abstract class TypeChecker {

    /** Funkce otestuje jesti je hodnota validni */
    abstract public boolean check(String value) throws FormatException;
}

/**
 * Trida testujici korektnost Booleanovske hodnoty elementu
 * @author stepan
 */
class BooleanChecker extends TypeChecker {

    /** Funkce otestuje jesti je hodnota validni */
    public boolean check(String value) throws FormatException {

        //{ 0, f, n, off, no, disabled }
        //{ 1, t, y, on, yes, enabled }
        String valuesRegexp = "0|f|n|off|no|disabled" + "|1|t|y|on|yes|enabled";

        Pattern pattern = Pattern.compile(valuesRegexp);
        Matcher matcher = pattern.matcher(value);

        boolean patternFound = matcher.find();
        if (patternFound && matcher.group().equals(value)) {
        } else {
            String message = "Boolean Element " + value + " doesnt contntain" +
                    " correct Boolean-Value";
            System.out.println(message);
            throw new FormatException(message);
        }

        return patternFound;
    }

}

/**
 * Trida testujici korektnost Stringove hodnoty elementu
 * @author stepan
 */
class StringChecker extends TypeChecker {

    /** Funkce otestuje jesti je hodnota validni */
    public boolean check(String value) throws FormatException {

        for (int index = 0; index < value.length(); index++) {
            //  ',', ':' a ';'
            boolean contain1 = value.substring(index, index+1).equals(",");
            boolean contain2 = value.substring(index, index+1).equals(":");
            boolean contain3 = value.substring(index, index+1).equals(";");

            if (contain1 || contain2 || contain3) {
                if (index == 0 ||
                        (!value.substring(index-1, index).equals("\\"))) {

                    String message = "String Element contains some of this " +
                            "chars {, :, ;} which are not alowed";
                    throw new FormatException(message);
                }
            }
        }

        if (Format.DEBUG) {
            System.out.println("Value: " + value + " match to string");
        }
        return true;
    }

    public boolean containsChar(String value, char specialChar) {

        int index = 0;
        int indexTempl = 0;
        while ((indexTempl = value.indexOf(specialChar, index)) > 0) {

            if (index == 0) {
                return true;
            }

            index = indexTempl;
            String subst = value.substring(index-1, index);
            if (! subst.equals("\\")) {
                return false;
            }
        }

        return false;
    }
}

/**
 * Trida testujici korektnost Signed hodnoty elementu
 * @author stepan
 */
class SignedChecker extends TypeChecker {

    public String MAX_VALUE = "+9223372036854775807";
    public String DOWN_VALUE = "-9223372036854775808";

    /** Funkce otestuje jesti je hodnota validni */
    @Override
    public boolean check(String value) throws FormatException {

        String valuesRegexp = "[+|-]{0,1}[1-9][0-9]*";
        Pattern pattern = Pattern.compile(valuesRegexp);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find() && matcher.group().equals(value)) {
            if (checkDecadic(value)) {
                if (Format.DEBUG) {
                    System.out.println("Value: " + value + " match to decadic");
                }
                return checkDecadic(value);
            }
        }

        return checkBinaryOctaHexa(value);
    }

    /** Funkce otestuje jesti je hodnota ve dvojkove, osmickove,
     * sestnactkove soustave
     */
    protected boolean checkBinaryOctaHexa(String value) throws FormatException {

        String binaryRegexp = "0b[0|1]{1,64}";
        Pattern binaryPattern = Pattern.compile(binaryRegexp);
        Matcher binaryMatcher = binaryPattern.matcher(value);
        if (binaryMatcher.find() && binaryMatcher.group().equals(value)) {
            if (Format.DEBUG) {
                System.out.println("Value: " + value + " match to binary");
            }
            return true;
        }

        String octaRegexp = "0[0-7]{1,21}";
        Pattern octaPattern = Pattern.compile(octaRegexp);
        Matcher octaMatcher = octaPattern.matcher(value);
        if (octaMatcher.find() && octaMatcher.group().equals(value)) {
            if (Format.DEBUG) {
                System.out.println("Value: " + value + " match to octa");
            }
            return true;
        }

        String hexaRegexp = "0x[0-9|A-F|a-f]{1,16}";
        Pattern hexaPattern = Pattern.compile(hexaRegexp);
        Matcher hexaMatcher = hexaPattern.matcher(value);
        if ( hexaMatcher.find() && hexaMatcher.group().equals(value)) {
            if (Format.DEBUG) {
                System.out.println("Value: " + value + " match to hexa");
            }
            return true;
        }

        String message = "Signed Element " + value +
                " doesnt have correct format";
        System.out.println(message);
        throw new FormatException(message);

    }

    /** Funkce otestuje jesti je hodnota v desitkove soustave */
    protected boolean checkDecadic(String value) throws FormatException{

        String valuesRegexp = "[+|-]{0,1}[0-9]+";
        Pattern pattern = Pattern.compile(valuesRegexp);
        Matcher matcher = pattern.matcher(value);
        if (! matcher.find()){
            String message = "Format of decadic number is not correct";
            System.out.println(message);
            throw new FormatException(message);
        }

        if ((value.charAt(0) != '-') || (value.charAt(0) != '+')) {
            value = "+" + value;
        }

        boolean isCorrect;
        if ((value.charAt(0) == '+')) {

            isCorrect = isValue1Smaller(value, MAX_VALUE);
        } else {

            isCorrect = isValue1Smaller(DOWN_VALUE, value);
        }

        if (!isCorrect) {
            String message = "Signed Element contains value " + value +
                    " which is not alowed";
            throw new FormatException(message);
        }

        return isCorrect;
    }

    /** Funkce porovnava kladna cisla v desitkove soustave ve formatu string,
     *  znamenka funkce nezavazuje
     */
    protected boolean isValue1Smaller(String value1, String value2) {

        if (value1.length() < value2.length()) {
            return true;
        } else if (value1.length() > value2.length()) {
            return false;
        }

        // v pripade ze maji stejnou delku, zacnu poravnavat cifry
        for (int index = 0; index < value1.length(); index++) {

            char ch1 = value1.charAt(index);
            char ch2 = value1.charAt(index);

            if (ch1 > ch2) {
                return false;
            }
        }

        return true;
    }

}

/**
 * Trida testujici korektnost Unsigned hodnoty elementu
 * @author stepan
 */
class UnSignedChecker extends SignedChecker {

    public String MAX_VALUE = "18446744073709551614";
    public String DOWN_VALUE = "0";

    /** Funkce otestuje jesti je hodnota validni */
    @Override
    public boolean check(String value) throws FormatException{

        String valuesRegexp = "[1-9][0-9]*";
        Pattern pattern = Pattern.compile(valuesRegexp);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find() && matcher.group().equals(value)) {
            if (checkDecadic(value)) {
                if (Format.DEBUG) {
                    System.out.println("Value: " + value + " match to decadic");
                }
                return checkDecadic(value);
            }
        }

        return checkBinaryOctaHexa(value);
    }

    /** Funkce otestuje jesti je hodnota v desitkove soustave */
    @Override
    public boolean checkDecadic(String value) throws FormatException{

        String valuesRegexp = "[0-9]+";
        Pattern pattern = Pattern.compile(valuesRegexp);
        Matcher matcher = pattern.matcher(value);
        if (! matcher.find()){
            return false;
        }

        if ((value.charAt(0) != '-') || (value.charAt(0) != '+')) {
            value = "+" + value;
        }

        boolean isCorrect;
        if ((value.charAt(0) == '+')) {

            isCorrect = isValue1Smaller(value, MAX_VALUE);
        } else {

            isCorrect = isValue1Smaller(DOWN_VALUE, value);
        }

        if (! isCorrect) {
            String message
                    = "Signed Element contains value which is not alowed";
            System.out.println(message);

            throw new FormatException(message);
        }

        return isCorrect;
    }

}


/**
 * Trida testujici korektnost Float hohonty elementu
 * @author stepan
 */
class FloatChecker extends TypeChecker {

    /** Funkce otestuje jesti je hodnota validni */
    @Override
    public boolean check(String value) throws FormatException {

        //min/-1.79769e+308
        String floatRegexp = "[+|-]{0,1}[0-9]+\\.[0-9]+" +
                "([e|E][+|-]{0,1}[1-9][0-9]{0,2}){0,1}";

        Pattern floatPattern = Pattern.compile(floatRegexp);
        Matcher floatMatcher = floatPattern.matcher(value);
        if ((floatMatcher.find()) && (floatMatcher.group().equals(value))) {
        } else {
            String message = "Float Element doesnt have correct format";
            if (Format.DEBUG) {
                System.out.println(message);
            }
            throw new FormatException(message);
        }

        String valueCorrectedE = value.replace('e', 'E');
        String valueCorrectedPlus = valueCorrectedE.replace("+", "");

        try {
            Double doubleValue = Double.parseDouble(valueCorrectedPlus);
            String stringValue = doubleValue.toString();

            if ( stringValue.equals("-Infinity") ||
                    stringValue.equals("Infinity")) {
                
                String message = "Float Element Overflow";
                if (Format.DEBUG) {
                    System.out.println(message);
                }
                throw new FormatException(message);
            }

        } catch (NumberFormatException e) {

            String message = "Float Element contains value which is not alowed";
            if (Format.DEBUG) {
                System.out.println(message);
            }
            throw new FormatException(message);
        }

        return true;
    }

}


/**
 * Trida testujici korektnost Enum hohonty elementu
 * @author stepan
 */
class EnumChecker extends TypeChecker {

    /** Funkce otestuje jesti je hodnota validni */
    @Override
    public boolean check(String value) throws FormatException {

        return false;
    }
}
