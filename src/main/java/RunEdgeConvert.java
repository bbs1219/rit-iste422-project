import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RunEdgeConvert {

   public static void main(String[] args) {
      Logger logger = LogManager.getLogger(RunEdgeConvert.class);
      logger.info("Application started!");

      EdgeConvertGUI edge = new EdgeConvertGUI();
   }
}