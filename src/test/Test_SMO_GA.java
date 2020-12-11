package test;

import fosalgo.Data;
import fosalgo.DataReader;
import fosalgo.Vertex;
import ga.GeneticAlgorithm;
import java.io.File;
import smo.SpiderMonkeyOptimization;

public class Test_SMO_GA {
    public static void main(String[] args) {
        //Dataset
        File file = new File("dataset/burma14.tsp");
        Vertex[] arrayVertex = DataReader.read(file);
        Data data = new Data(arrayVertex);

        //GA-------------------------------------------------------------------- 
        //Parameters
        int nGenerasi = 100;
        int nPopulasi = 1000;
        int nIndividuTerseleksi = nPopulasi / 2;
        int nCrossoverPoint = arrayVertex.length / 4;
        double probabilitasMutasi = 0.2;//Mutation Rate
        int MAX_N_MUTATION = 24;
        GeneticAlgorithm ga = new GeneticAlgorithm(data, nGenerasi, nPopulasi, nIndividuTerseleksi, nCrossoverPoint, probabilitasMutasi, MAX_N_MUTATION);
        System.out.print("Individu Terbaik GA : ");
        System.out.println(ga.bestIndividu.toString());
        //END OF GA-------------------------------------------------------------
        
        //SMO-------------------------------------------------------------------
        int MAX_ITERATION               = 100; 
        int allowedMaximumGroup         = 4;
        double perturbationRate         = 0.5;//pr
        int localLeaderLimit            = 10; 
        int globalLeaderLimit           = 10; 
        int totalNumberOfSpiderMonkey   = 1000; 
        SpiderMonkeyOptimization smo    = new SpiderMonkeyOptimization(data, MAX_ITERATION, allowedMaximumGroup, perturbationRate, localLeaderLimit, globalLeaderLimit, totalNumberOfSpiderMonkey);
        System.out.print("Individu Terbaik SMO: ");
        System.out.println(smo.bestIndividu.toString());
        //END OF SMO------------------------------------------------------------
    }
}
