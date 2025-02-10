mvn clean compile assembly:single

## Export
#!/bin/bash
java -jar xml_test.jar export
zip -r xml.zip /tmp/xml

config.ini.example kopieren nach config.ini

## Import
java -jar xml_test.jar import 


Beispiel:
java -jar xml_test.jar import 

## Screencast
https://projekte.lygie.de/xml_performancetest.mp4
https://projekte.lygie.de/xml_performancetest_threads.mp4
