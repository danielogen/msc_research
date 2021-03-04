package Main;

import Entity.ClassInfo;
import Entity.Features;
import Extractor.FeatureExtractor;
import Util.ClassUtility;
import Util.XPathProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by arifn on 8/22/2017.
 */
public class MatrixMain {

    private static HashMap<String, ClassInfo> classes = new HashMap<String, ClassInfo>();
    static int[][] matrix;
//    static ArrayList<String>  names;
    static String[] names;

    public static void main(String[] args){


        loadCSV();
        readDocList();

//        printArray();
//        printIndex();
//        check();
    }


    private static void readDocList() {

        Set<String> keys = classes.keySet();
        int i = 0;

//        for(String key: keys){
        String key = "k9mail-library.main.java.com.fsck.k9.mailstore.BinaryMemoryBody";
        ClassInfo kelas = classes.get(key);
        String xmlPath = "";
        String expression = "";
        if(kelas.getLibrary().equals("k9mail")){
            xmlPath = "data\\k9mail-clean.xml";
            expression = "srcml:unit/srcml:unit[srcml:package/srcml:name = '"+ transformToPackageName(kelas.getJavapathdot()) +"']";
        } else{
            xmlPath = "data\\k9mail-library-clean.xml";
            expression = "srcml:unit/srcml:unit[srcml:package/srcml:name ='" + transformToPackageName(kelas.getJavapathdot())+"']";
        }

        Document doc = ClassUtility.getDocument(xmlPath);
        XPath xPath = XPathProvider.getInstance();
        System.out.println(expression);
        try {

            NodeList units = (NodeList) xPath.evaluate(expression, doc, XPathConstants.NODESET);

            if(units.getLength() != 1){
                System.out.println("Error detected: Account, "+units.getLength()+", "+expression);
//                    break;
            }

            System.out.println("("+i+") Processing "+key);
//            for(int j=0;j<units.getLength();j++){
//                updatePath(units.item(0),key);
//                checkImport(units.item(0), key);
//                checkExtends(units.item(0), key);
//                checkImplements(units.item(0),key);
//            }

//            names.add(kelas.getNo(), kelas.getFullname());
            i += 1;

        }catch (XPathExpressionException e){
            e.printStackTrace();
        }
//        }
    }

    private static String transformToPackageName(String path){
        String[] tokens = path.split("\\.");
        String packageName = "";
        for(int i=0;i<tokens.length;i++){
            String token = tokens[i];
            if(token.equals("k9mail") || token.equals("k9mail-library") || token.equals("main") || token.equals("java")){
                continue;
            }
            else if(i == (tokens.length-1)){
                packageName = packageName.concat(token);
            }
            else{
                packageName = packageName.concat(token).concat(".");
            }
        }
        return packageName;
    }

//    private static void updatePath(Node unit, String key) throws  XPathExpressionException{
//
//        String importExpression = "srcml:package/srcml:name";
//        XPath xPath = XPathProvider.getInstance();
//
//        NodeList nodeList = (NodeList) xPath.evaluate(importExpression, unit, XPathConstants.NODESET);
//
//        if(nodeList.getLength() == 1){
//            String path = nodeList.item(0).getTextContent()+"."+classes.get(key).getFullname();
//             classes.get(key).setFullPackageName(path);
//        }
//
//    }
//
//
//    private static void printIndex() {
//        for(int i=0;i<names.length;i++){
//            System.out.println(i+","+names[i]+","+classes.get(names[i]).getFullPackageName());
//        }
//    }

    private static void checkImport(Node unit, String className) throws XPathExpressionException{

        String importExpression = "srcml:import/srcml:name";
        XPath xPath = XPathProvider.getInstance();

        NodeList nodeList = (NodeList) xPath.evaluate(importExpression, unit, XPathConstants.NODESET);

        if(nodeList.getLength() > 0) {

            //for all node in nodelist
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node importNode = nodeList.item(i);
                String search = classes.get(className).getLibrary().concat("/").concat(baseFromPath(importNode.getTextContent()));
//                System.out.println(search);
                // get the position of the iterated class
                int positionIterate = classes.get(className).getNo();

                if (classes.containsKey(search)) {
                    // get the position of found library
                    int position = classes.get(search).getNo();
//                    System.out.println("Update index:" + positionIterate + "," + position);
                    //update the matrix
                    matrix[positionIterate][position] = 1;
                }
            }

        }

    }

    private static void checkExtends(Node unit, String className) throws XPathExpressionException{

        String importExpression = "srcml:class/srcml:super/srcml:extends/srcml:name";
        XPath xPath = XPathProvider.getInstance();

        NodeList nodeList = (NodeList) xPath.evaluate(importExpression, unit, XPathConstants.NODESET);

        if(nodeList.getLength() > 0){
            Node nodeExtends = nodeList.item(0);
            String search = classes.get(className).getLibrary().concat("/").concat(nodeExtends.getTextContent());
//            System.out.println(search);
            // get the position of the iterated class
            int positionIterate = classes.get(className).getNo();

            if (classes.containsKey(search)) {
                // get the position of found library
                int position = classes.get(search).getNo();
//                System.out.println("Update index:" + positionIterate + "," + position);
                //update the matrix
                matrix[positionIterate][position] = 2;
            }

        }

    }

    private static void checkImplements(Node unit, String className) throws XPathExpressionException {

        String importExpression = "srcml:class/srcml:super/srcml:implements/srcml:name";
        XPath xPath = XPathProvider.getInstance();

        NodeList nodeList = (NodeList) xPath.evaluate(importExpression, unit, XPathConstants.NODESET);

        if(nodeList.getLength() > 0){
            for(int i=0;i<nodeList.getLength();i++){

                Node nodeImplements = nodeList.item(i);
                String search = classes.get(className).getLibrary().concat("/").concat(nodeImplements.getTextContent());
//                System.out.println(search);
                // get the position of the iterated class
                int positionIterate = classes.get(className).getNo();

                if (classes.containsKey(search)) {
                    // get the position of found library
                    int position = classes.get(search).getNo();
//                    System.out.println("Update index:" + positionIterate + "," + position);
                    //update the matrix
                    matrix[positionIterate][position] = 3;
                }

            }
        }


    }

    private void generateName(){
        //        String xmlpath = "data\\k9mail-library-noinner.xml";
        String xmlpath = "data\\k9mail-noinner.xml";

        Document doc = ClassUtility.getDocument(xmlpath);

        XPath xPath = XPathProvider.getInstance();

        try {
            NodeList units = (NodeList) xPath.evaluate("srcml:unit/srcml:unit", doc, XPathConstants.NODESET);

//            ClassInfo unit = units.item(7);
//            ClassInfo unit = units.item(26);


            for(int i=0;i<units.getLength();i++){
//                System.out.print("Processing "+i+": ");
                FeatureExtractor featureExtractor = new FeatureExtractor(units.item(i));
//                FeatureExtractor featureExtractor = new FeatureExtractor(unit);
                featureExtractor.extractClassName();
                featureExtractor.extractFullClassName();
                Features feature = featureExtractor.getFeatures();

                System.out.println(feature.getFullClassName()+", "+feature.getClassname());
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private static void loadCSV(){

        String csvFile = "data/labeled-data-set.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        names = new String[538];

        try {
            int i = 0;

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
//                System.out.println(data[0]+","+data[1]+","+data[2]+","+data[3]);
                ClassInfo classInfo = new ClassInfo();
                classInfo.setNo(i);
                String library = data[0].replaceAll("\\P{Print}", "");
                classInfo.setLibrary(library);
                classInfo.setJavapathslash(data[6]);
                classInfo.setJavapathdot(data[7]);
                classInfo.setRole(data[9]);
                classes.put(classInfo.getJavapathdot(), classInfo);
//                System.out.println(classInfo);
                names[classInfo.getNo()] = classInfo.getJavapathdot();
                i += 1;


//                if(i == 1) break;
            }

            matrix = new int[classes.size()][classes.size()];
//            System.out.println("Matrix size: "+matrix.length);
//            System.out.println("Classes size: "+classes.size());
//            System.out.println("Name size: "+names.length);
            System.out.println("Load CSV Completed");
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printArray(){

        for(int i=0;i<matrix.length;i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]);
                if(j != matrix[i].length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println();
        }
    }

//    private static void check(){
//        for(int i=0;i<names.length;i++){
//            System.out.print(names[i]+" ");
//            System.out.print(classes.get(names[i]).getNo());
//            if(! names[i].equals(classes.get(names[i]).getPathFullName())){
//                System.out.println("Masalah: ("+i+")"+names[i]+" vs "+classes.get(names[i]).getFullname());
//            } else {
//                System.out.println(" ok");
//            }
//        }
//
//    }

    private static String baseFromPath(String javaPath){
        String[] tokens = javaPath.split("\\.");

        if(Character.isUpperCase(tokens[tokens.length-2].charAt(0))){
            return tokens[tokens.length-2]+"."+tokens[tokens.length-1];
        }
        else{
            return tokens[tokens.length-1];
        }
    }
}
