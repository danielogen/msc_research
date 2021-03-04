/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Util.ClassUtility;
import org.w3c.dom.Node;

/**
 *
 * @author arifn
 */
public class Kelas {
    
    private String prefix;  // store package and import information
    private String content; // store class block
    private String fullpath; // store fullpath and path
    private String textContent; // store full source code
    private String classname; // store only the classname, if the class is an innerclass, store with the name of its outer class, eg. OuterClass.InnerClass
    private int type;
    private String typename;
    private Node node; // the node
    private String writingPath;

    public static final int CLASS = 1;
    public static final int INTERFACE = 2;
    public static final int ENUM = 3;

    public Kelas(String fullpath, String textContent){
        this.fullpath = fullpath;
        this.textContent = textContent;
        this.content = textContent; // initialization

    }

    public Kelas(){

    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFullpath() {
        return fullpath;
    }

    public void setFullpath(String fullpath) {
        this.fullpath = fullpath;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    @Override
    public String toString() {

        String output = "Path\t: "+fullpath+"\n";
        output = output.concat("Type\t: "+ typename +"\n");
        output = output.concat("Classname\t: "+ classname +"\n");
        return output;
    }

    /**
     * function to remove inner class line of code in the parent code
     * @param innerClassString the content of inner class
     */
    public void cleanInner(String innerClassString){
        content = ClassUtility.substring(content, innerClassString);
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {

        this.classname = classname;
        writingPath = fullpath.replace("filename=\"","");
        writingPath = writingPath.replace("\"","");
//        parent
        if(!this.classname.concat(".java").equalsIgnoreCase(ClassUtility.getFileName(fullpath))){
            String prefixpath = ClassUtility.getPath(writingPath);
            writingPath = prefixpath.concat(classname).concat(".java");
        }
//        System.out.println(writingPath);

    }

    public int getType() {
        return type;
    }

    public String getTypeName(){
        return typename;
    }

    public void setType(int type) {
        this.type = type;

        switch (type){
            case 1: typename = "Class";
                break;
            case 2: typename = "Interface";
                break;
            case 3: typename = "Enum";
                break;
            default: typename = "unknown";
        }
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getWritingPath() {
        return writingPath;
    }
}
