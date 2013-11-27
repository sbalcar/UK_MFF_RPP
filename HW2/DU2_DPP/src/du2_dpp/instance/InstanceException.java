/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.instance;

/**
 *
 * @author stepan
 */
public class InstanceException extends Exception {

    String message = "";
    int line = -1;

    public InstanceException(){
        super();
    }

    public InstanceException(String message, int line){

        super(message);
        this.line = line;
    }

}