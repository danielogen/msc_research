package Main;

import Extractor.Importer;
import Util.ClassUtility;
import Util.XPathProvider;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public class ImportExtractor {

    public static void main(String[] args) {
        String xmlpath = "data\\input\\k9-20171122.xml";

        Document doc = ClassUtility.getDocument(xmlpath);

        XPath xPath = XPathProvider.getInstance();

        try {
            NodeList units = (NodeList) xPath.evaluate("srcml:unit/srcml:unit", doc, XPathConstants.NODESET);
            Importer iextractor = new Importer(units);
            iextractor.toCSV("data\\output\\imported.csv");

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

    }
}
