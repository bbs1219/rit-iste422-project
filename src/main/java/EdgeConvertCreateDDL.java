
// import java.awt.*;
// import java.awt.event.*;
// import javax.swing.*;
// import javax.swing.event.*;
// import java.io.*;
// import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EdgeConvertCreateDDL {
   private static Logger logger = LogManager.getLogger(EdgeConvertCreateDDL.class.getName());

   static String[] products = { "MySQL" };
   protected EdgeTable[] tables; // master copy of EdgeTable objects
   protected EdgeField[] fields; // master copy of EdgeField objects
   protected int[] numBoundTables;
   protected int maxBound;
   protected StringBuffer sb;
   protected int selected;

   public EdgeConvertCreateDDL(EdgeTable[] tables, EdgeField[] fields) {
      // System.out.println("EdgeConvertCreateDDL - EdgeConvertCreateDDL");
      logger.info("Constructing EdgeConvertCreateDDL\nTables: " + tables + "\nFields: " + fields);
      this.tables = tables;
      this.fields = fields;
      initialize();
   } // EdgeConvertCreateDDL(EdgeTable[], EdgeField[])

   public EdgeConvertCreateDDL() { // default constructor with empty arg list for to allow output dir to be set
                                   // before there are table and field objects
      logger.info("Constructing EdgeConvertCreateDDL using default constructor.");
   } // EdgeConvertCreateDDL()

   public void initialize() {
      // System.out.println("EdgeConvertCreateDDL - initialize");
      logger.info("Initialize");
      numBoundTables = new int[tables.length];
      logger.info("Number of tables: " + numBoundTables);
      maxBound = 0;
      sb = new StringBuffer();

      logger.info("Finding maximum bound");
      for (int i = 0; i < tables.length; i++) { // step through list of tables
         int numBound = 0; // initialize counter for number of bound tables
         int[] relatedFields = tables[i].getRelatedFieldsArray();
         for (int j = 0; j < relatedFields.length; j++) { // step through related fields list
            if (relatedFields[j] != 0) {
               numBound++; // count the number of non-zero related fields
            }
         }
         logger.info("Table " + (i + 1) + " number of bounds: " + numBound);
         numBoundTables[i] = numBound;
         if (numBound > maxBound) {
            maxBound = numBound;
         }
      }
      logger.info("Max bound: " + maxBound);
   }

   protected EdgeTable getTable(int numFigure) {
      // System.out.println("EdgeConvertCreateDDL - EdgeTable");
      logger.info("Getting table with numFigure " + numFigure);
      for (int tIndex = 0; tIndex < tables.length; tIndex++) {
         if (numFigure == tables[tIndex].getNumFigure()) {
            logger.info("Table found at index " + tIndex);
            return tables[tIndex];
         }
      }
      return null;
   }

   protected EdgeField getField(int numFigure) {
      // System.out.println("EdgeConvertCreateDDL - EdgeField");
      logger.info("Getting field with numFigure " + numFigure);
      for (int fIndex = 0; fIndex < fields.length; fIndex++) {
         if (numFigure == fields[fIndex].getNumFigure()) {
            logger.info("Field found at index " + fIndex);
            return fields[fIndex];
         }
      }
      return null;
   }

   public abstract String getDatabaseName();

   public abstract String getProductName();

   public abstract String getSQLString();

   public abstract void createDDL();

}// EdgeConvertCreateDDL
