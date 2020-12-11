package smo;

import fosalgo.Data;
import fosalgo.DataReader;
import fosalgo.Vertex;
import java.io.File;

public class TestSpiderMonkeyOptimization {
    public static void main(String[] args) {
        //Dataset
        File file = new File("dataset/burma14.tsp");
        Vertex[] arrayVertex = DataReader.read(file);
        Data data = new Data(arrayVertex);

        //SMO Parameters
        int MAX_ITERATION               = 100; 
        int allowedMaximumGroup         = 4;
        double perturbationRate         = 0.8;//pr
        int localLeaderLimit            = 10; 
        int globalLeaderLimit           = 10; 
        int totalNumberOfSpiderMonkey   = 1000; 
        SpiderMonkeyOptimization smo    = new SpiderMonkeyOptimization(data, MAX_ITERATION, allowedMaximumGroup, perturbationRate, localLeaderLimit, globalLeaderLimit, totalNumberOfSpiderMonkey);
        System.out.print("Individu Terbaik SMO: ");
        System.out.println(smo.bestIndividu.toString());
    }
}
