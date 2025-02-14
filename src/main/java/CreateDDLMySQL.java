
// import java.awt.*;
// import java.awt.event.*;
import javax.swing.*;
// import javax.swing.event.*;
// import java.io.*;
// import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateDDLMySQL extends EdgeConvertCreateDDL {
   private static Logger logger = LogManager.getLogger(CreateDDLMySQL.class.getName());

   protected String databaseName;
   // this array is for determining how MySQL refers to datatypes
   protected String[] strDataType = { "VARCHAR", "BOOL", "INT", "DOUBLE" };
   protected StringBuffer sb;

   public CreateDDLMySQL(EdgeTable[] inputTables, EdgeField[] inputFields) {
      super(inputTables, inputFields);
      logger.info("Constructing CreateDDLMySQL\nTables: " + inputTables + "\nFields: " + inputFields);
      sb = new StringBuffer();
   } // CreateDDLMySQL(EdgeTable[], EdgeField[])

   public CreateDDLMySQL() { // default constructor with empty arg list for to allow output dir to be set
                             // before there are table and field objects
      logger.info("Constructing CreateDDLMySQL using default constructor.");
      sb = new StringBuffer();
   }

   // So we can bypass gui in testing
   public void createDDL() {
      createDDL(null);
   }

   public void createDDL(String name) {
      logger.info("CreateDDL");
      EdgeConvertGUI.setReadSuccess(true);
      if (name == null || name.equals("")) {
         databaseName = generateDatabaseName();
      } else {
         databaseName = name;
      }
      logger.info("Database name: " + databaseName);
      sb.append("CREATE DATABASE " + databaseName + ";\r\n");
      sb.append("USE " + databaseName + ";\r\n");
      logger.info("Generating create table statements");
      // If there are no tables, skip the table creation script
      // This allows us to make an empty db creation script when the default
      // constructor is used instead of just throwing an exception
      if (tables != null) {
         for (int boundCount = 0; boundCount <= maxBound; boundCount++) { // process tables in order from least
                                                                          // dependent (least number of bound tables) to
                                                                          // most dependent
            for (int tableCount = 0; tableCount < numBoundTables.length; tableCount++) { // step through list of tables
               if (numBoundTables[tableCount] == boundCount) { //
                  sb.append("CREATE TABLE " + tables[tableCount].getName() + " (\r\n");
                  logger.info("Creating table " + tables[tableCount].getName());
                  int[] nativeFields = tables[tableCount].getNativeFieldsArray();
                  int[] relatedFields = tables[tableCount].getRelatedFieldsArray();
                  boolean[] primaryKey = new boolean[nativeFields.length];
                  int numPrimaryKey = 0;
                  int numForeignKey = 0;
                  for (int nativeFieldCount = 0; nativeFieldCount < nativeFields.length; nativeFieldCount++) { // print
                                                                                                               // out
                                                                                                               // the
                                                                                                               // fields
                     EdgeField currentField = getField(nativeFields[nativeFieldCount]);
                     sb.append("\t" + currentField.getName() + " " + strDataType[currentField.getDataType()]);
                     logger.debug("Creating field " + currentField.getName() + "\n- Type: "
                           + strDataType[currentField.getDataType()]);
                     if (currentField.getDataType() == 0) { // varchar
                        sb.append("(" + currentField.getVarcharValue() + ")"); // append varchar length in () if data
                                                                               // type is varchar
                        logger.debug("Length: " + currentField.getVarcharValue());
                     }
                     if (currentField.getDisallowNull()) {
                        sb.append(" NOT NULL");
                        logger.debug("Not null");
                     }
                     if (!currentField.getDefaultValue().equals("")) {
                        logger.debug("Default value: " + currentField.getDefaultValue());
                        if (currentField.getDataType() == 1) { // boolean data type
                           sb.append(" DEFAULT " + convertStrBooleanToInt(currentField.getDefaultValue()));
                        } else { // any other data type
                           sb.append(" DEFAULT " + currentField.getDefaultValue());
                        }
                     }
                     if (currentField.getIsPrimaryKey()) {
                        primaryKey[nativeFieldCount] = true;
                        numPrimaryKey++;
                     } else {
                        primaryKey[nativeFieldCount] = false;
                     }
                     if (currentField.getFieldBound() != 0) {
                        numForeignKey++;
                     }
                     // Only put a comma if anything (another field, a PK, or an FK) will appear
                     // after this field
                     if (nativeFieldCount + 1 < nativeFields.length || numPrimaryKey > 0 || numForeignKey > 0) {
                        sb.append(","); // end of field
                     }
                     sb.append("\r\n");
                  }
                  if (numPrimaryKey > 0) { // table has primary key(s)
                     logger.debug("Creating primary keys");
                     sb.append("CONSTRAINT " + tables[tableCount].getName() + "_PK PRIMARY KEY (");
                     for (int i = 0; i < primaryKey.length; i++) {
                        if (primaryKey[i]) {
                           sb.append(getField(nativeFields[i]).getName());
                           numPrimaryKey--;
                           if (numPrimaryKey > 0) {
                              sb.append(", ");
                           }
                        }
                     }
                     sb.append(")");
                     if (numForeignKey > 0) {
                        sb.append(",");
                     }
                     sb.append("\r\n");
                  }
                  if (numForeignKey > 0) { // table has foreign keys
                     logger.debug("Creating foreign keys");
                     int currentFK = 1;
                     for (int i = 0; i < relatedFields.length; i++) {
                        if (relatedFields[i] != 0) {
                           sb.append("CONSTRAINT " + tables[tableCount].getName() + "_FK" + currentFK +
                                 " FOREIGN KEY(" + getField(nativeFields[i]).getName() + ") REFERENCES " +
                                 getTable(getField(nativeFields[i]).getTableBound()).getName() + "("
                                 + getField(relatedFields[i]).getName() + ")");
                           if (currentFK < numForeignKey) {
                              sb.append(",\r\n");
                           }
                           currentFK++;
                        }
                     }
                     sb.append("\r\n");
                  }
                  sb.append(");\r\n\r\n"); // end of table
               }
            }
         }
      }
   }

   protected int convertStrBooleanToInt(String input) { // MySQL uses '1' and '0' for boolean types
      if (input != null && input.toLowerCase().equals("true")) {
         return 1;
      } else {
         return 0;
      }
   }

   public String generateDatabaseName() { // prompts user for database name
      logger.info("Asking for database name");
      String dbNameDefault = "MySQLDB";
      // String databaseName = "";

      do {
         databaseName = (String) JOptionPane.showInputDialog(
               null,
               "Enter the database name:",
               "Database Name",
               JOptionPane.PLAIN_MESSAGE,
               null,
               null,
               dbNameDefault);
         if (databaseName == null) {
            EdgeConvertGUI.setReadSuccess(false);
            return "";
         }
         if (databaseName.equals("")) {
            JOptionPane.showMessageDialog(null, "You must select a name for your database.");
         }
      } while (databaseName.equals(""));
      return databaseName;
   }

   public String getDatabaseName() {
      return databaseName;
   }

   public String getProductName() {
      return "MySQL";
   }

   // So we can bypass gui in testing
   public String getSQLString() {
      return getSQLString(null);
   }

   public String getSQLString(String name) {
      logger.info("Generating SQL string");
      createDDL(name);
      return sb.toString();
   }

}// EdgeConvertCreateDDL
