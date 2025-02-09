package de.lygie;

import de.lygie.model.Dsme;
import de.lygie.model.Sendung;
import de.lygie.model.Versicherungsnummer;
import de.lygie.utils.Config;
import de.lygie.utils.MyThread;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static de.lygie.utils.RandomDsmeBuilder.generateDsme;
import static java.lang.Math.ceil;

/**
 * Hello world!
 *
 */
public class App 
{

    static Config config;

    public static void main( String[] args ) throws ParserConfigurationException, IOException, SAXException, SQLException {

        config = new Config();

        String folder = new String("/tmp/xml/");
        System.out.println("Aufruf war " + String.join(" ", args) + " mit " + args.length + " Parameter");

        if (args.length > 0 && args[0].equals("import")){
            System.out.println("lese");
            readXML(folder);
        }
        else{
            writeXML(folder);
        }

    }


    private static void readXML(String foldername) throws ParserConfigurationException, IOException, SAXException, SQLException {
        File folder = new File(foldername);
        File[] listOfFiles = folder.listFiles();
        int groesse = (int) (listOfFiles.length / config.getThreads()) + 1;
        File[][] listOfFileForThread = new File[config.getThreads()][groesse];
        List<MyThread> threadsToStart = new ArrayList<>();
        int[] feldInTopf = new int[config.getThreads()];
        int j = 0;
        int curlength;
        int topf;
        for (File file : listOfFiles) {
            topf = j % config.getThreads();
            listOfFileForThread[topf][feldInTopf[topf]]=file;
            feldInTopf[topf]++;
            j++;
        }

        long t1 = System.currentTimeMillis();
        int alle = listOfFiles.length*10000;


        for (File[] workload : listOfFileForThread){
                threadsToStart.add(new MyThread(workload, config.getChunksize()));
        }
        for (MyThread thread : threadsToStart){
            thread.start();

        }
        for (MyThread thread : threadsToStart){

            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println(alle + " Datensätze in Datenbank in " + Math.round((System.currentTimeMillis() - t1)/1000) + " Sekunden");


    }

    private static void writeXML(String folder) throws ParserConfigurationException {

        File f = new File(folder);
        f.mkdir();
        int j = 1;
        int alle = 0;

        long t1 = System.currentTimeMillis();


        Sendung sendung = new Sendung();
        for (int i=0;i<=config.getTestQuantity();i++){//1 millionen Datensaetze
            alle ++;
            Dsme dsme = generateDsme();
            sendung.addMeldung(dsme);
            if(i!= 0 && i%10000==0){
                sendung.toXML(folder + "sendung_" + j++ +".xml");
                sendung = new Sendung();
            }
        }
        System.out.println(alle-1 + " Datensätze geschrieben in " + Math.round((System.currentTimeMillis() - t1)/1000) + " Sekunden");

    }
}
