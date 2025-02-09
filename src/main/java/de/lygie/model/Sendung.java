package de.lygie.model;

import de.lygie.utils.MysqlConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Sendung {


    private List<Dsme> meldungen = new ArrayList<>();


    public void toXML(String filename) throws ParserConfigurationException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("meldungen");
        doc.appendChild(rootElement);

        for (Dsme dsme : meldungen) {
            rootElement.appendChild(dsme.toXMLElement(doc));
        }


        // write dom document to a file
        try (FileOutputStream output =
                     new FileOutputStream(filename)) {
            writeXml(doc, output);
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public void addMeldung(Dsme dsme){
        meldungen.add(dsme);
    }

    private static void writeXml(Document doc,
                                 OutputStream output)
        throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

    public void importFile(File file, int chunksize, String url) throws ParserConfigurationException, IOException, SAXException, SQLException {

        long t1 = System.currentTimeMillis();
        String username = "lygie";
        String password = "lygie";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("meldung");
        String query = "INSERT INTO sendungen (" +
                "kennung," +
                "verfahren," +
                "absendernummer," +
                "empfaengernummer," +
                "erstellungsdatum," +
                "fehlerkennzeichen," +
                "fehleranzahl," +
                "vsnr" +

                ")" +
                " VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (int temp = 0; temp < list.getLength(); temp++) {

            Node node = list.item(temp);
            Element element = (Element) node;
            Dsme dsme = new Dsme();

            String kennung = element.getAttributes().getNamedItem("kennung").getTextContent();
            String verfahren = element.getAttributes().getNamedItem("verfahren").getTextContent();
            String absendernummer = element.getAttributes().getNamedItem("absendernummer").getTextContent();
            String empfaengernummer = element.getAttributes().getNamedItem("empfaengernummer").getTextContent();
            String erstellungsdatum = element.getAttributes().getNamedItem("erstellungsdatum").getTextContent();
            String fehlerkennzeichen  = element.getAttributes().getNamedItem("fehlerkennzeichen").getTextContent();
            String fehleranzahl  = element.getAttributes().getNamedItem("fehleranzahl").getTextContent();
            String vsnr  = element.getAttributes().getNamedItem("vsnr").getTextContent();

            try {
                stmt.setString(1,kennung);
                stmt.setString(2,verfahren);
                stmt.setString(3,absendernummer);
                stmt.setString(4,empfaengernummer);
                stmt.setString(5,erstellungsdatum);
                stmt.setString(6,fehlerkennzeichen);
                stmt.setString(7,fehleranzahl);
                stmt.setString(8,vsnr);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            stmt.addBatch();
            if(temp%chunksize==0){
                stmt.executeBatch();
                stmt.close();
                stmt = connection.prepareStatement(query);
            }

        }
        stmt.executeBatch();
        Double ende = (Double.valueOf(System.currentTimeMillis() - t1) / 1000);
        DecimalFormat f = new DecimalFormat("##.00");
        System.out.println(System.currentTimeMillis() + "|" + list.getLength() + " Datensätze in Datenbank in " +
                ende + " Sekunden");

    }

}
