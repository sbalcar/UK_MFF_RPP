/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package du2_dpp.instance;

import du2_dpp.instance.reader.Reader;
import du2_dpp.instance.reader.ReadingResult;
import du2_dpp.instance.section.Section;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Trida reprezentujici Instanci konfuguracniho souboru
 * @author stepan
 */
public class Instance {

    private File file = null;

    protected List<Section> sections;
    protected List<String> commentLinesBeforeFirstSection;

    private boolean DEBUG = false;

    public Instance() {

        this.sections = new ArrayList<Section>();
        this.commentLinesBeforeFirstSection = new ArrayList<String>();
    }

    public Instance(File file) {

        this.sections = new ArrayList<Section>();
        this.file = file;
    }

    public void addCommentLinesBeforeFirstSection(String string) {

        commentLinesBeforeFirstSection.add(string);
    }
    /** Funce prida Sekci */
    public void addSection(Section section) throws InstanceException {

        this.addingTest(section);
        this.sections.add(section);
    }

    /** Funce vlozi Sekci na zadany index */
    public void addSection(int index, Section section) throws InstanceException {

        this.addingTest(section);
        this.sections.add(index, section);
    }

    /** Funce otestuje sekci pred vlozenim */
    private void addingTest(Section section) throws InstanceException {

        if (section == null) {
            String message = "Section is null";
            throw new InstanceException(message,-1);
        }

        for ( Section sectionI : sections) {
            if (sectionI.getName().equals(section.getName())){
                String message = "Section with name " + section.getName() +
                        "is contain yet";
                throw new InstanceException(message,-1);
            }
        }
    }

    /** Funce vrati vsechny Sekce */
    public List<Section> getSections() {

        return this.sections;
    }

    /** Funce vrati Sekci podle zadaneho jmena */
    public Section getSection(String name) {

        for ( Section sectionI : sections) {
            if (sectionI.getName().equals(name)){
                return sectionI;
            }
        }

        return null;
    }

    /** Funce vrati posledni zadanou Sekci */
    public Section getLastSection() {

        if (sections.isEmpty()) {
            return null;
        }

        return sections.get(sections.size() -1);
    }


    /** Funce vrati pocet Sekci v cele instanci */
    public int getNumOfSections() {

        return sections.size();
    }

    /** Funce vrati pocet Elementu v cele instanci */
    public int getNumOfElements() {

        int counter = 0;
        for ( Section sectionI : sections) {

            counter += sectionI.getNumOfElements();
        }

        return counter;
    }

    /** Funce nacte Instanci ze zadaneho souboru */
    public void ReadInstance() throws FileNotFoundException, IOException,
            InstanceException {

        Reader reader = new Reader(this.file);

        ReadingResult result = reader.read();
        if (result.resultOK) {
            List<Section> newSections = result.instance.sections;
            List<String> newCommentLinesBeforeFirstSection
                    = result.instance.commentLinesBeforeFirstSection;
            this.sections = newSections;
            this.commentLinesBeforeFirstSection
                    = newCommentLinesBeforeFirstSection;
        } else {
            InstanceException exception = new InstanceException();
            throw exception;
        }


        List<String> linesReaded = reader.readLines();
        List<String> linesCreated = toStringLines();

        if (DEBUG) {
            System.out.println("-------------------------------------------");
        }
        if (linesReaded.size() != linesCreated.size()) {
            System.out.println("Systemova chyba - ruzny pocet radek");
        }
        for (int lineIndex = 0; lineIndex < linesReaded.size(); lineIndex++) {
            String lineReaded = linesReaded.get(lineIndex);
            String lineCreated = linesCreated.get(lineIndex);
            if (DEBUG) {
                System.out.println(lineReaded);
                System.out.println(lineCreated);
            }
            if (! lineReaded.equals(lineCreated)) {
                String message = "Input line num " + lineIndex + "can not be" +
                        "good parsed";
                throw new InstanceException(message, lineIndex);
            }
        }

        if (DEBUG) {
            System.out.println("-------------------------------------------");
        }
    }

    /** Funce vrati radky cele Instance */
    public List<String> toStringLines() {

        List<String> resultLines = new ArrayList<String>();
        resultLines.addAll(commentLinesBeforeFirstSection);
        
        for (Section sectionI : sections) {
            List<String> sectionLines = sectionI.toStringLines();
            resultLines.addAll(sectionLines);
        }

        return resultLines;
    }


    /** Funce vytiskne celou Instanci */
    public void myPrint() {

        System.out.println("/////////////////////////////////////////////");

        List<String> lines = toStringLines();
        for (String lineI : lines) {
            System.out.println(lineI);
        }

        System.out.println("/////////////////////////////////////////////");
    }

}
