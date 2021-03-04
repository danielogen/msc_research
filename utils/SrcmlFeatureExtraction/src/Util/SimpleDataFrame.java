package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SimpleDataFrame {

    private String[] columnNames;
    private int numColumn;
    private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    private boolean separator = true;

    /**
     * Constructor for the simple data frame
     * @param columns name of the columns
     */
    public SimpleDataFrame(String ... columns){
        numColumn = columns.length;
        columnNames = new String[numColumn];

        int i=0;
        for(String column: columns){
            columnNames[i++] = column;
        }

    }


    public void printColumnNames(){

        int i = columnNames.length;
        for(String title: columnNames){
            System.out.print(title);
            if(i-- > 1){
                System.out.print(",");
            }
        }
    }

    /**
     * adding data to the end of the dataframe
     * @param rows one row of the data
     */
    public void append(String ... rows){

        ArrayList datum =  new ArrayList<String>();

        for(String iterate: rows){
            datum.add(iterate);
        }

        data.add(datum);
    }

    /**
     * printing the content of the csv
     * @return the content of the csv
     */
    public String toString(){
        StringBuilder output = new StringBuilder();

        // printing column name
        for(String columnName: columnNames){
            output.append(columnName);
            output.append(",");
        }
        output.append("\n");

        // printing rows
        for(ArrayList<String> rows: data){
            int i = columnNames.length;
            for(String column: rows){
                output.append(column);
                if(i-- > 1){
                    output.append(",");
                }
            }
            output = output.append("\n");
        }

        return output.toString();
    }

    /**
     * Writing to CSV file
     * @param filename filename.csv
     */
    public void writeToCSV(String filename){

        String content = "";
        if(separator){
            content = content.concat("sep=,\n");
        }
        content = content.concat(this.toString());

        try {
            PrintWriter pw = new PrintWriter(new File(filename));
            pw.write(content);
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
