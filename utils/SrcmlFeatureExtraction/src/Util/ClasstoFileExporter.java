package Util;

import Entity.Kelas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by arifn on 8/1/2017.
 */
public class ClasstoFileExporter {

    private static String filepath = "data\\output\\"; // default path location


    public static void export(ArrayList<Kelas> classes){

        for(Kelas kelas: classes){
            String filename = filepath + kelas.getWritingPath();
            filename = filename.replaceAll("<[A-z]([,]\\s*[A-z])*>","");
            filename = filename.replaceAll("<[A-z](\\s[A-z]*)*>*","");
//            System.out.println(kelas.getWritingPath());
//            System.out.println(filename);
            write(filename,kelas.getPrefix() + "\n" + kelas.getContent());
//            System.out.println("...");
        }

    }

    public static void export(ArrayList<Kelas> classes, String path){
        filepath = path;
        export(classes);
    }

    private static void write(String filename, String content){

        File file = new File(filename);
        file.getParentFile().mkdirs();
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
//            System.out.print("...");
            bw.write(content);
//            System.out.print("...");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }
}
