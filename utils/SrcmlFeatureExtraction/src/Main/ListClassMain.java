package Main;

import Util.ClassUtility;
import Util.XPathProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public class ListClassMain {

    public static void main(String[] args) {

        String xmlpath = "data/input/k9-20171122-optimized.xml";

        Document doc = ClassUtility.getDocument(xmlpath);
        XPath xPath = XPathProvider.getInstance();

        try {

//            get list of classes
            NodeList units = (NodeList) xPath.evaluate("srcml:unit/srcml:unit", doc, XPathConstants.NODESET);

            for(int i=0;i<units.getLength();i++){
                Node unit = units.item(i);
                System.out.print(i+1+",");
                String path = unit.getAttributes().getNamedItem("filename").toString();
                System.out.print(getDirectory(cleanFilename(path))+",");
                System.out.println(ClassUtility.getFileName(path));
            }


        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }
    }

    private static String cleanFilename(String filename){

        filename = filename.replaceAll("filename=","");
        filename = filename.replaceAll("\"","");
        filename = filename.replaceAll("k9-20171122-optimized\\\\","");
        filename = filename.replaceAll("src\\\\","");
        return filename;
    }

    private static String getDirectory(String path){
        String[] paths = path.split("\\\\");
        System.out.print(paths[0]+",");
        path = path.replaceAll(paths[0]+"\\\\","");
        return path;
    }

}
