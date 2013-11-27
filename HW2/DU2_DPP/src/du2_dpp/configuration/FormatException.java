/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.configuration;

/**
 *
 * @author stepan
 */
public class FormatException extends Exception {

    String message = "";

    public FormatException(){
        super();
    }
    
    public FormatException(String message){

        super(message);
    }

}
