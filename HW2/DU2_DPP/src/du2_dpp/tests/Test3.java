/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.tests;

import du2_dpp.instance.Instance;
import du2_dpp.instance.element.Element;
import du2_dpp.instance.section.Section;

/**
 *
 * @author stepan
 */
public class Test3 {

    public static void test() {

        Instance instance = new Instance();

        Section section1 = new Section("Section1");
        Element element1 = new Element("Element1");
        section1.addElement(element1);
        instance.addSection(section1);

        Section section2 = new Section("Section2");
        Element element2 = new Element("Element2");
        section2.addElement(element2);
        instance.addSection(section2);

    }
}
