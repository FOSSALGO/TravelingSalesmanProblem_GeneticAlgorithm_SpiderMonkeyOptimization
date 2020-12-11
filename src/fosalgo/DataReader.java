package fosalgo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataReader {

    public static Vertex[] read(File file) {
        Vertex[] arrayVertex = null;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String baris;
            ArrayList<Vertex> listVertex = new ArrayList<>();

            //read header data set
            boolean bacaVertex = false;
            while ((baris = br.readLine()) != null) {

                //validasi baris------------------------
                if (baris.length() >= 18
                        && baris.substring(0, 18).equals("NODE_COORD_SECTION")) {
                    bacaVertex = true;
                } else if (baris.equals("EOF")) {
                    bacaVertex = false;
                    break;
                }
                //end of validasi baris------------------------
                if (bacaVertex == true) {
                    baris = baris.replaceAll("\\s+", " ");
                    if (baris.substring(0, 1).equals(" ")) {
                        baris = baris.substring(1);
                    }
                    String[] dBaris = baris.split("\\s");
                    if (dBaris.length == 3) {
                        String label = dBaris[0];
                        double x = Double.parseDouble(dBaris[1]);
                        double y = Double.parseDouble(dBaris[2]);
                        Vertex v = new Vertex(label, x, y);
                        listVertex.add(v);
                        //System.out.println(v.toString());
                    }
                }
            }//end of while

            if (listVertex.size() > 0) {
                //convert to arrayVertex
                int n = listVertex.size();
                arrayVertex = new Vertex[n];
                for (int i = 0; i < arrayVertex.length; i++) {
                    arrayVertex[i] = listVertex.get(i);
                }
            }
        } catch (FileNotFoundException ex) {
            //ex.printStackTrace();
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        return arrayVertex;
    }

}
