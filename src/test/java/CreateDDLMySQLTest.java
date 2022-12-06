// JUnit assersion
import static org.junit.Assert.*;

// JUnit is
import static org.hamcrest.CoreMatchers.*;

// JUnit test notation
import org.junit.Test;

// Test class written by SJ Miller
public class CreateDDLMySQLTest {
    // Test object
    CreateDDLMySQL testObj;
    // Test data
    String dbName = "TestDB";
    String tableName1 = "Person";
    String tableName2 = "Dog";

    // Coding note:
    // I cannot create one "before" that makes the same instance of testObj for each test
    // Because testObj constructor is something that I want to test, with different inputs depending on what I'm testing
    // So instead, I'm going straight into the tests
    
    // Test to verify the product name
    @Test
    public void verifyProductName() {
        // New object, empty constructor
        testObj = new CreateDDLMySQL();
        // Should return MySQL
        assertThat("getProductName - Product should by MySQL ", testObj.getProductName(), is("MySQL"));
    }

    // Test to verify the boolean string to integer conversion
    @Test
    public void verifyBooleanConversion() {
        // New object, empty constructor
        testObj = new CreateDDLMySQL();
        // "true" should be 1
        assertThat("convertStrBooleanToInt - String 'true' should evaluate to int 1 ", testObj.convertStrBooleanToInt("true"), is(1));
        // "True" should be 1
        assertThat("convertStrBooleanToInt - String 'True' should evaluate to int 1 ", testObj.convertStrBooleanToInt("True"), is(1));
        // "TRUE" should be 1
        assertThat("convertStrBooleanToInt - String 'TRUE' should evaluate to int 1 ", testObj.convertStrBooleanToInt("TRUE"), is(1));
        // "false" should be 0
        assertThat("convertStrBooleanToInt - String 'false' should evaluate to int 0 ", testObj.convertStrBooleanToInt("false"), is(0));
        // "blah" should be 0
        assertThat("convertStrBooleanToInt - String 'true' should evaluate to int 0 ", testObj.convertStrBooleanToInt("blah"), is(0));
        // empty string should be 0
        assertThat("convertStrBooleanToInt - Empty String should evaluate to int 0 ", testObj.convertStrBooleanToInt(""), is(0));
        // null string should be 0
        assertThat("convertStrBooleanToInt - Null String should evaluate to int 0 ", testObj.convertStrBooleanToInt(null), is(0));
    }

    // Test to verify db creation script with no input from constructor
    @Test
    public void createNoInput() {
        // New object, empty constructor
        testObj = new CreateDDLMySQL();
        // Get the script generated and split per line
        String script = testObj.getSQLString(dbName);
        String[] splitScript = script.split("\r\n");
        assertThat("createNoInput - Error in DB creation line ", splitScript[0], is("CREATE DATABASE " + dbName + ";"));
        assertThat("createNoInput - Error in use DB line ", splitScript[1], is("USE " + dbName + ";"));
        
    }

    // Test to verify db creation script with empty list input from constructor
    @Test
    public void createEmptyInput() {
        // New object, empty lists for constructor
        testObj = new CreateDDLMySQL(new EdgeTable[0], new EdgeField[0]);

        // Get the script generated and split per line
        String script = testObj.getSQLString(dbName);
        String[] splitScript = script.split("\r\n");

        // DB Creation line
        assertThat("createNoInput - Error in DB creation line ", splitScript[0], is("CREATE DATABASE " + dbName + ";"));
        // Use db line
        assertThat("createNoInput - Error in use DB line ", splitScript[1], is("USE " + dbName + ";"));
    }

    // Test to verify db creation script with empty list input from constructor
    @Test
    public void createTablesOnly() {
        // New object, correct tables with empty fields for constructor
        EdgeTable[] tables = new EdgeTable[2];
        tables[0] = new EdgeTable("1|" + tableName1);
        tables[0].makeArrays();
        tables[1] = new EdgeTable("2|" + tableName2);
        tables[1].makeArrays();
        testObj = new CreateDDLMySQL(tables, new EdgeField[0]);

        // Get the script generated and split per line
        String script = testObj.getSQLString(dbName);
        String[] splitScript = script.split("\r\n");

        // DB Creation line
        assertThat("createTablesOnly - Error in DB creation line ", splitScript[0], is("CREATE DATABASE " + dbName + ";"));
        // Use DB Line
        assertThat("createTablesOnly - Error in use DB line ", splitScript[1], is("USE " + dbName + ";"));
        // Create tables
        assertThat("createTablesOnly - Error in create table line ", splitScript[2], is("CREATE TABLE " + tableName1 + " ("));
        assertThat("createTablesOnly - Error in closing create table line ", splitScript[3], is(");"));
        assertThat("createTablesOnly - Error in create table line ", splitScript[5], is("CREATE TABLE " + tableName2 + " ("));
        assertThat("createTablesOnly - Error in closing create table line ", splitScript[6], is(");"));
    }

    // Test to verify db creation script with correct and complete input
    @Test
    public void createCorrectInput() {
        // New object, correct input for constructor
        EdgeTable[] tables = new EdgeTable[2];
        EdgeField[] fields = new EdgeField[7];
        tables[0] = new EdgeTable("1|" + tableName1);
        fields[0] = new EdgeField("2|ID");
        fields[0].setTableID(1);
        fields[0].setDataType(2);
        fields[0].setDisallowNull(true);
        fields[0].setIsPrimaryKey(true);
        tables[0].addNativeField(2);
        fields[1] = new EdgeField("3|Name");
        fields[1].setTableID(1);
        fields[1].setDataType(0);
        fields[1].setVarcharValue(50);
        fields[1].setDisallowNull(true);
        tables[0].addNativeField(3);
        tables[1] = new EdgeTable("4|" + tableName2);
        fields[2] = new EdgeField("5|ID");
        fields[2].setTableID(2);
        fields[2].setDataType(2);
        fields[2].setDisallowNull(true);
        fields[2].setIsPrimaryKey(true);
        tables[1].addNativeField(5);
        fields[3] = new EdgeField("6|Owner");
        fields[3].setTableID(2);
        fields[3].setDataType(2);
        fields[3].setDisallowNull(true);
        fields[3].setTableBound(1);
        fields[3].setFieldBound(2);
        tables[1].addNativeField(6);
        tables[1].addRelatedTable(1);
        fields[4] = new EdgeField("7|Name");
        fields[4].setTableID(2);
        fields[4].setDataType(0);
        fields[4].setVarcharValue(25);
        fields[4].setDisallowNull(true);
        tables[1].addNativeField(7);
        fields[5] = new EdgeField("8|Weight");
        fields[5].setTableID(2);
        fields[5].setDataType(3);
        tables[1].addNativeField(8);
        fields[6] = new EdgeField("9|IsSmallDog");
        fields[6].setTableID(2);
        fields[6].setDataType(1);
        tables[1].addNativeField(9);
        tables[1].makeArrays();
        tables[0].makeArrays();
        tables[1].setRelatedField(1, 2);
        testObj = new CreateDDLMySQL(tables, fields);

        // Get the script generated and split per line
        String script = testObj.getSQLString(dbName);
        String[] splitScript = script.split("\r\n");


        // DB Creation line
        assertThat("createCorrectInput - Error in DB creation line ", splitScript[0], is("CREATE DATABASE " + dbName + ";"));
        // Use DB Line
        assertThat("createCorrectInput - Error in use DB line ", splitScript[1], is("USE " + dbName + ";"));
        // Create tables
        // Person
        assertThat("createCorrectInput - Person - Error in create table line ", splitScript[2], is("CREATE TABLE " + tableName1 + " ("));
        assertThat("createCorrectInput - Person - Error in int field creation line ", splitScript[3], is("\tID INT NOT NULL,"));
        assertThat("createCorrectInput - Person - Error in varchar field creation line ", splitScript[4], is("\tName VARCHAR(50) NOT NULL,"));
        assertThat("createCorrectInput - Person - Error in primary key creation line ", splitScript[5], is("CONSTRAINT Person_PK PRIMARY KEY (ID)"));
        assertThat("createCorrectInput - Person - Error in closing create table line ", splitScript[6], is(");"));
        // Dog
        assertThat("createCorrectInput - Dog - Error in create table line ", splitScript[8], is("CREATE TABLE " + tableName2 + " ("));
        assertThat("createCorrectInput - Dog - Error in int field creation line ", splitScript[9], is("\tID INT NOT NULL,"));
        assertThat("createCorrectInput - Dog - Error in int field creation line ", splitScript[10], is("\tOwner INT NOT NULL,"));
        assertThat("createCorrectInput - Dog - Error in varchar field creation line ", splitScript[11], is("\tName VARCHAR(25) NOT NULL,"));
        assertThat("createCorrectInput - Dog - Error in double field creation line ", splitScript[12], is("\tWeight DOUBLE,"));
        assertThat("createCorrectInput - Dog - Error in boolean field creation line ", splitScript[13], is("\tIsSmallDog BOOL,"));
        assertThat("createCorrectInput - Dog - Error in primary key creation line ", splitScript[14], is("CONSTRAINT Dog_PK PRIMARY KEY (ID),"));
        assertThat("createCorrectInput - Dog - Error in foreign key creation line ", splitScript[15], is("CONSTRAINT Dog_FK1 FOREIGN KEY(Owner) REFERENCES Person(ID)"));
        assertThat("createCorrectInput - Dog - Error in closing create table line ", splitScript[16], is(");"));
    }

    // Test to verify db creation script with an invalid foreign key
    @Test
    public void createInvalidFK() {
        // New object, correct input for constructor
        EdgeTable[] tables = new EdgeTable[2];
        EdgeField[] fields = new EdgeField[5];
        tables[0] = new EdgeTable("1|" + tableName1);
        fields[0] = new EdgeField("2|ID");
        fields[0].setTableID(1);
        fields[0].setDataType(2);
        fields[0].setDisallowNull(true);
        fields[0].setIsPrimaryKey(true);
        tables[0].addNativeField(2);
        fields[1] = new EdgeField("3|Name");
        fields[1].setTableID(1);
        fields[1].setDataType(0);
        fields[1].setVarcharValue(50);
        fields[1].setDisallowNull(true);
        tables[0].addNativeField(3);
        tables[1] = new EdgeTable("4|" + tableName2);
        fields[2] = new EdgeField("5|ID");
        fields[2].setTableID(2);
        fields[2].setDataType(2);
        fields[2].setDisallowNull(true);
        fields[2].setIsPrimaryKey(true);
        tables[1].addNativeField(5);
        fields[3] = new EdgeField("6|Owner");
        fields[3].setTableID(2);
        fields[3].setDataType(2);
        fields[3].setDisallowNull(true);
        fields[3].setTableBound(1);
        fields[3].setFieldBound(2);
        tables[1].addNativeField(6);
        tables[1].addRelatedTable(1);
        fields[4] = new EdgeField("7|Name");
        fields[4].setTableID(2);
        fields[4].setDataType(0);
        fields[4].setVarcharValue(25);
        fields[4].setDisallowNull(true);
        tables[1].addNativeField(7);
        tables[1].makeArrays();
        tables[0].makeArrays();
        tables[1].setRelatedField(1, -1); // this is the change
        testObj = new CreateDDLMySQL(tables, fields);

        // Get the script generated and split per line
        try {
            String script = testObj.getSQLString(dbName);
            fail("createInvalidFK - Should have raised a NullPointerException");
        }
        catch (NullPointerException ne) {
            System.out.println(ne.getMessage());
        }
    }

    // Test to verify db creation script with a table missing a primary key
    @Test
    public void createNoPK() {
        // New object, correct input for constructor
        EdgeTable[] tables = new EdgeTable[1];
        EdgeField[] fields = new EdgeField[2];
        tables[0] = new EdgeTable("1|" + tableName1);
        fields[0] = new EdgeField("2|ID");
        fields[0].setTableID(1);
        fields[0].setDataType(2);
        fields[0].setDisallowNull(true);
        tables[0].addNativeField(2);
        fields[1] = new EdgeField("3|Name");
        fields[1].setTableID(1);
        fields[1].setDataType(0);
        fields[1].setVarcharValue(50);
        fields[1].setDisallowNull(true);
        tables[0].addNativeField(3);
        tables[0].makeArrays();
        testObj = new CreateDDLMySQL(tables, fields);

        // Get the script generated and split per line
        String script = testObj.getSQLString(dbName);
        String[] splitScript = script.split("\r\n");

        // DB Creation line
        assertThat("createNoPK - Error in DB creation line ", splitScript[0], is("CREATE DATABASE " + dbName + ";"));
        // Use DB Line
        assertThat("createNoPK - Error in use DB line ", splitScript[1], is("USE " + dbName + ";"));
        // Create tables
        // Person
        assertThat("createNoPK - Person - Error in create table line ", splitScript[2], is("CREATE TABLE " + tableName1 + " ("));
        assertThat("createNoPK - Person - Error in int field creation line ", splitScript[3], is("\tID INT NOT NULL,"));
        assertThat("createNoPK - Person - Error in varchar field creation line ", splitScript[4], is("\tName VARCHAR(50) NOT NULL"));
        assertThat("createNoPK - Person - Error in closing create table line ", splitScript[5], is(");"));
    }

}