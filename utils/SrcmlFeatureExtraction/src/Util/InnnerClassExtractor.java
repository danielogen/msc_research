package Util;

import Entity.Kelas;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.ArrayList;

/**
 * Provides implementation of extracting inner classes
 * Created by arifn on 7/31/2017.
 */
public class InnnerClassExtractor {

    private ArrayList<Kelas> classes = new ArrayList<>();
    private XPath xPath = XPathProvider.getInstance();


    /*
    Method to determine the type of the parent class
    Whether it is Class, Interface, or Enum
    @param  Node node : represents all classes in the project
     */
    public void processUnit(Node node) {
        Kelas parent = buildParent(node);
        parent.setNode(node);
        classes.add(parent);

        System.out.println(parent.getClassname());

        try {
            // check whether the parent class is class, interface, or enum type
            NodeList classNodes = (NodeList) xPath.evaluate("srcml:class", node, XPathConstants.NODESET);
            NodeList interfaceNodes = (NodeList) xPath.evaluate("srcml:interface", node, XPathConstants.NODESET);
            NodeList enumNodes = (NodeList) xPath.evaluate("srcml:enum", node, XPathConstants.NODESET);

            if(classNodes.getLength() > 0){
                parent.setType(Kelas.CLASS);
                processParentClass(classNodes.item(0), parent);
            }else if(interfaceNodes.getLength() > 0){
                parent.setType(Kelas.INTERFACE);
                processParentClass(interfaceNodes.item(0), parent);
            } else if(enumNodes.getLength() > 0){
                parent.setType(Kelas.ENUM);
                processParentClass(enumNodes.item(0), parent);
            } else{
//                to catch undefined class type yet
                System.err.println(parent.getClassname()+" type is not detected");
            }


        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

    }


    /**
     * Creating the parent class
     * @param node node represent the parent class
     * @param parent information holder for class
     */
    private void processParentClass(Node node, Kelas parent) {

        String content = node.getTextContent();

//        Prefix is the import statement until class declaration
        String prefix = ClassUtility.substring(parent.getTextContent(), content);
        parent.setPrefix(prefix);
        parent.setContent(content);

        try {
//            Check inner class type, whether class, interface, or enum
            NodeList inClasses = (NodeList) xPath.evaluate("srcml:block/srcml:class", node, XPathConstants.NODESET);
            NodeList inInterfaces = (NodeList) xPath.evaluate("srcml:block/srcml:interface", node, XPathConstants.NODESET);
            NodeList inEnums = (NodeList) xPath.evaluate("srcml:block/srcml:enum", node, XPathConstants.NODESET);


//            process inner classes
            for (int i = 0; i < inClasses.getLength(); i++) {
                buildInner(parent, inClasses.item(i), Kelas.CLASS);
            }

//            process inner interfaces
            for (int i = 0; i < inInterfaces.getLength(); i++) {
                buildInner(parent, inInterfaces.item(i), Kelas.INTERFACE);
            }

            for (int i = 0; i < inEnums.getLength(); i++) {
                buildInner(parent, inEnums.item(i), Kelas.ENUM);
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to create new Kelas class to represent information of a class
     * @param node represent the class
     * @return Kelas class represent the class being processed
     */
    private Kelas buildParent(Node node) {
        String filenameAttribute = node.getAttributes().getNamedItem("filename").toString();
        String filename = ClassUtility.getFileName(filenameAttribute);
        Kelas parent = new Kelas(filenameAttribute, node.getTextContent());
        String[] tokens = filename.split("\\.");
//            get the class name by removing ".java"
        String classname = tokens[tokens.length-2];
        parent.setClassname(classname);
        return parent;
    }


    public ArrayList<Kelas> getClasses() {
        return classes;
    }

    public void printClasses() {

        for (int a = 0; a < classes.size(); a++) {
            System.out.println("==============================index: " + a);
            System.out.println(classes.get(a));
        }
    }


    /**
     * Method to build inner class as new Kelas class to store information about the inner class
     * @param parent represent parent in Kelas class
     * @param inner node represent inner class
     * @param type type of the inner class
     * @throws XPathExpressionException if the XPath expression is not valid
     */
    private void buildInner(Kelas parent, Node inner, int type) throws XPathExpressionException {

        parent.cleanInner(inner.getTextContent());

        Kelas child = new Kelas(parent.getFullpath(), inner.getTextContent());
        child.setPrefix(parent.getPrefix());
        child.setType(type);

        NodeList childNodes = (NodeList) xPath.evaluate("srcml:name", inner, XPathConstants.NODESET);
        String childName = childNodes.item(0).getTextContent();
        childName = parent.getClassname().concat(".").concat(childName);
        child.setClassname(childName);
        child.setNode(inner);
        classes.add(child);

    }
}