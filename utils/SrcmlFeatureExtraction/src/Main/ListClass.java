package Main;

import Entity.SimpleClass;
import Extractor.Importer;
import Util.ClassToCsv;
import Util.ClassUtility;
import Util.XPathProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class ListClass {



    public static void main(String[] args) {
        String xmlpath = "data\\input\\k9-20171122.xml";

        Document doc = ClassUtility.getDocument(xmlpath);
        XPath xPath = XPathProvider.getInstance();

        List<SimpleClass> classes = new ArrayList<SimpleClass>();

        try {
            NodeList units = (NodeList) xPath.evaluate("srcml:unit/srcml:unit", doc, XPathConstants.NODESET);

            for (int i = 0; i < units.getLength(); i++) {
                Node node = units.item(i);
                classes.add(getClassName(node));

            }

        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }

        ClassToCsv.SimpleClasstoCSV(classes);
    }

    private static SimpleClass getClassName(Node node) {

        SimpleClass simpleClass = new SimpleClass();

        XPath xPath = XPathProvider.getInstance();
        String fullpath = Importer.parseFilename(node.getAttributes().getNamedItem("filename").toString());
        simpleClass.setFullPath(fullpath);
        simpleClass.setPackagePath(getpackagePath(fullpath));

        try{
            NodeList classNodes = (NodeList) xPath.evaluate("srcml:enum/srcml:name | srcml:class/srcml:name | srcml:interface/srcml:name", node, XPathConstants.NODESET);

            if(classNodes.getLength() > 0){
                Node className = classNodes.item(0);
                simpleClass.setClassName(className.getTextContent());
                simpleClass.setType(className.getParentNode().getNodeName());


            }
            else{
                System.err.println("Anomaly "+fullpath);
                System.err.println(classNodes.getLength());
                for(int j=0;j<classNodes.getLength();j++){
                    System.err.println("- "+classNodes.item(j).getTextContent());
                }
            }

        } catch (XPathExpressionException ex){
            ex.printStackTrace();
        }

        return simpleClass;
    }

    public static String getpackagePath(String filename) {

        String file = filename;
        String separatorPattern = "\\\\";
        String prefixMailPattern = "^k9-20171122.k9mail.src.main.java.";
        String prefixLibraryPattern = "^k9-20171122.k9mail-library.src.main.java.";
        String extensionPattern = ".java$";

        String result = file.replaceAll(separatorPattern, ".");
        result = result.replaceAll(prefixMailPattern,"");
        result = result.replaceAll(prefixLibraryPattern, "");
        result = result.replaceAll(extensionPattern, "");

//        System.out.println(result);
        return result;
    }
}
      