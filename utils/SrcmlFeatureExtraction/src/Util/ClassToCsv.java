package Util;

import Entity.SimpleClass;
import joinery.DataFrame;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ClassToCsv {

    private static DataFrame<Object> dataFrame = new DataFrame<Object>("no", "fullpath","packagepath", "classname",
            "type");

    public static void SimpleClasstoCSV(List<SimpleClass> list){

        for(int i=0;i<list.size();i++){
            SimpleClass simpleClass = list.get(i);
            dataFrame.append(i+1, Arrays.asList(i+1,simpleClass.getFullPath(), simpleClass.getPackagePath(),
                    simpleClass.getClassName(), simpleClass.getType()));

        }

        try {
            dataFrame.writeCsv("data\\output\\listclass.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
