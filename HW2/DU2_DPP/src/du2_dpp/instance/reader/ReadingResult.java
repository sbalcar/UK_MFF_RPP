/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.instance.reader;

import du2_dpp.instance.Instance;

/**
 *
 * @author stepan
 */

 /**
  * Trida ktera vraci informace o nacteni instance.
  * @author stepan
  */
public class ReadingResult {

    /** Vysledek nacteni */
    public boolean resultOK;
    public Instance instance;

    /** Chyba cteni */
    public String error;

    /** Pokod nacteni neprobehlo uspesne, informace o radcich a sloupcich,
     *  kde nastala chyba. V opacnem pripade jsou hodnoty nastavene na -1.
     */
    public int numOfRow;
    public int numOfColumnStart;
    public int numOfColumnEnd;

    public ReadingResult()
    {
        numOfRow = -1;
        numOfColumnStart = -1;
        numOfColumnEnd = -1;
    }
    
    public ReadingResult(ParseResult result, Instance instance) {

        this.instance = instance;

        this.resultOK = result.result;
        this.error = result.error;

        this.numOfRow = result.numOfRow;
        this.numOfColumnStart = result.numOfColumnStart;
        this.numOfColumnEnd = result.numOfColumnEnd;
    }
}
