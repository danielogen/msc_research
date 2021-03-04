package Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import Util.ClassUtility;
import Util.SimpleDataFrame;
import Util.XPathProvider;
import javafx.beans.binding.When;
import joinery.DataFrame;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import tablesaw.api.BooleanColumn;

import javax.xml.crypto.Data;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dsdi
 */
public class Tester {

    public static void main(String[] args) {
//        String path = "D:\\Drive\\_Researches and Conferences\\Ongoing\\Classifying Objects\\Works\\code\\testclass\\RecipientAdapter.xml";
//
//        Document doc = ClassUtility.getDocument(path);
//        XPath xPath = XPathProvider.getInstance();
//
//        try {
//            NodeList units = (NodeList) xPath.evaluate("srcml:unit/srcml:class/srcml:specifier", doc, XPathConstants.NODESET);
//
//            System.out.println(units.getLength());
//
//            for(int i = 0;i<units.getLength();i++){
//                ClassInfo node = units.item(i);
//                System.out.println(node.getTextContent());
//            }
//
//        } catch (XPathExpressionException e) {
//            e.printStackTrace();
//        }

//        String source = "private void flush(boolean propagate) throws IOException {\n" +
//                "        int avail = base64.avail();\n" +
//                "        if (avail > 0) {\n" +
//                "            byte[] buf = new byte[avail];\n" +
//                "            int c = base64.readResults(buf, 0, avail);\n" +
//                "            if (c > 0) {\n" +
//                "                out.write(buf, 0, c);\n" +
//                "            }\n" +
//                "        }\n" +
//                "        if (propagate) {\n" +
//                "            out.flush();\n" +
//                "        }\n" +
//                "    }";
//
//        Pattern pattern = Pattern.compile("(\\s)(.+)\\.(.+)");
//        Matcher match = pattern.matcher(source);
//
//        int count = 0;
//        while(match.find()){
//            count++;
//
//        }
//
//        System.out.println(count);

//        String xmlPath = "data\\k9mail-library-noinner.xml";
////        String fullname = "Base64.DecoderException";
////        String expression = "srcml:unit/srcml:unit[@filename=\'lib\\" + fullname + ".java\']";
//        String expression = "srcml:unit/srcml:unit[@filename='lib\\Account.java']";
//        System.out.println(expression);
//
//        Document doc = ClassUtility.getDocument(xmlPath);
//        XPath xPath = XPathProvider.getInstance();
//        try {
//            NodeList units = (NodeList) xPath.evaluate(expression, doc, XPathConstants.NODESET);
//
//            if(units.getLength() > 0){
//                System.out.println("Ketemu");
//                System.out.println(units.item(0).getTextContent());
//            }
//        }catch (XPathExpressionException e){
//            e.printStackTrace();
//        }

//        String javaPath = "com.fsck.k9.mail.Base64.EncoderException";
//
//        String[] tokens = javaPath.split("\\.");
//
//        if(Character.isUpperCase(tokens[tokens.length-2].charAt(0))){
//            System.out.println(tokens[tokens.length-2]+"."+tokens[tokens.length-1]);
//        }
//        else{
//            System.out.println(tokens[tokens.length-1]);
//        }

//        String test = "\uFEFFk9mail/Account";
//        String clean = test.replaceAll("\\P{Print}", "");
//        System.out.println(clean);

//        String fullpath = "filename=\"\\k9mail-library\\com\\fsck\\k9\\mail\\filter\\Base64.java\"";
//        String classname = "filename=\"\\k9mail-library\\com\\fsck\\k9\\mail\\filter\\Base64.DecoderException";
//        String word = "\\k9mail\\com\\fsck\\k9\\provider\\MessageProvider.FieldExtractor<T, K>.java";
//        String writingpath = "";
//
//        if(classname.concat(".java").equalsIgnoreCase(fullpath)){
//            writingpath = fullpath.replace("filename=\"","");
//            writingpath = writingpath.replace("\"","");
//        }
//        else{
//            writingpath = classname.replace("filename=\"","");
//            writingpath = writingpath.concat(".java");
//        }
//
//        System.out.println(writingpath);
//        word = word.replaceAll("<[A-z]([,]\\s*[A-z])*>","");
//        System.out.println(word);
//        word = word.replaceAll("<[A-z](\\s[A-z]*)*>*","");
//        System.out.println(word);

//        DataFrame<Object> df = new DataFrame<>("satu","dua","tiga");
//
//        df.append("0", Arrays.asList("Test1",2,3));
//        df.append("1", Arrays.asList("Test2",5,6));
//        df.append(Arrays.asList("Test3",2,3));
//        System.out.println(df.row(2));
//
//        try {
//            df.writeCsv("data\\test.csv");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        String filename = "filename=\"k9-20171122\\k9mail\\src\\main\\java\\com\\fsck\\k9\\activity\\compose\\RecipientPresenter.java\"";
//
//        String pattern = "^filename=\"";
//        String pattern2 = "\"";
//
//        System.out.println(filename.replaceAll(pattern,"").replaceAll(pattern2,""));

//        String expression = "this.filename().there()";
//        Pattern pattern = Pattern.compile("(\\s)*(.+)\\.(.+)\\(\\)");
//        Matcher match = pattern.matcher(expression);
//
//        int count = 0;
//        while(match.find()){
//            System.out.println("here");
//        }

//        SimpleDataFrame df = new SimpleDataFrame("Test","Satu","Dua");
//        df.append("1","23","no");
//        df.writeToCSV("test.csv");

        String fullpath = "old_version\\k9mail-library\\src\\main\\java\\com\\fsck\\k9\\mail\\filter\\Base64.java";
        System.out.println(ClassUtility.getPath(fullpath));

    }

}
