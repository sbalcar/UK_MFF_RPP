/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.instance.reader;


import du2_dpp.instance.reader.Reader.MatchResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Trida nacita ze souboru Instanci.
 * @author stepan
 */
public class Reader {

    private File file;

    private List<String> fileLines;
    private int currentNumOfRow;
    private int currentNumOfColumn;

    private Builder builder;

    private boolean RESULT_OK = true;
    private boolean DONT_RESULT_OK = false;
    private String ANY_ERROR = "";

    private boolean DEBUG = false;

    public Reader(File file) {
        this.file = file;
    }

    /** Funckce nacte ze souboru seznam radek */
    public List<String> readLines() throws FileNotFoundException, IOException {

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        List<String> fileLine = new ArrayList<String>();

        String value;
        while ((value = br.readLine()) != null) {
            fileLine.add(value);
        }

        return fileLine;
    }

    /** Funkce nacte Instanci a vrati ji ve tride ReadingResult */
    public ReadingResult read() throws FileNotFoundException, IOException {

        this.currentNumOfRow = 0;
        this.currentNumOfColumn = 0;
        this.fileLines = this.readLines();

        builder = new Builder();

        ParseResult sectionORElementi = readUntilSectionOrElement();

        while(fileLines.size() != currentNumOfRow) {

            ParseResult sectionResult = readSection();
            if (! sectionResult.result) {
                ReadingResult readingResult =
                        new ReadingResult(sectionResult, builder.getInstance());
                return readingResult;
            }
        }

        ReadingResult result = new ReadingResult();
        result.resultOK = true;
        result.error = "";
        result.instance = builder.getInstance();
        return result;
    }

    /** Funce nacte Sekci a ulozi to co nacetla do tridy Builder */
    private ParseResult readSection()
    {

        ParseResult headerResult = readHeaderOfSection();
        if (! headerResult.result) {
            return headerResult;
        }

        ParseResult bodyResult = readBodyOfSection();
        return bodyResult;
    }


    /** Funce nacte Hlavicku Sekce a ulozi do tridy Builder */
    private ParseResult readHeaderOfSection()
    {
        if (fileLines.size() == currentNumOfRow) {
            return new ParseResult(DONT_RESULT_OK, ANY_ERROR);
        }

        MatchResult whiteSpaceBefore = readWhiteSpace();
        builder.addSectionWhiteSpaceBeforeName(whiteSpaceBefore.value);

        MatchResult sectionName = readSectionName();
        if ( sectionName.result ) {
            builder.addSectionName(sectionName.value);
        } else {
            String resultString = "Name of section not found";
            return new ParseResult(DONT_RESULT_OK, resultString);
        }

        MatchResult whiteSpaceAfter = readWhiteSpace();
        builder.addSectionWhiteSpaceBeforeComment(whiteSpaceAfter.value);

        MatchResult comment = readComment();
        builder.addSectionComment(comment.value);

        return new ParseResult(RESULT_OK, ANY_ERROR);
    }


    /** Funce nacte Telo Sekce a ulozi do tridy Builder */
    private ParseResult readBodyOfSection()
    {
        ParseResult resultUntil = readUntilSectionOrElement();
        if (! resultUntil.result) {
            return resultUntil;
        }

        while (true) {

            if (fileLines.size() == currentNumOfRow) {
                return new ParseResult(RESULT_OK, ANY_ERROR);
            }

            // nasleduje-li dalsi sekce
            if (isSectionHeader()) {

                return new ParseResult(RESULT_OK, ANY_ERROR);
            }

            // nasleduje-li dalsi emement
            if ( isElementHeader()) {

                ParseResult elementHeaderResult = readElement();
                if (!elementHeaderResult.result) {
                    System.out.println("SYSTEMOVA CHYBA v regexpech"
                            + " tryToReadBodyOfElement");
                }
                continue;
            }

            String errorString = "Unexpected chars";
            return new ParseResult(DONT_RESULT_OK, errorString);
        }

    }


    /** Funce nacte Element a ulozi do tridy Builder */
    private ParseResult readElement()
    {
        ParseResult headerResult = readHeaderOfElement();
        if (! headerResult.result){
            return headerResult;
        }
        ParseResult bodyResult = readBodyOfElement();
        return bodyResult;
    }


    /** Funce nacte Hlavicku Elementu a ulozi do tridy Builder */
    private ParseResult readHeaderOfElement()
    {
        if (fileLines.size() == currentNumOfRow) {
            return new ParseResult(DONT_RESULT_OK, ANY_ERROR);
        }

        MatchResult whiteSpaceBeforeIdentifier = readWhiteSpace();
        builder.addElementWhiteSpaceBeforeIdentifier(
                whiteSpaceBeforeIdentifier.value);

        MatchResult identifier = readIdentifier();
        if (identifier.result) {
            builder.addElementName(identifier.value);
        } else {

            String error = "Identifier not found";
            return new ParseResult(identifier, error);
        }

        MatchResult whiteSpaceAfterIdentifier = readWhiteSpace();
        builder.addElementWhiteSpaceAfterIdentifier(
                whiteSpaceAfterIdentifier.value);

        MatchResult equal = readEqual();
        if (! equal.result) {

            String error = "Equal char not found";
            return new ParseResult(identifier, error);
        }

        MatchResult whiteSpaceBeforeValue = readWhiteSpace();
        builder.addElementWhiteSpaceBeforeValue(whiteSpaceBeforeValue.value);

        MatchResult identifierValue = readIdentifierValue();
        if ( identifierValue.result) {
            builder.addElementValue(identifierValue.value);

        } else {

            String error = "Value not found";
            return new ParseResult(identifier, error);
        }

        MatchResult whiteSpaceAfterValue = readWhiteSpace();
        builder.addElementWhiteSpaceAfterValue(whiteSpaceAfterValue.value);

        MatchResult comment = readComment();
        builder.addElementComment(comment.value);

        return new ParseResult(RESULT_OK, ANY_ERROR);
    }


    /** Funce nacte Telo Elementu a ulozi do tridy Builder */
    private ParseResult readBodyOfElement()
    {
        return readUntilSectionOrElement();
    }


    /** Funce nacita prazdne radky a komentare dokud nenarazi na Hlavicku sekce
     *  nebo na hlavicku Elementu a uklada do tridy Builder
     */
    private ParseResult readUntilSectionOrElement()
    {
        while (true) {

            if (fileLines.size() == currentNumOfRow) {
                return new ParseResult(RESULT_OK, ANY_ERROR);
            }

            // dokud se nepodari precist dalsi element
            if (this.isElementHeader()) {
                return new ParseResult(RESULT_OK, ANY_ERROR);
            }

            // dokud se nepodari precist dalsi sekci
            if (this.isSectionHeader()) {
                return new ParseResult(RESULT_OK, ANY_ERROR);
            }

            if (this.isWhiteLine()) {

                ParseResult resultWhiteLine = readWhiteLine();
                if (! resultWhiteLine.result) {
                    System.out.println("SYSTEMOVA CHYBA v regexpech "
                            + "readUntilElementOrSectionOrEoF");
                }
                continue;
            }


            String errorString = "Unexpected chars";
            return new ParseResult(DONT_RESULT_OK, errorString);
        }

    }

    /** Funce nacte radku bilych znaku a ulozi do tridy Builder,
     *  radka muze obsahovat i komentar
     */
    private ParseResult readWhiteLine()
    {
        MatchResult whiteSpace = readWhiteSpace();
        MatchResult comment = readComment();

        if (( whiteSpace.result) && ( comment.result)) {
            builder.addWhiteSpaceLineComment(whiteSpace.value, comment.value);
        }

        if (( whiteSpace.result) && (! comment.result)) {
            builder.addWhiteSpaceLineComment(whiteSpace.value, "");
        }

        boolean result = whiteSpace.result;
        return new ParseResult(result, ANY_ERROR);
    }


    /** Funkce rozhodne jestli je naseldujici radka Hlavicka Sekce */
    private boolean isSectionHeader() {

        Pattern pattern = Pattern.compile(RegexpExpression.sectionHeaderLine);
        Matcher matcher = pattern.matcher(fileLines.get(currentNumOfRow));

        boolean expressionFound = matcher.find(0);

        if (DEBUG) {
            System.out.println("IS SECTION_HEADER:");
            System.out.println("" + expressionFound);
        }

        return expressionFound;
    }

    /** Funkce rozhodne jestli je naseldujici radka Hlavicka Elementu */
    private boolean isElementHeader() {

        Pattern pattern = Pattern.compile(RegexpExpression.elementHeaderLine);
        Matcher matcher = pattern.matcher(fileLines.get(currentNumOfRow));

        boolean expressionFound = matcher.find(0);

        if (DEBUG) {
            System.out.println("IS ELEMENT_HEADER:");
            System.out.println("" + expressionFound);
        }

        return expressionFound;
    }

    /** Funkce rozhodne jestli je naseldujici radka tvorena pouze bilymi znaky
     */
    private boolean isWhiteLine() {

        Pattern pattern
                = Pattern.compile(RegexpExpression.whiteSpaceCommentLine);
        Matcher matcher = pattern.matcher(fileLines.get(currentNumOfRow));

        boolean expressionFound = matcher.find();

        if (DEBUG) {
            System.out.println("IS WHITE_LINE:");
            System.out.println("" + expressionFound);
        }

        return expressionFound;
    }

    /** Funkce nacita hlavicku Sekce */
    private MatchResult readSectionName() {

        MatchResult matchResult
                = tryToReadRegexp(RegexpExpression.sectionRegexp);

        if (DEBUG) {
            System.out.println("SECTION:");
            System.out.println("--" + matchResult.value + "--");
        }

        return matchResult;
    }

    /** Funkce nacita identifikator */
    private MatchResult readIdentifier() {

        MatchResult matchResult
                = tryToReadRegexp(RegexpExpression.identifierRegexp);

        if (DEBUG) {
            System.out.println("IDENTIFIER:");
            System.out.println("--" + matchResult.value + "--");
        }

        return matchResult;
    }

    /** Funkce nacita rovna se */
    private MatchResult readEqual() {

        MatchResult matchResult = tryToReadRegexp(RegexpExpression.equalRegexp);

        if (DEBUG) {
            System.out.println("EQUAL:");
            System.out.println("--" + matchResult.value + "--");
        }

        return matchResult;
    }

    /** Funkce nacita hodnotu elementu */
    private MatchResult readIdentifierValue() {

        MatchResult matchResult = tryToReadRegexp(RegexpExpression.valueRegexp);

        if (DEBUG) {
            System.out.println("VALUE:");
            System.out.println("--" + matchResult.value + "--");
        }

        return matchResult;
    }

    /** Funkce nacita komentar */
    private MatchResult readComment() {

        MatchResult matchResult
                = tryToReadRegexp(RegexpExpression.commentRegexp);

        if (DEBUG) {
            System.out.println("COMMENT:");
            System.out.println("--" + matchResult.value + "--");
        }

        currentNumOfRow++;
        currentNumOfColumn = 0;

        return matchResult;
    }

    /** Funkce nacita radku bilych znaku */
    private MatchResult readWhiteSpace() {

        String line = fileLines.get(currentNumOfRow);
        int indexStart =  currentNumOfColumn;
        int indexEnd =  currentNumOfColumn;


        while (true) {

            if ((indexEnd) == line.length()) {
                break;
            }

            if (line.substring(indexEnd, indexEnd+1).equals(" ")) {
                indexEnd++;
            } else {
                break;
            }

        }

        currentNumOfColumn += indexEnd - indexStart;

        MatchResult match = new MatchResult();
        match.result = true;
        match.value = line.substring(indexStart, indexEnd);
        match.numOfRow = this.currentNumOfRow;
        match.numOfColumnStart = indexStart;
        match.numOfColumnEnd = indexEnd;

        if (DEBUG) {
            System.out.println("WHITE_SPACE:");
            System.out.println("--" + match.value + "--");
        }

        return match;
    }

    /** Funkce se pokusi nacist regexp z aktualni radku a aktualniho sloupce */
    private MatchResult tryToReadRegexp(String regexp) {

        if (DEBUG) {
            String currentLine = fileLines.get(currentNumOfRow);
            System.out.println("AKTUALNI RADKA: " + currentLine);
        }

        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(fileLines.get(currentNumOfRow));
        boolean expressionFound = matcher.find(currentNumOfColumn);

        boolean result = false;
        String expression = "";
        int numOfRow = currentNumOfRow;
        int numOfColStart = -1;
        int numOfColEnd = -1;

        if (expressionFound) {
            expression = matcher.group();
            numOfColStart = matcher.start();
            numOfColEnd = matcher.end();
            result = expressionFound && matcher.start() == currentNumOfColumn;
        }

        currentNumOfColumn = numOfColEnd;

        MatchResult matchResult = new MatchResult();
        matchResult.result = result;
        matchResult.value = expression;
        matchResult.numOfRow = numOfRow;
        matchResult.numOfColumnStart = numOfColStart;
        matchResult.numOfColumnEnd = numOfColEnd;

        return matchResult;
    }

    /** Trida reprzentujici vysledek Parsovani */
    protected class MatchResult {

        public boolean result;
        public String value = "";

        public int numOfRow;
        public int numOfColumnStart;
        public int numOfColumnEnd;
    }

}


/** Trida reprzentujici vysledek Parsovani */
class ParseResult {

        public boolean result;
        public String error;

        public int numOfRow;
        public int numOfColumnStart;
        public int numOfColumnEnd;

        public ParseResult(boolean result, String error) {
            this.result = result;
            this.error = error;

            this.numOfRow = 0;
            this.numOfColumnStart = 0;
            this.numOfColumnEnd = 0;
        }

        protected ParseResult(MatchResult match, String error) {
            result = match.result;
            numOfRow = match.numOfRow;
            numOfColumnStart = match.numOfColumnStart;
            numOfColumnEnd = match.numOfColumnEnd;
        }
    }
