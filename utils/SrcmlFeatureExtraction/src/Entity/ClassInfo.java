package Entity;

/**
 * Created by arifn on 8/22/2017.
 */
public class ClassInfo {

    private int no;
    private String library; // eg. k9mail or k9mail-library
    private String path; // eg. main/java/com/fsck/k9/Account.java
    private String javapathslash; // eg. k9mail/main/java/com/fsck/k9/Account
    private String javapathdot; // eg. k9mail.main.java.com.fsck.k9.Account
    private String role; // eg. Information Holder

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getJavapathslash() {
        return javapathslash;
    }

    public void setJavapathslash(String javapathslash) {
        this.javapathslash = javapathslash;
    }

    public String getJavapathdot() {
        return javapathdot;
    }

    public void setJavapathdot(String javapathdot) {
        this.javapathdot = javapathdot;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "no=" + no +
                ", library='" + library + '\'' +
                ", path='" + path + '\'' +
                ", javapathslash='" + javapathslash + '\'' +
                ", javapathdot='" + javapathdot + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
