package de.lygie.utils;

import de.lygie.model.Dsme;
import de.lygie.model.Sendung;

import javax.xml.parsers.ParserConfigurationException;

import static de.lygie.utils.RandomDsmeBuilder.generateDsme;

public class ThreadedBlockWriter implements Runnable{

    private String filename;
    private int size;

    ThreadedBlockWriter( String filename, int size) {
        this.filename = filename;
        this.size = size;
    }

    @Override
    public void run() {
        Sendung sendung = new Sendung();
        for (int i=0;i<size;i++){//eine millinen Datensaetze
            Dsme dsme = generateDsme();
            sendung.addMeldung(dsme);
            try {
                sendung.toXML(filename);
            } catch (ParserConfigurationException e) {
                //nop
            }
            sendung = new Sendung();
        }
    }
}
