package de.lygie.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class RandomStringGenerator {

    public static String generateString(int max) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        if(max > 0){
            uuid=uuid.substring(0,max);
        }

        return uuid;
    }

    public static String generateTimeString(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

}
