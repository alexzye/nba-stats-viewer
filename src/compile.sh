#!/bin/bash
javac NbaData.java
java -cp ./mysql-connector-java-5.1.40-bin.jar:. NbaData
rm *.class
