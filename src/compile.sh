#!/bin/bash
javac NbaData.java SpringUtilities.java
java -cp ./mysql-connector-java-5.1.40-bin.jar:. NbaData
rm *.class
