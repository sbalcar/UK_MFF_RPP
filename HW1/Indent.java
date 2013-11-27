/**
 *
 * @author Stepan Balcar, Filip Repka
 */

/** Typy tokenu */
enum IndentLvlType {
    VIRTUAL_ROUND_BRACKET, WHITESPACE, COMMENT, RESERVED_WORD;
}


/** Rezervovana slova */
enum ReservedWord {
    UNTIL, END;
}


/** Objekt flagu. */
class TokenFlagModel {

    /** Vlajka tokenu "nic". */
    public boolean isNone;
    /** Vlajka tokenu "zacina radek". */
    public boolean isBeginsLine;
    /** Vlajka tokenu "ukoncuje radek". */
    public boolean isEndsLine;

    /** Vlajka tokenu "povoleny upravy casu rezervovanych slov". */
    public boolean isReservedWordsOn;
    /** Vlajka tokenu "povoleny upravy casu direktiv". */
    public boolean isDirectivesOn;
    /** Vlajka tokenu "povoleny upravy casu identifikatoru". */
    public boolean isIdentifiersOn;
    /** Vlajka tokenu "povoleno vkladani mezer na vhodna mista". */
    public boolean isInsertSpaceOn;
    /** Vlajka tokenu "povoleno odsazovani". */
    public boolean isIndentOn;
    /** Vlajka tokenu "povoleno zalamovani radku". */
    public boolean isWrapOn;
    /** Vlajka tokenu "povoleno vkladani prazdnych radku". */
    public boolean isBlankLinesOn;

    /**
     * Vytvori model se vsemi hodnami false.
     *
     */
    public TokenFlagModel() {

        this.isNone = false;
        this.isBeginsLine = false;
        this.isEndsLine = false;

        this.isReservedWordsOn = false;
        this.isDirectivesOn = false;
        this.isIdentifiersOn = false;
        this.isInsertSpaceOn = false;
        this.isIndentOn = false;
        this.isWrapOn = false;
        this.isBlankLinesOn = false;
    }

    /**
     * Vytvori model jako prunik dvou modelu.
     *
     * @param model1 prvni model
     * @param model2 druhy model
     */
    public static TokenFlagModel intersection(TokenFlagModel model1,
        TokenFlagModel model2) {

        TokenFlagModel tokenModel = new TokenFlagModel();

        tokenModel.isNone = model1.isNone && model2.isNone;
        tokenModel.isBeginsLine = model1.isBeginsLine &&
            model2.isBeginsLine;
        tokenModel.isEndsLine = model1.isEndsLine &&
            model2.isEndsLine;

        tokenModel.isReservedWordsOn = model1.isReservedWordsOn  &&
            model2.isReservedWordsOn;
        tokenModel.isDirectivesOn = model1.isDirectivesOn &&
            model2.isDirectivesOn;
        tokenModel.isIdentifiersOn = model1.isIdentifiersOn &&
            model2.isIdentifiersOn;
        tokenModel.isInsertSpaceOn = model1.isInsertSpaceOn &&
            model2.isInsertSpaceOn;
        tokenModel.isIndentOn = model1.isIndentOn &&
            model2.isIndentOn;
        tokenModel.isWrapOn = model1.isWrapOn &&
            model2.isWrapOn;
        tokenModel.isBlankLinesOn = model1.isBlankLinesOn &&
            model2.isBlankLinesOn;

        return tokenModel;
    }
}


/** Objekt tokenu. */
class Token implements Cloneable {

    /* Vlajky tokenu. */

    /** Vlajka tokenu "nic". */
    public boolean isNone;
    /** Vlajka tokenu "zacina radek". */
    public boolean isBeginsLine;
    /** Vlajka tokenu "ukoncuje radek". */
    public boolean isEndsLine;

    /** Vlajka tokenu "povoleny upravy casu rezervovanych slov". */
    public boolean isReservedWordsOn;
    /** Vlajka tokenu "povoleny upravy casu direktiv". */
    public boolean isDirectivesOn;
    /** Vlajka tokenu "povoleny upravy casu identifikatoru". */
    public boolean isIdentifiersOn;
    /** Vlajka tokenu "povoleno vkladani mezer na vhodna mista". */
    public boolean isInsertSpaceOn;
    /** Vlajka tokenu "povoleno odsazovani". */
    public boolean isIndentOn;
    /** Vlajka tokenu "povoleno zalamovani radku". */
    public boolean isWrapOn;
    /** Vlajka tokenu "povoleno vkladani prazdnych radku". */
    public boolean isBlankLinesOn;


    /** Radek, kde zacina (indexovano od 0). */
    public int row;
    /** Sloupec, kde zacina (indexovano od 0). */
    public int column;
    /** Text. */
    public String text;
    /** Typ. */
    public IndentLvlType type;

    /** Odkaz na predchozi token ve spojaku. */
    public Token previous;
    /** Odkaz na nasledujici token ve spojaku. */
    public Token next;

    /**
     * Vytvori token podle zadanych parametru.
     *
     * @param text text
     * @param klass trida
     * @param flagsModel vlajky
     * @param row radek, kde zacina (indexovano od 0)
     * @param column sloupec, kde zacina (indexovano od 0)
     * @param previous odkaz na predchozi token ve spojaku
     * @param next odkaz na nasledujici token ve spojaku
     */
    public Token(String text, IndentLvlType type, TokenFlagModel flagsModel,
        int row, int column, Token previous, Token next) {

        this.text = text;
        this.type = type;
        this.row = row;
        this.column = column;

        setTokenFlagModel(flagsModel);

        this.previous = previous;
        this.next = next;
    }

    /**
     * Odpovida token dane tride a textu?
     *
     * @param aClass trida
     * @param aText text
     * @return <code>true</code> pokud token odpovida dane tride a textu;
     *         <code>false</code> pokud token neodpovida dane tride a textu
     */
    boolean match(IndentLvlType type, String aText) {

        return this.type.equals(type) &&
            this.text.equalsIgnoreCase(aText);
    }


    /**
     * Vrati TokenFlagModel daneho Tokenu.
     *
     * @return TokenFlagModel
     */
    public TokenFlagModel getTokenFlagModel() {

        TokenFlagModel tokenModel = new TokenFlagModel();

        tokenModel.isNone = this.isNone;
        tokenModel.isBeginsLine = this.isBeginsLine;
        tokenModel.isEndsLine = this.isEndsLine;

        tokenModel.isReservedWordsOn = this.isReservedWordsOn;
        tokenModel.isDirectivesOn = this.isDirectivesOn;
        tokenModel.isIdentifiersOn = this.isIdentifiersOn;
        tokenModel.isInsertSpaceOn = this.isInsertSpaceOn;
        tokenModel.isIndentOn = this.isIndentOn;
        tokenModel.isWrapOn = this.isWrapOn;
        tokenModel.isBlankLinesOn = this.isBlankLinesOn;

        return tokenModel;
    }

    /**
     * Priradi TokenFlagModel danemu Tokenu.
     *
     * @param TokenFlagModel model na prirazeni
     */
    public void setTokenFlagModel(TokenFlagModel flagsModel) {

        this.isNone = flagsModel.isNone;
        this.isBeginsLine = flagsModel.isBeginsLine;
        this.isEndsLine = flagsModel.isEndsLine;
        this.isReservedWordsOn = flagsModel.isReservedWordsOn;
        this.isDirectivesOn = flagsModel.isDirectivesOn;
        this.isIdentifiersOn = flagsModel.isIdentifiersOn;
        this.isInsertSpaceOn = flagsModel.isInsertSpaceOn;
        this.isIndentOn = flagsModel.isIndentOn;
        this.isWrapOn = flagsModel.isWrapOn;
        this.isBlankLinesOn = flagsModel.isBlankLinesOn;
    }

    /**
     * Vytvori klon objektu.
     *
     * @return klon objektu
     */
    public Object clone() {

        Token token_clone = new Token(this.text, this.type,
            this.getTokenFlagModel(), this.row, this.column, null, null);

        return token_clone;
    }
}


/** Trida popisujici jednu uroven odsazeni. Slouzi jako jedna polozka
 *   v zasobniku odsazeni. */
class IndentLvl {

    /** Druh odsazeni. */
    public IndentLvlType type;
    /** Ukazatel na dalsi polozku zasobniku, */
    public IndentLvl next;

    IndentLvl(IndentLvlType type) {

        this.type = type;
    }
}


/** Zajistuje spravne odsazovani. */
public class Indent {

    /** Velikost jednoho odsazeni */
    public static final int INDENT_SIZE = 4;

    /**
     * Vrati <code>1</code> nebo <code>0</code> podle toho, jestli je dany
     * druh odsazeni jen realny nebo virtualni.
     *
     * Vracena hodnota je ve volajici funkci pouzita jako koeficint, coz ma
     * efektivne za nasledek, ze se virtualni druh odsazovani fyzicky
     * neodsazuje.
     *
     * @param klass druh odsazeni
     * @return <code>1</code> pokud je druh odsazeni realny;
     *         <code>0</code> pokud je druh odsazeni virtualni
     */
    private static int indentLevel(IndentLvlType type) {

        boolean isVirtualIndent = type.equals(
            IndentLvlType.VIRTUAL_ROUND_BRACKET);

        if (isVirtualIndent) {
            return 0;
        } else {
            return 1;
        }
    }

    /** Trida popisujici aktualni stav odsazovani v prubehu algoritmu. */
    private final static class IndentContext {

        /** Zasobnik <code>Indent</code>u. */
        public IndentLvl top;

        /** Aktualni uroven odsazeni. */
        public int currLevel;
        /** Minimalni uroven odsazeni na teto radce. */
        public int minLevel;

        /**
         * Odebere polozku ze zasobniku <code>Indent</code>u.
         *
         * @return odebrana polozka
         */
        private IndentLvl pop() {

            if (top != null) {
                IndentLvl top_old = top;

                top = top.next;
                currLevel -= indentLevel(top_old.type) * INDENT_SIZE;

                if (minLevel > currLevel) {
                    minLevel = currLevel;
                }

                return top_old;
            } else {
                return null;
            }
        }

        /**
         * Prida polozku na zasobnik <code>Indent</code>u.
         *
         * @param indent pridavana polozka
         */
        private void push(IndentLvl indent) {

            if (top != null) {
                indent.next = top;
            } else {
                indent.next = null;
            }

            top = indent;
            currLevel += indentLevel(indent.type) * INDENT_SIZE;
        }

        /**
         * Odsadi o jednu uroven, jejiz druh je urcen parametrem
         * <code>klass</code>.
         *
         * @param klass druh odsazeni
         */
        public void indent(IndentLvlType type) {

            IndentLvl odsazeni = new IndentLvl(type);

            push(odsazeni);
        }

        /** Odsadi zpatky. */
        public void unindent() {

            pop();
        }

        /** Odsadi zpatky, ale nesnizi pritom <code>minLevel</code>. */
        public void unindentNext() {

            int oldMinLevel = minLevel;

            pop();

            minLevel = oldMinLevel;
        }

        /**
         * Zjistuje, zda je druh odsazeni na vrcholu zasobniku
         * <code>type</code>.
         *
         * @param type - druh odsazeni, se kterym se porovnava druh odsazeni na
         *         vrcholu zasobniku
         * @return <code>true</code> pokud je zasobnik neprazdny a druh
         *          odsazeni na jeho vrcholu je <code>type</code>;
         *          <code>false</code> pokud je zasobnik prazdny nebo je druh
         *          odsazeni na jeho vrcholu jiny nez <code>type</code>
         */
        public boolean topClassIs(IndentLvlType type) {

            if (top == null) {
                return false;
            }

            if (top.type.equals(type)) {
                return true;
            } else {
                return false;
            }
        }

    };

    /**
     * Preskoci bile mezery a komentare, ktere nasleduji za <code>start</code>.
     *
     * @param start token, za kterym se preskakuji bile mezery a komentare,
     *         sam to nemusi byt bily komentar nebo mezera
     * @return nejblizsi token za <code>start</code>, ktery neni bila mezera
     *           ani komentar; <code>null</code> pokud takovy uz ve spojaku
     *           neni
     */
    private static Token skipWhitespaceAndComments(Token start) {

        Token tokenI = start.next;

        while (tokenI != null) {

            if ((tokenI.type.equals(IndentLvlType.WHITESPACE)) ||
                (tokenI.type.equals(IndentLvlType.COMMENT)))    {
                tokenI = tokenI.next;
            } else {
                break;
            }
        }

        return tokenI;
    }

    /**
     * Preskoci vsechny tokeny od <code>token</code> (vcetne) a nemaji tridu
     * <code>type</code> a text <code>text</code>. Porovnavani tridy tokenu
     * je zde pro efektivitu, aby se nemusely porad porovnavat retezce.
     * Misto toho se nejdrive porovnaji tridy a az kdyz jsou shodne,
     * porovnavaji se retezce.
     *
     * @param token token, za kterym se preskakuji neodpovidajici tokeny
     * @param type trida hledaneho tokenu
     * @param text text hledaneho tokenu
     * @return nejblizsi token za <code>token</code> (vcetne) se tridou
     *          <code>type</code> a textem <code>text</code>;
     *          <code>null</code> pokud takovy uz ve spojaku neni
     */
    private static Token skipUntil(Token token, String type, String text) {

        /** Going through the connection list. */
        Token tokenI = token;

        while (tokenI != null) {

            /** Comparation of actual item with input arguments. */
            if (tokenI.type.equals(type) &&
                tokenI.text.equalsIgnoreCase(text)) {
                break;
            }

            tokenI = tokenI.next;
        }

        return tokenI;
    }

    /**
     * Zmeni hodnotu polozky <code>column</code> o <code>delta</code> u vsech
     * tokenu od <code>start</code> az do konce radku.
     *
     * @param start prni token, kde se ma menit hodnota <code>column</code>
     * @param delta o kolik se ma zmenit hodnota <code>column</code>
     */
    static void changeColUntilEOL(Token start, int delta) {

        /** Prochazi cely spojovy seznam az na konec radku. */
        Token tokenI = start;

        while (tokenI != null) {

            /** Tokens from first line will be changed */
            if (tokenI.row == start.row) {
                tokenI.column += delta;
            } else {
                break;
            }

            tokenI = tokenI.next;
        }

    }

    /**
     * Zmeni hodnotu polozky <code>row</code> o <code>delta</code> u vsech
     * tokenu od <code>start</code> az do konce spojaku tokenu.
     *
     * @param start prni token, kde se ma menit hodnota <code>row</code>
     * @param delta o kolik se ma zmenit hodnota <code>row</code>
     */
    static void changeRowUntilEOF(int delta, Token start) {

        Token tokenRow = start;

        while (tokenRow != null) {
            tokenRow.row += delta;

            tokenRow = tokenRow.next;
        }
    }

    /**
     * Zaridi, aby za danym tokenem byl prazdny radek.
     *
     * @param token token, za kterym ma byt prazdna radka
     */
    private static void ensureBlankLineAfter(Token token) {

        if (token == null || token.next == null) {
            return;
        }

        while (token != null) {
            if (token.next == null) {
                return;
            }

            if (token.isEndsLine) {
                break;
            }

            token = token.next;
        }

        if (token != null && token.next != null) {
            TokenFlagModel tokenFM = token.getTokenFlagModel();
            TokenFlagModel tokenNextFM = token.next.getTokenFlagModel();

            TokenFlagModel intersected_model
                = TokenFlagModel.intersection(tokenFM, tokenNextFM);

            token.setTokenFlagModel(intersected_model);
        }


        if (token.next.isEndsLine) {
            return;
        }

        TokenFlagModel model_new = token.next.getTokenFlagModel();

        model_new.isBeginsLine = true;
        model_new.isEndsLine = true;

        String newLine = "\n";
        Token newToken = new Token(newLine, IndentLvlType.WHITESPACE,
            model_new, token.next.row, 0, token, token.next);

        int one = 1;
        changeRowUntilEOF(one, token.next);

        token.next.previous = newToken;
        token.next = newToken;

    }

    /**
     * Odsadi radek zacinajici tokenem <code>start</code> na uroven
     *  <code>level</code>.
     *
     * @param tokenStart prvni token na radce
     * @param tokenLength uroven, na kterou ma byt tato radka odsazena
     * @return token, kterym radka zacina po odsazeni (neni nutne stejny jako
     *          <code>start</code>)
     */
    private static Token indentLine(Token tokenStart, int tokenLength) {

        /** Jedna se o bile znaky */
        if (tokenStart.type.equals(IndentLvlType.WHITESPACE)) {
            if (tokenStart.isEndsLine) {
                return tokenStart;
            }

            if (tokenLength > 0) {
                return indentLineExtension(tokenStart, tokenLength);
            }

            if (tokenLength < 0) {
                return indentLineShortening(tokenStart);
            }
        } else {
            if (tokenLength > 0) {
                return indentLineCreating(tokenStart, tokenLength);
            }
        }

        return tokenStart;
    }

    /**
     * Vrati prazdny retezec delky length.
     *
     * @param length delka pozadovaneho prazdneho retezce
     * @return vrati prazdny retezec
     */
    private static String createEmptyString(int length) {

        String string = "";

        for (int actualLength = 1; actualLength <= length; actualLength++) {
            string += " ";
        }

        return string;
    }

    /**
     * Odsadi radek za tokenem start smerem vpravo
     *
     * @param start prvni token na radce
     * @param length uroven, na kterou ma byt tato radka odsazena
     * @return vrati odsazeny token
     */
    private static Token indentLineExtension(Token start, int length) {

        int delta = length - start.text.length();
        String string_empty = createEmptyString(delta);

        start.text += string_empty;

        changeColUntilEOL(start.next, delta);

        return start;
    }

    /**
     * Odsadi radek za tokenem start smerem vlevo
     *
     * @param start prvni token na radce
     * @return vrati odsazeny token
     */
    private static Token indentLineShortening(Token start) {

        /*delta = -start.column;*/
        int delta = -(int)start.text.length();

        if (start.previous != null) {
            start.previous.next = start.next;
        }

        if (start.next != null) {
            start.next.previous = start.previous;
        }

        start.next.isBeginsLine = true;

        changeColUntilEOL(start.next, delta);

        return start.next;
    }

    /**
     * Odsadi radek za tokenem start vytvorenim noveho tokenu
     *
     * @param start prvni token na radce
     * @param length uroven, na kterou ma byt tato radka odsazena
     * @return vrati odsazeny token
     */
    private static Token indentLineCreating(Token start, int length) {

        String emptyString = createEmptyString(length);

        TokenFlagModel modelLine = start.getTokenFlagModel();

        modelLine.isBeginsLine = true;

        Token newToken = new Token(emptyString, IndentLvlType.WHITESPACE,
            modelLine, start.row, start.column, start.previous, start);

        changeColUntilEOL(start, length);

        if (start.previous != null) {
            start.previous.next = newToken;
        }

        start.previous = newToken;
        start.isBeginsLine = false;

        return start;
    }

    /**
     * Odsadi urcite typy komentaru podle toho, k cemu patri.
     *
     * @param tokens spojovy seznam tokenu, kde se maji najit a odsadit
     * komentare
     */
    static void indentComments(Token tokensToIndent) {

        Token token = tokensToIndent;

        while (token != null) {
            if (token.type.equals(IndentLvlType.COMMENT)) {
                indentComment(token);
            }

            token = token.next;
        }
    }

    /**
     * Urci jestli token stoji osamocene.
     * Osamoceny je pokud pred nim nic nestoji.
     *
     * @param token spojovy seznam tokenu
     * @return boolean je osamocen?
     */
    static boolean isCommentStandAlone(Token token) {

        if (token.isBeginsLine) {
            return true;
        }

        Token tokenI = token.previous;

        while (tokenI != null) {
            if (!tokenI.type.equals(IndentLvlType.WHITESPACE)) {
                return false;
            }

            if (tokenI.isBeginsLine) {
                return true;
            }

            tokenI = tokenI.previous;
        }

        return true;
    }

    /**
     * Vrati prvni token na aktualni radce.
     *
     * @param tokens spojovy seznam tokenu
     * @return token
     */
    static Token firstWhiteTokenAtThisLine(Token token) {

        Token tokenRow = token;

        while (tokenRow != null) {
            if (tokenRow.type.equals(IndentLvlType.WHITESPACE) &&
                tokenRow.isBeginsLine) {
                return tokenRow;
            }

            tokenRow = tokenRow.previous;
        }

        return null;
    }

    /**
     * Odsadi urcity typy komentare podle toho, k cemu patri.
     *
     * @param tokens spojovy seznam tokenu, kde se maji najit a odsadit
     * komentare
     */
    static void indentComment(Token token) {

        /* Nejdrive zjistime, zda je komentar standalone */
        if (! isCommentStandAlone(token)) {
            return;
        }

        Token tokenConneceted = token.next;

        /* Ted najdeme neco, k cemu by se mohl tento komentar vztahovat. */
        while (tokenConneceted != null) {
            if (tokenConneceted.type.equals(IndentLvlType.COMMENT) ||
                tokenConneceted.type.equals(IndentLvlType.WHITESPACE)) {

                tokenConneceted = tokenConneceted.next;
            } else {
                break;
            }
        }

        boolean isTokenNull = (tokenConneceted == null);

        boolean isTokenEnd = tokenConneceted.match(
           IndentLvlType.RESERVED_WORD,ReservedWord.END.toString());

        boolean isTokenUntil = tokenConneceted.match(
           IndentLvlType.RESERVED_WORD,ReservedWord.UNTIL.toString());

        if (isTokenNull || isTokenEnd || isTokenUntil) {
            return;
        }

        /** Prvni bily token na radce */
        Token firstToken = firstWhiteTokenAtThisLine(token);

        /** Posunuti prvniho tokenu na radce na pozici tokenu
         * tokenConneceted. */
        indentLine(firstToken, tokenConneceted.column);
    }
}