import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Test;

// test class written by Ben Shapiro
public class EdgeConvertFileParserTest {    

    // globals
    public EdgeConvertFileParser parser;
    public File oneTableEdgeFile = new File("./oneTable.edg");
    public File oneTableSaveFile = new File("./oneTable.sav");
    public File twoTablesEdgeFile = new File("./twoTables.edg");
    public File badSyntaxEdgeFile = new File("./badSyntax.edg");
    public File badLogicEdgeFile = new File("./badLogic.edg");
    public File badSyntaxSaveFile = new File("./badSyntax.sav");
    public File badLogicSaveFile = new File("./badLogic.sav");

    // no @before method here, different methods have setup with different files. There's no universal setup 

    // Method: parseEdgeFile
    @Test
    public void givenParseEdgeFile_whenFileIsValid_thenArrayListsAreValid() {
        parser = new EdgeConvertFileParser(oneTableEdgeFile);
        parser.openFile(oneTableEdgeFile, false);
        try {
            parser.parseEdgeFile(true);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        boolean populatedTableList = parser.getALTables().size() == 1;
        assertThat("Arraylist should be populated: ", populatedTableList, is(true));
    }

    @Test
    public void givenParseEdgeFile_whenFileHasBadSyntax_thenArrayListsAreInvalid() {
        parser = new EdgeConvertFileParser(badSyntaxEdgeFile);
        parser.openFile(badSyntaxEdgeFile, true);
        try {
            parser.parseEdgeFile(true);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        boolean emptyTableList = parser.getALTables().size() == 0;
        assertThat("Arraylist shouldn't be populated: ", emptyTableList, is(true));
    }

    @Test
    public void givenParseEdgeFile_whenFileHasBadLogic_thenMethodAborts() {
        parser = new EdgeConvertFileParser(badLogicEdgeFile);
        parser.openFile(badLogicEdgeFile, true);
        try {
            parser.parseEdgeFile(true);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        boolean emptyTableList = parser.getALTables().size() == 0;
        assertThat("Arraylist shouldn't be populated: ", emptyTableList, is(true));
    }

    // Method: parseSaveFile
    @Test
    public void givenParseSaveFile_whenFileIsValid_thenTablesAreValid() {
        parser = new EdgeConvertFileParser(oneTableSaveFile);
        parser.openFile(oneTableSaveFile, true);
        try {
            parser.parseSaveFile();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        boolean populatedTableList = parser.getALTables().size() == 1;
        assertThat("Arraylist should be populated: ", populatedTableList, is(true));
    }

    @Test(expected = NumberFormatException.class)
    public void givenParseSaveFile_whenFileHasBadSyntax_thenTablesAreInvalid() {
        parser = new EdgeConvertFileParser(badSyntaxSaveFile);
        parser.openFile(badLogicEdgeFile, true);
        try {
            parser.parseSaveFile();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        boolean emptyTableList = parser.getALTables().size() == 0;
        assertThat("Arraylist shouldn't be populated: ", emptyTableList, is(true));
    }

    @Test(expected = NoSuchElementException.class)
    public void givenParseSaveFile_whenFileHasBadLogic_thenTablesAreInvalid() {
        parser = new EdgeConvertFileParser(badLogicSaveFile);
        parser.openFile(badLogicSaveFile, true);
        try {
            parser.parseSaveFile();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        boolean emptyTableList = parser.getALTables().size() == 0;
        assertThat("Arraylist shouldn't be populated: ", emptyTableList, is(true));
    }

    // Method: openFile
    @Test
    public void givenOpenFile_whenFileBeginsWithEDGE_ID_thenCallParseEdgeFile() {
        parser = new EdgeConvertFileParser(oneTableEdgeFile);
        parser.openFile(oneTableEdgeFile, true);
        assertThat("detected file type is equal to: edge", parser.checkFileType(), is("edge"));
    }

    @Test
    public void givenOpenFile_whenFileBeginsWithSAVE_ID_thenCallParseSaveFile() {
        parser = new EdgeConvertFileParser(oneTableSaveFile);
        parser.openFile(oneTableSaveFile, true);
        assertThat("detected file type is equal to: edge", parser.checkFileType(), is("save"));
    }

    @Test
    public void givenOpenFile_whenInvalidFileStartsWithEDGE_ID_thenOpenWithoutError() {
        parser = new EdgeConvertFileParser(badSyntaxEdgeFile);
        parser.openFile(badSyntaxEdgeFile, true);
    }

    @Test
    public void givenOpenFile_whenInvalidFileStartsWithSAVE_ID_thenOpenWithoutError() {
        parser = new EdgeConvertFileParser(badSyntaxSaveFile);
        parser.openFile(badSyntaxSaveFile, true);
    }

    // Method: resolveConnectors
    @Test(expected = NullPointerException.class)
    public void givenResolveConnectors_whenArraysUninitialized_thenNullPointer() {
        parser = new EdgeConvertFileParser(oneTableEdgeFile);
        parser.setEdgeConnectors(null);
        parser.setEdgeTables(null);
        parser.setEdgeFields(null);
        parser.resolveConnectors(true);

    }

    @Test
    public void givenResolveConnectors_whenTwoFieldEndpoints_thenWarnUser() {
        parser = new EdgeConvertFileParser(oneTableEdgeFile);
        EdgeConnector[] connectors = {
            new EdgeConnector("9|7|7|null|null"),
        };
        connectors[0].setIsEP1Field(true);
        connectors[0].setIsEP2Field(true);
        EdgeField[] fields = {
            new EdgeField("7|Grade")
        };
        EdgeTable[] tables = {};
        parser.setEdgeConnectors(connectors);
        parser.setEdgeFields(fields);
        parser.setEdgeTables(tables);
        parser.resolveConnectors(true);
        assertThat("Connectors are not valid due to connector joining two fields", parser.getConnectorValidity(), is(false));
    }

    @Test
    public void givenResolveConnectors_whenFieldConnectsToMultipleTables_thenWarnUser() {
        parser = new EdgeConvertFileParser(twoTablesEdgeFile);
        EdgeConnector[] connectors = {
            new EdgeConnector("9|1|7|null|null"),
            new EdgeConnector("10|2|7|null|null")
        };
        connectors[0].setIsEP1Table(true);
        connectors[0].setIsEP2Field(true);
        connectors[1].setIsEP1Table(true);
        connectors[1].setIsEP2Field(true);
        EdgeField[] fields = {
            new EdgeField("7|Grade")
        };
        EdgeTable[] tables = {
            new EdgeTable("1|Student"),
            new EdgeTable("2|Course")
        };
        parser.setEdgeConnectors(connectors);
        parser.setEdgeFields(fields);
        parser.setEdgeTables(tables);
        parser.resolveConnectors(true);
        assertThat("Connectors are not valid due to attribute connected to multiple tables", parser.getConnectorValidity(), is(false));
    }

    @Test
    public void givenResolveConnectors_whenManyToManyRel_thenWarnUser() {
        parser = new EdgeConvertFileParser(oneTableEdgeFile);
        EdgeConnector[] connectors = {
            new EdgeConnector("9|1|2|many|many"),
        };
        connectors[0].setIsEP1Table(true);
        connectors[0].setIsEP2Table(true);
        EdgeField[] fields = {};
        EdgeTable[] tables = {
            new EdgeTable("1|Student"),
            new EdgeTable("2|Course")
        };
        parser.setEdgeConnectors(connectors);
        parser.setEdgeFields(fields);
        parser.setEdgeTables(tables);
        parser.resolveConnectors(true);
        assertThat("Connectors are not valid due many to many relationship", parser.getConnectorValidity(), is(false));
    }

    @Test
    public void givenResolveConnectors_whenConnectorsArrayValid_thenConnectorsResolve() {
        parser = new EdgeConvertFileParser(oneTableEdgeFile);
        EdgeConnector[] connectors = {
            new EdgeConnector("9|1|2|one|mamy")
        };
        connectors[0].setIsEP1Table(true);
        connectors[0].setIsEP2Field(true);
        EdgeField[] fields = {
            new EdgeField("2|Grade")
        };
        EdgeTable[] tables = {
            new EdgeTable("1|Student")
        };
        parser.setEdgeConnectors(connectors);
        parser.setEdgeFields(fields);
        parser.setEdgeTables(tables);
        parser.resolveConnectors(true);
        assertThat("Connectors are not valid due many to many relationship", parser.getConnectorValidity(), is(true));
    }

    // Method: makeArrays
    @Test
    public void givenMakeArrays_whenALsAreNull_thenMethodDoesNothing() {
        parser = new EdgeConvertFileParser(oneTableEdgeFile);
        parser.setALTables(null);
        parser.setALFields(null);
        parser.setALConnectors(null);
        parser.setEdgeTables(null);
        parser.setEdgeFields(null);
        parser.setEdgeConnectors(null);
        parser.makeArrays();
        boolean arraysAreNull = ( parser.getEdgeTables() == null 
                               && parser.getEdgeFields() == null 
                               && parser.getEdgeConnectors() == null);
        assertThat("parser arrays are null", arraysAreNull, is(true));
    }

    @Test
    public void givenMakeArrays_whenALsAreValid_thenMethodMakesArrays() {
        parser = new EdgeConvertFileParser(oneTableEdgeFile);
        ArrayList<EdgeConnector> alConnectors = new ArrayList<EdgeConnector>();
        ArrayList<EdgeField> alFields = new ArrayList<EdgeField>();
        ArrayList<EdgeTable> alTables = new ArrayList<EdgeTable>();
        alConnectors.add(new EdgeConnector("9|1|2|one|mamy"));
        alFields.add(new EdgeField("2|Grade"));
        alTables.add(new EdgeTable("1|Student"));
        parser.setALConnectors(alConnectors);
        parser.setALFields(alFields);
        parser.setALTables(alTables);
        parser.makeArrays();
        boolean arraysAreNull = ( parser.getEdgeTables() == null 
                               && parser.getEdgeFields() == null 
                               && parser.getEdgeConnectors() == null);
        assertThat("parser arrays are instantiated", arraysAreNull, is(false));
    }

    @Test
    public void givenMakeArrays_whenALsAreEmpty_thenMethodMakesEmptyArrays() {
        parser = new EdgeConvertFileParser(oneTableEdgeFile);
        parser.setALTables(new ArrayList<EdgeTable>());
        parser.setALFields(new ArrayList<EdgeField>());
        parser.setALConnectors(new ArrayList<EdgeConnector>());
        parser.makeArrays();
        boolean arraysAreEmpty = ( parser.getEdgeTables().length == 0 
                               && parser.getEdgeFields().length == 0 
                               && parser.getEdgeConnectors().length == 0);
        assertThat("parser arrays are instantiated and empty", arraysAreEmpty, is(true));
    }
}
