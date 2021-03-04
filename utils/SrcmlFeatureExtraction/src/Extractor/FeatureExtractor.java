package Extractor;

import Entity.Features;
import Entity.Kelas;
import Util.ClassUtility;
import Util.XPathProvider;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * provide all of the methods to extract feature from xml node of each class
 * Created by arifn on 8/2/2017.
 */
public class FeatureExtractor {

    /**
     * unit: xml representation of a class
     * features: hold features information for a class (unit)
     */

    private Node unit;
    private int type;
    private XPath xPath;
    private Features features;
    private boolean DEBUG = false;

    public FeatureExtractor(Node unit) {
        this.unit = unit;
        xPath = XPathProvider.getInstance();
        features = new Features(unit);
        getType();

    }

    /**
     * method that do the extraction, consists of every feature extraction method
     * @return features for the class
     */
    public Features extract() {

        try {
            extractClassName();
            extractLOC();
            extractAttribute();
            extractMethods();
            extractImports();
            extractClassSpecifier();
            extractIfs();
            extractMethodParameter();
            extractPersistence();
            extractCollection();
            extractOutboundInvocation();
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }

        return features;
    }

    /**
     * extract outbound method invocation
     * e.g. test.test()
     */
    private void extractOutboundInvocation() {

        Pattern pattern = Pattern.compile("(\\s)*(.+)\\.(.+)\\(\\)");
        Matcher match = pattern.matcher(unit.getTextContent());

        int count = 0;
        while(match.find()){
            count++;
        }

        features.setNumOutboundInvocation(count);

        if(DEBUG){
            System.out.println("Method invocation: "+features.getNumOutboundInvocation());
        }
    }

    /**
     * check whether the class extending from Java Collection classes
     * @throws XPathExpressionException throwing exception for XPath
     */
    private void extractCollection() throws XPathExpressionException{

        String expression = "srcml:class/srcml:super/srcml:extends/srcml:name/srcml:name | " +
                "srcml:interface/srcml:super/srcml:extends/srcml:name/srcml:name | " +
                "srcml:enum/srcml:super/srcml:extends/srcml:name/srcml:name";

        NodeList imports = (NodeList) xPath.evaluate(expression, unit, XPathConstants.NODESET);

        Pattern collectionFW = Pattern.compile(ClassUtility.Collections);
        Matcher m;

        for(int i=0;i<imports.getLength();i++){
            m = collectionFW.matcher(imports.item(i).getTextContent());

            if(m.find()){
                features.setCollection(true);
                break;
            }
        }

        if(DEBUG){
            System.out.println("Collection: "+features.isCollection());
        }

    }

    /**
     * check if there is a persistence feature in the class:
     * (1) implements Serializable interface
     * (2) importing or have SQL statement in the source code
     * @throws XPathExpressionException
     */
    private void extractPersistence()  throws  XPathExpressionException{

        // check implements Serializable
        String expression = "srcml:class/srcml:super/srcml:implements/srcml:nam | " +
                "srcml:interface/srcml:super/srcml:implements/srcml:name | " +
                "srcml:enum/srcml:super/srcml:implements/srcml:name";

        NodeList serials = (NodeList) xPath.evaluate(expression, unit, XPathConstants.NODESET);

        for(int i=0;i<serials.getLength();i++){
            if(serials.item(i).getTextContent().equals("Serializable")){
                features.setPersist(true);
                break;
            }
        }

        expression = "srcml:import/srcml:name/srcml:name";

        NodeList sqls = (NodeList) xPath.evaluate(expression, unit, XPathConstants.NODESET);

        for(int i=0;i<sqls.getLength();i++){
            if(sqls.item(i).getTextContent().toLowerCase().contains("sql")){
                features.setPersist(true);
                break;
            }
        }

        if(DEBUG){
            System.out.println("Persistence: "+features.isPersist());
        }
    }

    /**
     * extract the number of decision making statement in the class
     */
    private void extractIfs(){

        int ifCounter = 0;

        String ifPattern = "(\\s)*if(\\s)*\\(";
        String elseIfPattern = "(\\s)*else if(\\s)*\\(";

//        checking if
        Pattern p = Pattern.compile(ifPattern);
        Matcher m = p.matcher(unit.getTextContent());

        while (m.find()) {
            ifCounter += 1;
        }

//        checking else if
//        else if is located at the same block as if
        p = Pattern.compile(elseIfPattern);
        m = p.matcher(unit.getTextContent());

        while (m.find()) {
            ifCounter -= 1;
        }


//        checking switch statement
        p = Pattern.compile("switch(\\s)*\\(");
        m = p.matcher(unit.getTextContent());

        while (m.find()) {
            ifCounter += 1;
        }

        features.setNumIfs(ifCounter);

        if(DEBUG){
            System.out.println("Decision: "+features.getNumIfs());
        }

    }

    /**
     * get the specifier of the class
     * @throws XPathExpressionException
     */
    private void extractClassSpecifier() throws XPathExpressionException {
        String expression = "srcml:class/srcml:specifier | srcml:interface/srcml:specifier | " +
                "srcml:enum/srcml:specifier";

        NodeList specifiers = (NodeList) xPath.evaluate(expression, unit, XPathConstants.NODESET);

        for(int i=0;i<specifiers.getLength();i++) {
            String specifier = specifiers.item(i).getTextContent();

            switch (specifier) {
                case "abstract":
                    features.setAbtract(true);
                    break;
                case "static":
                    features.setStaticClass(true);
                    break;
                case "public":
                case "private":
                case "protected":
                    features.setClassPublicity(specifier);
                    break;
                default:
                    System.err.println("Specifier not detected: " + specifier);
                    break;
            }
        }

        if(DEBUG){
            System.out.println("Specifier AM,Abstract,Static: "+features.getClassPublicity()+" "+
            features.isAbtract()+" "+features.isStaticClass());
        }
    }

    /**
     * get the number of import statements in the class
     * @throws XPathExpressionException throwing exception for XPath
     */
    private void extractImports() throws XPathExpressionException {

        NodeList imports = (NodeList) xPath.evaluate("srcml:import", unit, XPathConstants.NODESET);
        features.setNumImports(imports.getLength());

        if(DEBUG){
            System.out.println("Number of import statements: "+features.getNumImports());
        }

    }


    /**
     * extract the number of attribute declaration on a class
     * @throws XPathExpressionException
     */
    private void extractAttribute() throws XPathExpressionException {

        NodeList declarations = (NodeList) xPath.evaluate("srcml:enum/srcml:block/srcml:decl | " +
                        "srcml:class/srcml:block/srcml:decl | srcml:interface/srcml:block/srcml:decl", unit,
                XPathConstants.NODESET);
        NodeList declarationStatements = (NodeList) xPath.evaluate("srcml:enum/srcml:block/srcml:decl_stmt | " +
                        "srcml:class/srcml:block/srcml:decl_stmt | srcml:interface/srcml:block/srcml:decl_stmt",
                unit, XPathConstants.NODESET);

        features.setNumAttr(declarations.getLength() + declarationStatements.getLength());

        if(DEBUG){
            System.out.println("Attribute: "+features.getNumAttr());
        }
    }

    /**
     * extract the number of line of code
     */
    private void extractLOC() {
        String[] counter = unit.getTextContent().split("\n");
        features.setLoc(counter.length);

        if(DEBUG){
            System.out.println("LOC: "+counter.length);
        }
    }


    /**
     * extracting classname from the class node
     * after that, extract the number of words in the class name, check whether the name of the class is ended with
     * Controller and "or" or "er"
     */
    public void extractClassName() {
        String fullpathname = unit.getAttributes().getNamedItem("filename").toString();
        features.setFullClassName(ClassUtility.formatFullPathName(fullpathname));
        String filename = ClassUtility.getFileName(fullpathname);

        assert filename != null;
        String[] tokens = filename.split("\\.");

//            get the class name by removing ".java"
        String classname = tokens[tokens.length-2];

//            if the token is greater than 2, then it is an inner class
        if (tokens.length > 2) {
            features.setInnerClass(true);

        } else {
            features.setInnerClass(false);
        }

        features.setClassname(classname);

        if(DEBUG) {
            System.out.println("Filename: "+fullpathname);
            System.out.println("Classname: "+classname);
            System.out.println("Innerclass: "+features.isInnerClass());
        }

        extractNumWordsName(classname);
        extractController(classname);
        extractOrEr(classname);

    }


    /**
     * determine whether the class is ended with "OR" or "ER"
     * @param classname the name of the class
     */
    private void extractOrEr(String classname) {
        boolean isOrEr = classname.toLowerCase().endsWith("er") || classname.toLowerCase().endsWith("or");
        features.setOrEr(isOrEr);

        if(DEBUG){
            System.out.println("Is end with ER or OR: "+isOrEr);
        }
    }


    /**
     * determine whether the class is ended with "controller"
     * @param classname the name of the class
     */
    private void extractController(String classname) {
        boolean isController = classname.toLowerCase().endsWith("controller");
        features.setController(isController);

        if(DEBUG){
            System.out.println("Is Controller: "+isController);
        }

    }

    /**
     * extract the number of words in the class name
     * @param classname the name of the class
     */
    private void extractNumWordsName(String classname) {
        String[] words = classname.split("(?=\\p{Upper})");
        features.setNumWordName(words.length);

        if(DEBUG){
            System.out.println("The number of words: "+words.length);
        }

    }

    private void getType() {
        try {

            NodeList isInterface = (NodeList) xPath.evaluate("srcml:interface", unit, XPathConstants.NODESET);
            NodeList isClass = (NodeList) xPath.evaluate("srcml:class", unit, XPathConstants.NODESET);
            NodeList isEnum = (NodeList) xPath.evaluate("srcml:enum", unit, XPathConstants.NODESET);

            if (isInterface.getLength() > 0) {
                this.type = Kelas.INTERFACE;
                features.setInterface(true);
            } else if (isClass.getLength() > 0) {
                this.type = Kelas.CLASS;
                features.setClass(true);
            } else if (isEnum.getLength() > 0) {
                this.type = Kelas.ENUM;
                features.setEnum(true);
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /**
     * first, get the list of methods from a class, store the number of method declarations
     * then, count the number of methods based on their access modifier
     * then, count the number of setters and getters method
     * @throws XPathExpressionException
     */
    private void extractMethods() throws XPathExpressionException {

        String expression = "srcml:class/srcml:block/srcml:function | srcml:interface/srcml:block/srcml:function | srcml:enum/srcml:block/srcml:function";

        NodeList listMethods = (NodeList) xPath.evaluate(expression, unit, XPathConstants.NODESET);
        features.setNumMethod(listMethods.getLength());

        if(DEBUG){
            System.out.println("Number of methods: "+features.getNumMethod());
        }

        countMethodPublicity(listMethods);
        countSetterGetterMethod(listMethods);

    }

    /**
     * extract the number of parameters in all of the methods on a class
     * @throws XPathExpressionException
     */
    private void extractMethodParameter() throws XPathExpressionException {
        String expression;
        expression = "srcml:class/srcml:block/srcml:function/srcml:parameter_list/srcml:parameter | " +
                "srcml:interface/srcml:block/srcml:function/srcml:parameter_list/srcml:parameter | " +
                "srcml:enum/srcml:block/srcml:function/srcml:parameter_list/srcml:parameter";

        NodeList listParams = (NodeList) xPath.evaluate(expression, unit, XPathConstants.NODESET);
        features.setNumParameters(listParams.getLength());

        if(DEBUG){
            System.out.println("Number of Parameters: "+features.getNumParameters());
        }
    }

    /**
     * calculate the number of methods based on their access modifier
     * @param listMethods list of methods on a class
     * @throws XPathExpressionException
     */
    private void countMethodPublicity(NodeList listMethods) throws XPathExpressionException {
        int publicMethodCounter = 0;
        int privateMethodCounter = 0;
        int protectedMethodCounter = 0;

        String expression = "srcml:specifier";

        for (int i = 0; i < listMethods.getLength(); i++) {
            Node method = listMethods.item(i);

            NodeList specifiers = (NodeList) xPath.evaluate(expression, method, XPathConstants.NODESET);
            if (specifiers.getLength() > 0) {
                String specifier = specifiers.item(0).getTextContent();

                if (specifier.equals("public")) {
                    publicMethodCounter += 1;
                }
                if (specifier.equals("private")) {
                    privateMethodCounter += 1;
                }
                if (specifier.equals("protected")) {
                    protectedMethodCounter += 1;
                }
            }
        }

        features.setNumPrivateMethods(privateMethodCounter);
        features.setNumPublicMethods(publicMethodCounter);
        features.setNumProtectedMethods(protectedMethodCounter);

        if(DEBUG){
            System.out.println("Number of Private, Public, Protected method: "+features.getNumPrivateMethods()+","+
            features.getNumPublicMethods()+","+features.getNumProtectedMethods());
        }

    }

    /**
     * count the number of setter and getter method
     * @param listMethods list of method on a class
     * @throws XPathExpressionException
     */
    private void countSetterGetterMethod(NodeList listMethods) throws XPathExpressionException {

        int setterCounter = 0;
        int getterCounter = 0;

        for (int i = 0; i < listMethods.getLength(); i++) {
            Node method = listMethods.item(i);

            String methodName = getMethodName(method);
            if (isSetter(methodName)) setterCounter += 1;
            if (isGetter(methodName)) getterCounter += 1;
        }

        features.setSetters(setterCounter);
        features.setGetters(getterCounter);

        if(DEBUG){
            System.out.println("Number of Setter,Getter: "+features.getSetters()+","+features.getGetters());
        }

    }

    private String getMethodName(Node method) throws XPathExpressionException {

        String methodName = "";
        NodeList names = (NodeList) xPath.evaluate("srcml:name", method, XPathConstants.NODESET);

        if (names.getLength() > 0) {
            methodName = names.item(0).getTextContent();
        }
        return methodName;
    }

    private boolean isSetter(String methodName) {
        return methodName.toLowerCase().startsWith("set");
    }

    private boolean isGetter(String methodName) {
        return methodName.toLowerCase().startsWith("get");
    }

    public Features getFeatures() {
        return features;
    }

    public void extractFullClassName(){
        String filename = unit.getAttributes().getNamedItem("filename").toString();
        String update = filename.replaceAll("filename=", "");
        update = update.replaceAll("\"", "");
        update = update.replaceAll("lib\\\\", "");
        update = update.replaceAll("mail\\\\","");
        update = update.replaceAll(".java", "");

        features.setFullClassName(update);
//        String[] tokens = update.split("\\.");
//        System.out.println(update);
    }
}