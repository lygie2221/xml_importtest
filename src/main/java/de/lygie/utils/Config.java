package de.lygie.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {

    private int chunksize = 100;
    private int threads = 4;
    private String url = "";
    private String username = "";
    private String password = "";
    private String folder ="";
    private int testQuantity=1000000;

    private Map<String, Properties> rawInputFromIni;

    public Config() throws IOException {

        FileReader config_ini = new FileReader("config.ini");
        parseINI(config_ini);

        chunksize = Integer.parseInt(rawInputFromIni.get("import").getProperty("chunksize"));
        threads = Integer.parseInt(rawInputFromIni.get("import").getProperty("threads"));
        url = (rawInputFromIni.get("import").getProperty("url"));
        username = (rawInputFromIni.get("import").getProperty("username"));
        password = (rawInputFromIni.get("import").getProperty("password"));

        folder = (rawInputFromIni.get("general").getProperty("folder"));
        testQuantity = Integer.parseInt(rawInputFromIni.get("export").getProperty("testQuantity"));


    }

    private void parseINI(Reader reader) throws IOException {
        Map<String, Properties> result = new HashMap<>();
        new Properties() {

            private Properties section;

            @Override
            public Object put(Object key, Object value) {
                String header = (((String) key) + " " + value).trim();
                if (header.startsWith("[") && header.endsWith("]"))
                    return result.put(header.substring(1, header.length() - 1),
                            section = new Properties());
                else
                    return section.put(key, value);
            }

        }.load(reader);
        rawInputFromIni = result;
    }

    public int getChunksize() {
        return chunksize;
    }

    public int getThreads() {
        return threads;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFolder() {
        return folder;
    }

    public int getTestQuantity() {
        return testQuantity;
    }
}
