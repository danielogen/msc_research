package Extractor;

import Main.ListClass;
import Util.XPathProvider;
import joinery.DataFrame;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Arrays;

public class Importer {

    private NodeList units;
    private DataFrame<Object> dataFrame = new DataFrame<Object>("no", "filename","classname", "import", "packagepath");
    private int numbering = 0;

    public Importer(NodeList units){
        this.units = units;
        process();
    }

    private void process(){
        for(int i = 0; i< units.getLength();i++){

            String filename = parseFilename(units.item(i).getAttributes().getNamedItem("filename").toString());

            XPath xPath = XPathProvider.getInstance();
            try{
                NodeList classes = (NodeList) xPath.evaluate("srcml:enum/srcml:name | srcml:class/srcml:name | srcml:interface/srcml:name", units.item(i), XPathConstants.NODESET);

                String classname = classes.item(0).getTextContent();

//                System.out.print("Processing "+(i+1)+"("+classname+")");

                extract(units.item(i), filename, classname);
            }
            catch (XPathExpressionException e){
                e.printStackTrace();
            }
        }
    }

    private void extract(Node unit, String filename, String classname) throws XPathExpressionException{

        XPath xPath = XPathProvider.getInstance();

            NodeList units = (NodeList) xPath.evaluate("srcml:import", unit, XPathConstants.NODESET);
            String packagePath = ListClass.getpackagePath(filename);
            if(units.getLength() > 0){
//                System.out.println(units.item(0).getTextContent());

                for(int i=0;i<units.getLength();i++){
                    String importName = parseImportName(units.item(i).getTextContent());
                    dataFrame.append(++numbering, Arrays.asList(numbering,filename,classname, importName, packagePath));

                }
            }
            else{
                System.out.println(filename+","+classname+","+"Not found");
            }


    }

    public static String parseFilename(String filename){
        String filenamePattern = "^filename=\"";
        String doubleQuotePattern = "\"";
        return filename.replaceAll(filenamePattern,"").replaceAll(doubleQuotePattern, "");
    }

    private String parseImportName(String importname){
        String importnamePattern = "^import\\s";
        String semicolonPattern = ";";
        return importname.replaceAll(importnamePattern, "").replaceAll(semicolonPattern, "");
    }

    public void toCSV(String filepath){
        try {
            dataFrame.writeCsv(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
