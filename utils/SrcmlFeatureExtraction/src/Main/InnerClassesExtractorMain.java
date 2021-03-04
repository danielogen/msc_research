package Main;

import Entity.Kelas;
import Util.ClassUtility;
import Util.ClasstoFileExporter;
import Util.InnnerClassExtractor;
import Util.XPathProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;

/**
 * Created by arifn on 7/25/2017.
 */
public class InnerClassesExtractorMain {

    private static String xmlpath = "data\\input\\new_version.xml";

    public static void main(String[] args) {

        if(args.length == 1){
            xmlpath = args[0];
        } else {
            System.err.println("Input path is not set, using default XML Path: "+xmlpath);
        }

        Document doc = ClassUtility.getDocument(xmlpath);
        XPath xPath = XPathProvider.getInstance();

        try {
            NodeList units = (NodeList) xPath.evaluate("srcml:unit/srcml:unit", doc, XPathConstants.NODESET);
            InnnerClassExtractor innnerClassExtractor = new InnnerClassExtractor();

//        FOR TESTING PURPOSE:
//            System.out.println(units.item(117).getTextContent());
//            innnerClassExtractor.processUnit(units.item(117));
//            innnerClassExtractor.printClasses();

//            THE REAL DEAL
            for (int i = 0; i < units.getLength(); i++) {
                Node node = units.item(i);
                innnerClassExtractor.processUnit(node);
            }


//            export into file
            ClasstoFileExporter.export(innnerClassExtractor.getClasses());

        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }

    }
}
