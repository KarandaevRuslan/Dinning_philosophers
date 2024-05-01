package program.businessLogic;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** Reads from or writes to single-level XML file */
public class XMLManager implements IXMLManager {

  /** Reads map of properties names and their values from single-level XML file */
  @Override
  public Map<String, String> readXML(String path)
      throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document dom = db.parse(path);

    Element doc = dom.getDocumentElement();
    NodeList nodeList = doc.getChildNodes();

    var hm = new HashMap<String, String>();
    for (int i = 0; i < nodeList.getLength(); i++) {
      var node = nodeList.item(i);
      if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
        var name = node.getNodeName();
        var value = node.getFirstChild().getNodeValue();
        hm.put(name, value);
      }
    }

    return hm;
  }

  /**
   * Writes map of properties names and their values to single-level XML file with specified name of
   * root
   */
  @Override
  public void writeXML(String path, Map<String, String> params, String rootName)
      throws ParserConfigurationException, TransformerException, FileNotFoundException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document dom = db.newDocument();

    Element rootEle = dom.createElement(rootName);

    params.forEach(
        (property, value) -> {
          Element e = dom.createElement(property);
          e.appendChild(dom.createTextNode(value));
          rootEle.appendChild(e);
        });

    dom.appendChild(rootEle);

    Transformer tr = TransformerFactory.newInstance().newTransformer();
    tr.setOutputProperty(OutputKeys.INDENT, "yes");
    tr.setOutputProperty(OutputKeys.METHOD, "xml");
    tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

    tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(path)));
  }
}
