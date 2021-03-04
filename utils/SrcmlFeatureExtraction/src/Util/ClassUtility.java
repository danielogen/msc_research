package Util;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by arifn on 7/13/2017.
 */
public class ClassUtility {

    public static String substring(String full, String subs){
        String result = full.replace(subs,"");
        return result.trim();
    }

    public static Document getDocument(String xmlpath) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setNamespaceAware(true);
            docBuilderFactory.setIgnoringElementContentWhitespace(true);
            docBuilderFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = null;

            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(xmlpath));
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;
    }

//    private static InputStream readFile(String path){
//        try{
//            Path filepath = Paths.get(path);
//            byte[] encoded = Files.readAllBytes(filepath);
//            String xmlContent = new  String(encoded);
////            System.out.println(xmlContent);
//            xmlContent = xmlContent.replaceAll("\\s{3,10}"," ");
////            System.out.println(xmlContent);
//            return new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
//        } catch (IOException ex){
//            ex.printStackTrace();
//        }
//        return null;
//    }

    public static String getClassName(Node node){

        XPath xPath = XPathProvider.getInstance();
        String className = null;

        try {
            NodeList units = (NodeList) xPath.evaluate("srcml:enum/srcml:name | srcml:class/srcml:name | " +
                            "srcml:interface/srcml:name", node,XPathConstants.NODESET);

            if(units.getLength()> 0){
                className = units.item(0).getTextContent();
            }else {
                System.err.println("Problem");
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return className;

    }

    /**
     * Get the file name from a path, special for source code ending with .java. The java extension is included
     * @param fullpath path to the Java file
     * @return
     */
    public static String getFileName(String fullpath) {

//        extracting file name
        Pattern pattern = Pattern.compile("(([A-Za-z0-9]\\.)*[A-Za-z0-9])+\\.java");
        Matcher matcher = pattern.matcher(fullpath);

        return getMatch(matcher);
    }

    public static String getPath(String fullpath){
        //        extracting file name
        Pattern pattern = Pattern.compile("(([A-Za-z0-9\\-\\_])+\\\\)+");
        Matcher matcher = pattern.matcher(fullpath);

        return getMatch(matcher);
    }

    private static String getMatch(Matcher matcher) {
        if (matcher.find()) {
            String filename = matcher.group();
            return filename;
        }else{
            System.err.println("Not Found");
            return null;
        }
    }

    public static String formatFullPathName(String fullpathname){
        fullpathname = fullpathname.replaceAll("filename=", "");
        fullpathname = fullpathname.replaceAll("\"", "");
        return fullpathname;
    }

    public static final String Collections = "AbstractCollection|AbstractList|AbstractQueue|AbstractSequentialList|AbstractSet|ArrayBlockingQueue|ArrayDeque|ArrayList|AttributeList|BeanContextServicesSupport|BeanContextSupport|ConcurrentHashMap.KeySetView|ConcurrentLinkedDeque|ConcurrentLinkedQueue|ConcurrentSkipListSet|CopyOnWriteArrayList|CopyOnWriteArraySet|DelayQueue|EnumSet|HashSet|JobStateReasons|LinkedBlockingDeque|LinkedBlockingQueue|LinkedHashSet|LinkedList|LinkedTransferQueue|List|PriorityBlockingQueue|PriorityQueue|RoleList|RoleUnresolvedList|Stack|SynchronousQueue|TreeSet|Vector";
}
