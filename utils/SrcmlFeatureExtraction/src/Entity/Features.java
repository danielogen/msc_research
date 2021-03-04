package Entity;

import org.w3c.dom.Node;

/**
 * Created by arifn on 8/2/2017.
 * As an information holder for features of a class
 */
public class Features {

    /**
     * classname: name of the class
     * fullClassName: full class name with path
     * node: xml representation of the class
     * loc: number of line on code
     * numAttr: number of attributes (variables) in the class
     * numMethod: number of method in the class, constructor excluded
     * setters: number of setter method in the class
     * getters: number of getter method in the class
     * isPersist: boolean value whether the class implements Seralizable or import database connectivity library
     * isCollection: boolean value whether the class extending Java's Collection interfaces
     * numWordName: number of words in the class name
     * isOrEr: boolean value whether the class name is ended with "or" or "er"
     * isController: boolean value whether the class name is ended with "Controller"
     * numIfs: number of decision making statements in the class (if, switch)
     * numParameters: total number of parameters of every methods in the class
     * numImports: number of import statements in the class
     * numOutboundInvocation: number of method invocation in the class
     * numInnerClasses: number of inner classes
     * isStaticClass: boolean value whether the class is a static class
     * isInterface: boolean value whether it is an interface
     * isInnerClass: boolean value whether the class is an inner class
     * isClass: boolean value whether it is a class
     * isEnum: boolean value whether it is an enum
     * classPublicity: the access modifier of the class (default: public)
     * numPublicMethods: number of public methods in the class
     * numPrivateMethods: number of private methods in the class
     * numProtectedMethods: number of protected methods in the class
     * isAbstract: boolean value whether the class is an abstract class
     */

    private String classname;
    private String fullClassName;
    private Node node;
    private int loc; //done
    private int numAttr; //done
    private int numMethod; //done
    private int setters; //done
    private int getters; //done
    private boolean isPersist = false; //done
    private boolean isCollection = false; //done
    private int numWordName; //done
    private boolean isOrEr; //done
    private boolean isController; //done
    private int numIfs;  //done
    private int numParameters; //done
    private int numImports; //done
    private int numOutboundInvocation;
    private int numInnerClasses;
    private boolean isStaticClass = false; //done
    private boolean isInterface; //done
    private boolean isInnerClass = false; // done
    private boolean isClass; //done
    private boolean isEnum; //done
    private String classPublicity = "default";//done
    private int numPublicMethods; //done
    private int numPrivateMethods; //done
    private int numProtectedMethods; //done
    private boolean isAbtract = false;

    public Features(Node node){
        this.node = node;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getNumAttr() {
        return numAttr;
    }

    public void setNumAttr(int numAttr) {
        this.numAttr = numAttr;
    }

    public int getNumMethod() {
        return numMethod;
    }

    public void setNumMethod(int numMethod) {
        this.numMethod = numMethod;
    }

    public int getSetters() {
        return setters;
    }

    public void setSetters(int setters) {
        this.setters = setters;
    }

    public int getGetters() {
        return getters;
    }

    public void setGetters(int getters) {
        this.getters = getters;
    }

    public boolean isPersist() {
        return isPersist;
    }

    public void setPersist(boolean persist) {
        isPersist = persist;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    public int getNumWordName() {
        return numWordName;
    }

    public void setNumWordName(int numWordName) {
        this.numWordName = numWordName;
    }

    public boolean isOrEr() {
        return isOrEr;
    }

    public void setOrEr(boolean orEr) {
        isOrEr = orEr;
    }

    public boolean isController() {
        return isController;
    }

    public void setController(boolean isController) {
        this.isController = isController;
    }

    public int getNumIfs() {
        return numIfs;
    }

    public void setNumIfs(int numIfs) {
        this.numIfs = numIfs;
    }

    public int getNumParameters() {
        return numParameters;
    }

    public void setNumParameters(int numParameters) {
        this.numParameters = numParameters;
    }

    public int getNumImports() {
        return numImports;
    }

    public void setNumImports(int numImports) {
        this.numImports = numImports;
    }

    public int getNumOutboundInvocation() {
        return numOutboundInvocation;
    }

    public void setNumOutboundInvocation(int numOutboundInvocation) {
        this.numOutboundInvocation = numOutboundInvocation;
    }

    public int getNumInnerClasses() {
        return numInnerClasses;
    }

    public void setNumInnerClasses(int numInnerClasses) {
        this.numInnerClasses = numInnerClasses;
    }

    public void setIsStaticClass(boolean isStaticClass) {
        this.isStaticClass = isStaticClass;
    }

    public String getClassPublicity() {
        return classPublicity;
    }

    public void setClassPublicity(String classPublicity) {
        this.classPublicity = classPublicity;
    }

    public int getNumPublicMethods() {
        return numPublicMethods;
    }

    public void setNumPublicMethods(int numPublicMethods) {
        this.numPublicMethods = numPublicMethods;
    }

    public int getNumPrivateMethods() {
        return numPrivateMethods;
    }

    public void setNumPrivateMethods(int numPrivateMethods) {
        this.numPrivateMethods = numPrivateMethods;
    }

    public int getNumProtectedMethods() {
        return numProtectedMethods;
    }

    public void setNumProtectedMethods(int numProtectedMethods) {
        this.numProtectedMethods = numProtectedMethods;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public boolean isInnerClass() {
        return isInnerClass;
    }

    public void setInnerClass(boolean innerClass) {
        isInnerClass = innerClass;
    }


    public boolean isStaticClass() {
        return isStaticClass;
    }

    public void setStaticClass(boolean staticClass) {
        isStaticClass = staticClass;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
        if(this.isInterface()){
            this.isClass = false;
            this.isEnum = false;
        }
    }

    public boolean isClass() {
        return isClass;
    }

    public void setClass(boolean aClass) {
        isClass = aClass;

        if(this.isClass){
            this.isInterface = false;
            this.isEnum = false;
        }
    }

    public boolean isEnum() {
        return isEnum;
    }

    public void setEnum(boolean anEnum) {
        isEnum = anEnum;

        if(this.isEnum){
            this.isClass = false;
            this.isInterface = false;
        }
    }

    public boolean isAbtract() {
        return isAbtract;
    }

    public void setAbtract(boolean abtract) {
        isAbtract = abtract;
    }

//    @Override
//    public String toString() {
//        String print = "Classname:\t"+classname;
//        print = print.concat("\nLOC:\t"+loc);
//        print = print.concat("\nnumAttr:\t"+numAttr);
//
//        if(isClass){
//            print = print.concat("\nType: Class");
//        }
//        else if(isInterface){
//            print = print.concat("\nType: Interface");
//        }
//        else if(isEnum){
//            print = print.concat("\nType: Enum");
//        }
//        else{
//            System.err.println("Anomaly Detected in type of the class");
//        }
//
//        if(isInnerClass){
//            print = print.concat("\nInnerClass: yes");
//        }
//        print = print.concat("\nNumMethod: "+numMethod);
//        print = print.concat("\nNumPublicMethod: "+numPublicMethods);
//        print = print.concat("\nNumPrivateMethod: "+numPrivateMethods);
//        print = print.concat("\nNumProtectedMethod: "+numProtectedMethods);
//        print = print.concat("\nNumSetters: "+setters);
//        print = print.concat("\nNumGetters: "+getters);
//        print = print.concat("\nNumWordName: "+numWordName);
//        print = print.concat("\nisController: "+isController);
//        print = print.concat("\nisOrEr: "+isOrEr);
//        print = print.concat("\nNumImports: "+numImports);
//        print = print.concat("\nClassPublicity: "+classPublicity);
//        print = print.concat("\nStaticClass: "+isStaticClass);
//        print = print.concat("\nNumIfs: "+numIfs);
//        print = print.concat("\nNumParameters: "+numParameters);
//        print = print.concat("\nPersistence: "+isPersist);
//        print = print.concat("\nCollection: "+isCollection);
//        print = print.concat("\nisAbstract: " +isAbtract);
//        print = print.concat("\nOutInvocation: "+numOutboundInvocation);
//        return print;
//    }


    @Override
    public String toString() {


        String print = ""+classname;
        print = print.concat(", "+loc);
        print = print.concat(", "+numAttr);

        if(isClass){
            print = print.concat(", Class");
        }
        else if(isInterface){
            print = print.concat(", Interface");
        }
        else if(isEnum){
            print = print.concat(", Enum");
        }
        else{
            System.err.println("Anomaly Detected in type of the class");
        }

        print = print.concat(", "+isInnerClass);

        print = print.concat(", "+numMethod);
        print = print.concat(", "+numPublicMethods);
        print = print.concat(", "+numPrivateMethods);
        print = print.concat(", "+numProtectedMethods);
        print = print.concat(", "+setters);
        print = print.concat(", "+getters);
        print = print.concat(", "+numWordName);
        print = print.concat(", "+isController);
        print = print.concat(", "+isOrEr);
        print = print.concat(", "+numImports);
        print = print.concat(", "+classPublicity);
        print = print.concat(", "+isStaticClass);
        print = print.concat(", "+numIfs);
        print = print.concat(", "+numParameters);
        print = print.concat(", "+isPersist);
        print = print.concat(", "+isCollection);
        print = print.concat(", " +isAbtract);
        print = print.concat(", "+numOutboundInvocation);
        return print;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public String csvString(){
        StringBuilder sb = new StringBuilder();
        sb.append(fullClassName).append(",");
        sb.append(classname).append(",");
        sb.append(loc).append(",");
        sb.append(numAttr).append(",");
        sb.append(numMethod).append(",");
        sb.append(setters).append(",");
        sb.append(getters).append(",");
        sb.append(isPersist).append(",");
        sb.append(isCollection).append(",");
        sb.append(numWordName).append(",");
        sb.append(isOrEr).append(",");
        sb.append(isController).append(",");
        sb.append(numIfs).append(",");
        sb.append(numParameters).append(",");
        sb.append(numImports).append(",");
        sb.append(numOutboundInvocation).append(",");
        sb.append(numInnerClasses).append(",");
        sb.append(isStaticClass).append(",");
        sb.append(isInterface).append(",");
        sb.append(isInnerClass).append(",");
        sb.append(isClass).append(",");
        sb.append(isEnum).append(",");
        sb.append(classPublicity).append(",");
        sb.append(numPublicMethods).append(",");
        sb.append(numPrivateMethods).append(",");
        sb.append(numProtectedMethods).append(",");
        sb.append(isAbtract).append(",");

        return sb.toString();
    }


}
