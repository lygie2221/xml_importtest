package de.lygie;

import de.lygie.model.Dsme;
import de.lygie.model.Sendung;
import de.lygie.model.Versicherungsnummer;
import de.lygie.utils.MyThread;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
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

    private static int chunksize = 100;
    private static int threads = 4;
    private static String url = "jdbc:mysql://localhost:3306/dsmetest";

    public static void main( String[] args ) throws ParserConfigurationException, IOException, SAXException, SQLException {

        String folder = new String("/tmp/xml/");
        System.out.println("Aufruf war " + String.join(" ", args) + args.length + " Parameter");

        if (args.length > 1 && Integer.parseInt(args[1])>0){
            chunksize=Integer.parseInt(args[1]);
            System.out.println("Chunksize ist " + chunksize);
        }

        if (args.length > 2){
            url=args[2];
            System.out.println("Url ist " + url);
        }

        if (args.length > 3){
            threads=Integer.parseInt(args[3]);
            System.out.println("threads ist " + threads);
        }

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
        int groesse = (int) (listOfFiles.length / threads) + 1;
        File[][] listOfFileForThread = new File[threads][groesse];
        List<MyThread> threadsToStart = new ArrayList<>();
        int[] feldInTopf = new int[threads];
        int j = 0;
        int curlength;
        int topf;
        for (File file : listOfFiles) {
            topf = j % threads;
            listOfFileForThread[topf][feldInTopf[topf]]=file;
            feldInTopf[topf]++;
            j++;
        }

        long t1 = System.currentTimeMillis();
        int alle = listOfFiles.length*10000;


        for (File[] workload : listOfFileForThread){
                threadsToStart.add(new MyThread(workload, chunksize, url));
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
        for (int i=0;i<=10000*100;i++){//1 millionen Datensaetze
            alle ++;
            Dsme dsme = generateDsme();
            sendung.addMeldung(dsme);
            if(i!= 0 && i%10000==0){
                sendung.toXML(folder + "sendung_" + j++ +".xml");
                sendung = new Sendung();
            }
        }
        System.out.println(alle + " Datensätze geschrieben in " + Math.round((System.currentTimeMillis() - t1)/1000) + " Sekunden");

    }
}
