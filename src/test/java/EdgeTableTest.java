import static org.junit.Assert.*;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;

public class EdgeTableTest{
   EdgeTable testObj;
   EdgeTable testObjInvalid;
   int[] testArray = {};
   
   // Test a proper creation
   @Before
   public void setUp() throws Exception {
       testObj = new EdgeTable("2|FACULTY");
   }
   
   // Test creations with invalid inputs
   
   @Test(expected = NullPointerException.class)
   public void testNullConstructor() {
       testObjInvalid = new EdgeTable(null);
   }
   
   @Test(expected = NoSuchElementException.class)
   public void testEmptyStringConstructor() {
       testObjInvalid = new EdgeTable("");
   }
    
   @Test(expected = NumberFormatException.class)
   public void testInvalidDataTypes1Constructor() {
       testObjInvalid = new EdgeTable("Number|Text");
   }
   
   // Test get functions
   
   @Test
   public void testGetNumFigure() {
      assertEquals("Correct numFigure was initialized - 2", 2, testObj.getNumFigure());
   }
   
   @Test
   public void testGetName() {
      assertEquals("Correct name was initialized - FACULTY", "FACULTY", testObj.getName());
   }
   
   @Test
   public void testGetRelatedTablesArray() {
      testObj.makeArrays();
      assertArrayEquals("Related tables was correctly initialized empty", testArray, testObj.getRelatedTablesArray());
   }
   
   @Test
   public void testGetRelatedFieldsArray() {
      testObj.makeArrays();
      assertArrayEquals("Related fields was correctly initialized empty", testArray, testObj.getRelatedFieldsArray());
   }
   
   @Test
   public void testGetNativeFieldsArray() {
      testObj.makeArrays();
      assertArrayEquals("Native fields was correctly initialized empty", testArray, testObj.getNativeFieldsArray());
   }
   
   // Test toString
   
   @Test
   public void testToString() {
      testObj.makeArrays();
      assertEquals("toString value should be 'Table: 2\r\n{\r\nTableName: FACULTY\r\nNativeFields: \r\nRelatedTables: \r\nRelatedFields: \r\n}\r\n'", 
         "Table: 2\r\n{\r\nTableName: FACULTY\r\nNativeFields: \r\nRelatedTables: \r\nRelatedFields: \r\n}\r\n", testObj.toString());
   }
   
   // Test additions
   
   @Test
   public void testAddRelatedTable() {
      testObj.addRelatedTable(13);
      testObj.makeArrays();
      int[] relatedTables = testObj.getRelatedTablesArray();
      assertEquals("Related table successfully entered", 13, relatedTables[0]);
   }
   
   @Test
   public void testSetRelatedField() {
      testObj.addNativeField(0);
      testObj.makeArrays();
      testObj.setRelatedField(0, 3);
      int[] relatedFields = testObj.getRelatedFieldsArray();
      assertEquals("Related field successfully entered", 3, relatedFields[0]);
   }
   
   @Test
   public void testAddNativeField() {
      testObj.addNativeField(11);
      testObj.makeArrays();
      int[] nativeFields = testObj.getNativeFieldsArray();
      assertEquals("Native field successfully entered", 11, nativeFields[0]);   
   }
   
   // Test moving fields
   
   @Test
   public void testMoveFieldDown() {
      testObj.addNativeField(11);
      testObj.addNativeField(12);
      testObj.makeArrays();
      testObj.moveFieldDown(0);
      int[] nativeFields = testObj.getNativeFieldsArray();
      assertEquals("Native field successfully moved down", 11, nativeFields[1]);
   }
   
   @Test
   public void testMoveFieldUp() {
      testObj.addNativeField(12);
      testObj.addNativeField(11);
      testObj.makeArrays();
      testObj.moveFieldUp(1);
      int[] nativeFields = testObj.getNativeFieldsArray();
      assertEquals("Native field successfully moved up", 11, nativeFields[0]);
   }
}