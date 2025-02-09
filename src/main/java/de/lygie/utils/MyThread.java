package de.lygie.utils;

import de.lygie.model.Sendung;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MyThread extends Thread{

    File[] listOfFiles;
    private static int chunksize = 100;
    private static String url = "jdbc:mysql://localhost:3306/dsmetest";

    public MyThread(File[] listOfFiles, int chunksize, String url){
        this.listOfFiles = listOfFiles;
        this.chunksize = chunksize;
        this.url = url;
    }

    @Override
    public void run()
    {
        for (File file : listOfFiles) {
            if (null != file && file.isFile()) {
                Sendung sendung = new Sendung();
                try {
                    sendung.importFile(file,chunksize,url);
                } catch (ParserConfigurationException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SAXException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
