#!/bin/sh
echo "Cleaning existing classes..."
rm -f *.class
# This command looks for matching files and runs the rm command for each file
# Note that {} are replaced with each file name
find . -name \*.class -exec rm {} \;

echo "Compiling source code and unit tests..."
cd src/main
javac -d ../../build -cp lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar CreateDDLMySQL.java EdgeConvertFileParser.java EdgeTable.java EdgeConnector.java EdgeConvertGUI.java ExampleFileFilter.java EdgeConvertCreateDDL.java  EdgeField.java RunEdgeConvert.java
cd ../../
javac -d ../../build -cp :lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:../../build/*.class src/test/EdgeConnectorTest.java
if [ $? -ne 0 ] ; then echo BUILD FAILED!; exit 1; fi
cd ../../
echo "Running unit tests..."
java -cp ./build/*.class:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore EdgeConnectorTest
if [ $? -ne 0 ] ; then echo TESTS FAILED!; exit 1; fi

echo "Running application..."
java -cp ./build/*.class RunEdgeConvert
