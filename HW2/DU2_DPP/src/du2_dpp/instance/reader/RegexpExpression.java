/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.instance.reader;

/**
 * Funkce obsahuje regularni vyrazy pro parsovani konfiguracniho souboru
 * @author stepan
 */
public class RegexpExpression {

    //{ a-z, A-Z, . , $, : }
    private static String startOfSectionRegexp = "([a-zA-Z]|\\.|\\$|\\:)";
    //{ a-z, A-Z, 0-9, _, ~, -, ., :, $, mezera }
    private static String bobyOfSectionRegexp
            = "([a-zA-Z0-9]|\\_|\\~|\\-|\\.|\\:|\\$|\\ )";
    private static String endOfSectionRegexp
            = "([a-zA-Z0-9]|\\_|\\~|\\-|\\.|\\:|\\$)";
    public static String nameSectionRegexp = startOfSectionRegexp +
            "(" + bobyOfSectionRegexp + "*" + endOfSectionRegexp + ")*";

    public static String sectionRegexp = "\\[" + nameSectionRegexp + "\\]";

    //{ a-z, A-Z, . , $, : }
    private static String startOfIdentifierRegexp = "([a-zA-Z]|\\.|\\$|\\:)";
    //{ a-z, A-Z, 0-9, _, ~, -, ., :, $, mezera }
    private static String bobyOfIdentifierRegexp
            = "([a-zA-Z0-9]|\\_|\\~|\\-|\\.|\\:|\\$|\\ )";
    private static String endOfIdentifikatorRegexp
            = "([a-zA-Z0-9]|\\_|\\~|\\-|\\.|\\:|\\$)";
    public static String identifierRegexp = startOfIdentifierRegexp +
            "(" + bobyOfIdentifierRegexp + "*" + endOfIdentifikatorRegexp + ")*";

    public static String equalRegexp = "\\=";

    public static String valueRegexp = "[^; ]([^;]*[^; ])*";

    public static String commentRegexp = "\\;(.)*$";

    public static String whiteSpaceRegexp = "(\\s)*";

    public static String configurationReference
            = "\\$\\{" + nameSectionRegexp + "\\#" + identifierRegexp + "\\}";

    //LINES
    public static String whiteSpaceCommentLine = "^(\\s)*(\\;(.)*)*$";

    public static String sectionHeaderLine = "^" + whiteSpaceRegexp +
            sectionRegexp;

    public static String elementHeaderLine = "^" +
            whiteSpaceRegexp + identifierRegexp + whiteSpaceRegexp + equalRegexp
            + whiteSpaceRegexp + valueRegexp;


}
