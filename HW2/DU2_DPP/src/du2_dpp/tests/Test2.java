/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.tests;

import du2_dpp.configuration.ElementTypes;
import du2_dpp.configuration.Format;
import du2_dpp.configuration.FormatException;
import du2_dpp.instance.Instance;
import du2_dpp.instance.InstanceException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stepan
 */
public class Test2 {

    public static void test() {

        File file = new File("tests/input2");

        Instance instance = new Instance(file);

        try {
            instance.ReadInstance();
            instance.myPrint();

        } catch (InstanceException ex) {
            System.out.println("CHYBA InstanceException");
        } catch (FileNotFoundException ex) {
            System.out.println("CHYBA FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("CHYBA IOException");
        }


        Format format = null;
        try {
        format = new Format();
        format.setDelimiter(",");
        format.addSection("Sekce 1", Format.IS_NOT_REQUIRED);
        format.addElement("Option 1", "Sekce 1", ElementTypes.STRING,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("oPtion 1", "Sekce 1", ElementTypes.STRING,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);

        List<String> hodnoty = new ArrayList();
        hodnoty.add("tyhle");
        hodnoty.add("tamty");
        ElementTypes enu = ElementTypes.ENUM;
        enu.setCoorectValues(hodnoty);

        format.addElement("frantisek 1", "Sekce 1", enu,
                Format.IS_ARRAY, "tyhle", Format.IS_NOT_REQUIRED);
        format.addSection("$Sekce::podsekce", Format.IS_NOT_REQUIRED);
        format.addElement("Option 2", "$Sekce::podsekce",
                ElementTypes.STRING, Format.IS_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("Option 3", "$Sekce::podsekce",
                ElementTypes.STRING, Format.IS_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("Option 4", "$Sekce::podsekce",
                ElementTypes.STRING, Format.IS_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("Option 5", "$Sekce::podsekce",
                ElementTypes.STRING, Format.IS_ARRAY, "1", Format.IS_REQUIRED);
        format.addSection("Cisla", Format.IS_NOT_REQUIRED);
        format.addElement("cele", "Cisla", ElementTypes.SIGNED,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("cele_bin", "Cisla", ElementTypes.UNSIGNED,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("cele_hex", "Cisla", ElementTypes.UNSIGNED,
                Format.IS_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("cele_oct", "Cisla", ElementTypes.UNSIGNED,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("float1", "Cisla", ElementTypes.FLOAT,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("float2", "Cisla", ElementTypes.FLOAT,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("float3", "Cisla", ElementTypes.FLOAT,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("float4", "Cisla", ElementTypes.FLOAT,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addSection("Other", Format.IS_NOT_REQUIRED);
        format.addElement("bool1", "Other", ElementTypes.BOOLEAN,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("bool2", "Other", ElementTypes.BOOLEAN,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);
        format.addElement("bool3", "Other", ElementTypes.BOOLEAN,
                Format.IS_NOT_ARRAY, "1", Format.IS_REQUIRED);

        File fileOut = new File("tests/output2");
        try {
             format.writeDefaultConfiguration(fileOut);
        } catch (IOException ex) {
                Logger.getLogger(
                        Test2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        } catch (FormatException ex) {
            System.out.println(ex.getMessage());
        }

        try {
           format.matchToFormat(instance);
        } catch (FormatException e) {
            System.out.println(e.getMessage());
            System.out.println("FormatException");
           return;
        }

        System.out.println("Instance odpovida Formatu");

    }
}
