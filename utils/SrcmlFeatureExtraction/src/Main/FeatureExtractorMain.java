package Main;

import Entity.Features;
import Extractor.FeatureExtractor;
import Util.ClassUtility;
import Util.SimpleDataFrame;
import Util.XPathProvider;
import org.w3c.dom.*;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;

/**
 * Extracting features from the source code
 * Created by arifn on 8/2/2017.
 */
public class FeatureExtractorMain {

    private static String xmlpath = "srcML-timber-v1.8.xml";

    public static void main(String[] args){

        if(args.length == 1){
            xmlpath = args[0];
        } else {
            System.err.println("XML path is not set, using default: " + xmlpath);
        }


//        get the document
        Document doc = ClassUtility.getDocument(xmlpath);

//        get xpath object
        XPath xPath = XPathProvider.getInstance();

        try {

//            get list of classes
            NodeList units = (NodeList) xPath.evaluate("srcml:unit/srcml:unit", doc, XPathConstants.NODESET);


            ArrayList<Features> features = new ArrayList<Features>();
//            System.out.println("Classname, LOC, numAttr, Type, InnerClass, NumMethod, NumPublicMethod, NumPrivateMethod, NumProtectedMethod, Setters, Getters, NumWordName, isController, isOrEr, NumImports, ClassPublicity, isStaticClass, NumIfs, NumParameters, IsPersist, IsCollection, isAbstractClass, NumOutboundInvocation");

//            for testing
//            Node unit = units.item(37);
//            FeatureExtractor featureExtractor = new FeatureExtractor(unit);
//            Features feature = featureExtractor.extract();
//            System.out.println(feature.csvString());
//            System.out.println(feature.getFullClassName());

//            for REAL
            for(int i=0;i<units.getLength();i++){
                System.out.print("Processing "+i+": ");
                System.out.println(ClassUtility.getClassName(units.item(i)));
                FeatureExtractor featureExtractor = new FeatureExtractor(units.item(i));
                Features feature = featureExtractor.extract();
                features.add(feature);
            }

//            writing to csv
            SimpleDataFrame dataFrame = new SimpleDataFrame(
                "Fullpathname", "Classname", "loc", "numAttr", "numMethod", "setters",
                "getters", "isPersist", "isCollection", "numWordName", "isOrEr",
                "isController", "numIfs", "numParameters", "numImports",
                "numOutboundInvocation", "numInnerClasses", "isStaticClass",
                "isInterface", "isInnerClass", "isClass", "isEnum",
                "classPublicity", "numPublicMethods", "numPrivateMethods",
                "numProtectedMethods", "isAbstract"
            );

            for(Features feature: features){
                dataFrame.append(feature.csvString());
            }
//
            dataFrame.writeToCSV("timber-features.csv");
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}