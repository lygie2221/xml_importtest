package de.lygie.utils;

import de.lygie.model.Dsme;
import de.lygie.model.Versicherungsnummer;
import de.lygie.model.enums.Fehlerkennzeichen;
import de.lygie.model.enums.Kennung;
import de.lygie.model.enums.Verfahren;

import static de.lygie.utils.RandomStringGenerator.generateString;

public class RandomDsmeBuilder {

    public static Dsme generateDsme(){
        Dsme dsme = new Dsme();

        dsme.setKennung(dsme.randomEnum(Kennung.class));
        dsme.setVerfahren(dsme.randomEnum(Verfahren.class));
        dsme.setAbsendernummer(generateString(15));
        dsme.setEmpfaengernummer(generateString(15));
        dsme.setErstellungsdatum(RandomStringGenerator.generateTimeString());
        dsme.setFehlerkennzeichen(dsme.randomEnum(Fehlerkennzeichen.class));
        dsme.setFehleranzahl(0 + (int)(Math.random() * 9));
        dsme.setVsnr(new Versicherungsnummer());

        return dsme;
    }
}
