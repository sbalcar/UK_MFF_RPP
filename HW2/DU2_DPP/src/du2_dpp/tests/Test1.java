/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.tests;

import du2_dpp.configuration.Format;
import du2_dpp.instance.Instance;
import du2_dpp.instance.InstanceException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author stepan
 */
public class Test1 {

    public static void test() {

        File file = new File("tests/input1");

        Instance instance = new Instance(file);

        try {
            instance.ReadInstance();

        } catch (InstanceException ex) {
            System.out.println("CHYBA InstanceException");
        } catch (FileNotFoundException ex) {
            System.out.println("CHYBA FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("CHYBA IOException");
        }

        instance.myPrint();
        
    }
}
