mvn clean compile assembly:single

##Export
#!/bin/bash
java -jar xml_test.jar export
zip -r xml.zip /tmp/xml


## Import
java -jar xml_test.jar import 10 jdbc:mysql://localhost:3306/dsmetest 6


Beispiel:
java -jar xml_test.jar import 10 jdbc:mysql://localhost:3306/dsmetest 6

