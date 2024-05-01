package program.businessLogic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/** Reads from or writes to single-level XML file */
public interface IXMLManager {

  Map<String, String> readXML(String path)
      throws ParserConfigurationException, IOException, SAXException;

  void writeXML(String path, Map<String, String> params, String rootName)
      throws ParserConfigurationException, TransformerException, FileNotFoundException;
}
