package de.lygie.model;
import de.lygie.model.enums.Fehlerkennzeichen;
import de.lygie.model.enums.Kennung;
import de.lygie.model.enums.Verfahren;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Random;


public class Dsme {

    private static final Random random = new Random();

    @Enumerated(EnumType.STRING)
    private Kennung kennung;

    @Enumerated(EnumType.STRING)
    private Verfahren verfahren;

    @Size(min=15, max = 15)
    private String absendernummer;

    @Size(min=15, max = 15)
    private String empfaengernummer;

    @Size(min=17, max = 20)
    private String erstellungsdatum;

    @Enumerated(EnumType.STRING)
    private Fehlerkennzeichen fehlerkennzeichen;

    @Min(0) @Max(9)
    private int fehleranzahl;

    private Versicherungsnummer vsnr;


    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public Kennung getKennung() {
        return kennung;
    }

    public void setKennung(Kennung kennung) {
        this.kennung = kennung;
    }

    public Verfahren getVerfahren() {
        return verfahren;
    }

    public void setVerfahren(Verfahren verfahren) {
        this.verfahren = verfahren;
    }

    public String getAbsendernummer() {
        return absendernummer;
    }

    public void setAbsendernummer(String Absendernummer) {
        this.absendernummer = Absendernummer;
    }

    public String getEmpfaengernummer() {
        return empfaengernummer;
    }

    public void setEmpfaengernummer(String empfaengernummer) {
        this.empfaengernummer = empfaengernummer;
    }

    public String getErstellungsdatum() {
        return erstellungsdatum;
    }

    public void setErstellungsdatum(String erstellungsdatum) {
        this.erstellungsdatum = erstellungsdatum;
    }

    public Fehlerkennzeichen getFehlerkennzeichen() {
        return fehlerkennzeichen;
    }

    public void setFehlerkennzeichen(Fehlerkennzeichen fehlerkennzeichen) {
        this.fehlerkennzeichen = fehlerkennzeichen;
    }

    public int getFehleranzahl() {
        return fehleranzahl;
    }

    public void setFehleranzahl(int fehleranzahl) {
        this.fehleranzahl = fehleranzahl;
    }

    public Versicherungsnummer getVsnr() {
        return vsnr;
    }

    public void setVsnr(Versicherungsnummer vsnr) {
        this.vsnr = vsnr;
    }

    public Element toXMLElement(Document doc) throws ParserConfigurationException {

        Element dsmeElement = doc.createElement("meldung");
        dsmeElement.setAttribute("kennung",kennung.toString());
        dsmeElement.setAttribute("verfahren",verfahren.toString());
        dsmeElement.setAttribute("absendernummer",absendernummer);
        dsmeElement.setAttribute("empfaengernummer",empfaengernummer);
        dsmeElement.setAttribute("erstellungsdatum",erstellungsdatum);
        dsmeElement.setAttribute("fehlerkennzeichen", fehlerkennzeichen.toString());
        dsmeElement.setAttribute("fehleranzahl",Integer.toString(fehleranzahl));
        dsmeElement.setAttribute("vsnr",vsnr.generateRandomVersicherungsnummer());


        return dsmeElement;

    }

}
