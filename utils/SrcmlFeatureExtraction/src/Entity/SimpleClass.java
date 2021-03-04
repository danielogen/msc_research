package Entity;

public class SimpleClass {

    String className;
    String fullPath;
    String packagePath;
    String type;

    public SimpleClass(){

    }

    public SimpleClass(String className, String fullPath, String packagePath, String type) {
        this.className = className;
        this.fullPath = fullPath;
        this.packagePath = packagePath;
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return "SimpleClass{" +
                "className='" + className + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", packagePath='" + packagePath + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
