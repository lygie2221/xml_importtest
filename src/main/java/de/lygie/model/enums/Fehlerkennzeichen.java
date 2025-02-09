package de.lygie.model.enums;

public enum Fehlerkennzeichen {
    FEHLERFREI("0"),
    FEHLERHAFT("1"),
    MANUELLRENTE("2"),
    MANUELLBA("3"),
    EINZUG("4");


    private final String value;

    public String getValue() { return value; }

    Fehlerkennzeichen(final String newValue) {
        value = newValue;
    }

}
