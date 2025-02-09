scp -P10022 root@lygie.de:~/xml.zip .
unzip xml.zip
mv tmp/xml /tmp/xml
rmdir tmp
rm xml.zip
java -jar xml_test.jar import
