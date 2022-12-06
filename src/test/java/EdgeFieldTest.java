import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

// Test class written by Kelly Appleton
public class EdgeFieldTest {
    EdgeField testObj;
    EdgeField testObjInvalid;

    @Before
    public void setUp() throws Exception {
        // expected string to pass in
        testObj = new EdgeField("3|Grade");
    }

    // Test constructor:

    // Test constructor with null input
    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        testObjInvalid = new EdgeField(null);
    }

    // Test constructor with empty string input
    @Test(expected = NoSuchElementException.class)
    public void testEmptyStringConstructor() {
        testObjInvalid = new EdgeField("");
    }

    // Test constructor with invalid data types - first data type - given string,
    // should be number
    @Test(expected = NumberFormatException.class)
    public void testInvalidDataTypes1Constructor() {
        testObjInvalid = new EdgeField("Hi|Yo");
    }

    // Test getters:

    @Test
    public void testGetNumFigure() {
        assertEquals("numFigure was intialized to 3", 3, testObj.getNumFigure());
    }

    @Test
    public void testGetName() {
        assertEquals("name was intialized as Grade", "Grade", testObj.getName());
    }

    @Test
    public void testGetTableID() {
        assertEquals("tableID was intialized to 0", 0, testObj.getTableID());
    }

    @Test
    public void testGetTableBound() {
        assertEquals("tableBound was intialized to 0", 0, testObj.getTableBound());
    }

    @Test
    public void testGetFieldBound() {
        assertEquals("fieldBound should be 0", 0, testObj.getFieldBound());
    }

    @Test
    public void testGetDisallowNull() {
        assertEquals("disallowNull should be false", false, testObj.getDisallowNull());
    }

    @Test
    public void testGetIsPrimaryKey() {
        assertEquals("isPrimaryKey should be false", false, testObj.getIsPrimaryKey());
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals("defaultValue should be an empty string", "", testObj.getDefaultValue());
    }

    @Test
    public void testGetVarcharValue() {
        assertEquals("varchar value should be 1", 1, testObj.getVarcharValue());
    }

    @Test
    public void testGetDataType() {
        assertEquals("dataType value should be 0", 0, testObj.getDataType());
    }

    // Test toString:

    @Test
    public void testToString() {
        assertEquals("toString value should be '3|Grade|0|0|0|0|1|false|false|'", "3|Grade|0|0|0|0|1|false|false|",
                testObj.toString());
    }

    // Test setters:

    @Test
    public void testSetTableID() {
        testObj.setTableID(5);
        assertEquals("tableID should be what you set it to: 5", 5, testObj.getTableID());
    }

    @Test
    public void testSetBound() {
        testObj.setTableBound(5);
        assertEquals("tableBound should be what you set it to: 5", 5, testObj.getTableBound());
    }

    @Test
    public void testSetFieldBound() {
        testObj.setFieldBound(5);
        assertEquals("fieldBound should be what you set it to: 5", 5, testObj.getFieldBound());
    }

    @Test
    public void testSetDisallowNull() {
        testObj.setDisallowNull(true);
        assertEquals("disallowNull should be what you set it to: true", true, testObj.getDisallowNull());
        testObj.setDisallowNull(false);
        assertEquals("disallowNull should be what you set it to: false", false, testObj.getDisallowNull());
    }

    @Test
    public void testSetIsPrimaryKey() {
        testObj.setIsPrimaryKey(true);
        assertEquals("isPrimaryKey should be what you set it to: true", true, testObj.getIsPrimaryKey());
        testObj.setIsPrimaryKey(false);
        assertEquals("isPrimaryKey should be what you set it to: true", false, testObj.getIsPrimaryKey());
    }

    @Test
    public void testSetDefaultValue() {
        testObj.setDefaultValue("default");
        assertEquals("defaultValue should be what you set it to: 'default'", "default", testObj.getDefaultValue());
    }

    @Test
    public void testSetVarcharValue() {
        testObj.setVarcharValue(2);
        assertEquals("varcharValue should be what you set it to: 2", 2, testObj.getVarcharValue());
    }

    @Test
    public void testSetDataType() {
        testObj.setDataType(2);
        assertEquals("dataType should be what you set it to: 2", 2, testObj.getDataType());
    }

}
