package test;

import fosalgo.Data;
import fosalgo.DataReader;
import fosalgo.Vertex;
import ga.GeneticAlgorithm;
import java.io.File;
import smo.SpiderMonkeyOptimization;

public class Test_SMO_GA_001 {

    public static void main(String[] args) {
        //Dataset
        File file = new File("dataset/burma14.tsp");
        Vertex[] arrayVertex = DataReader.read(file);
        Data data = new Data(arrayVertex);

        int numTest = 10;

        double SumTourCostGA = 0;
        double SumTourCostSMO = 0;

        double AverageTourCostGA = -1;
        double AverageTourCostSMO = -1;

        double MinTourCostGA = Double.MAX_VALUE;
        double MinTourCostSMO = Double.MAX_VALUE;

        //lakukan pengujian untuk masing-masing Algoritma (GA dan SMO) sebanyak numTest kali
        for (int test = 1; test <= numTest; test++) {
            System.out.println("Test_"+test+"------------------------------------");
            //GA-------------------------------------------------------------------- 
            //Parameters
            int nGenerasi = 100;//iterasi
            int nPopulasi = 1000;//populasi
            int nIndividuTerseleksi = nPopulasi / 2;
            int nCrossoverPoint = arrayVertex.length / 4;
            double probabilitasMutasi = 0.2;//Mutation Rate
            int MAX_N_MUTATION = 24;
            GeneticAlgorithm ga = new GeneticAlgorithm(data, nGenerasi, nPopulasi, nIndividuTerseleksi, nCrossoverPoint, probabilitasMutasi, MAX_N_MUTATION);
            System.out.print("Individu Terbaik GA : ");
            System.out.println(ga.bestIndividu.toString());
            double totalJarakGA = ga.bestIndividu.getTotalJarak();
            SumTourCostGA = SumTourCostGA + totalJarakGA;
            if(totalJarakGA<MinTourCostGA){
                MinTourCostGA = totalJarakGA;
            }
            //END OF GA-------------------------------------------------------------

            //SMO-------------------------------------------------------------------
            int MAX_ITERATION = 100;//iterasi
            int allowedMaximumGroup = 4;
            double perturbationRate = 0.5;//pr
            int localLeaderLimit = 10;
            int globalLeaderLimit = 10;
            int totalNumberOfSpiderMonkey = 1000;//populasi
            SpiderMonkeyOptimization smo = new SpiderMonkeyOptimization(data, MAX_ITERATION, allowedMaximumGroup, perturbationRate, localLeaderLimit, globalLeaderLimit, totalNumberOfSpiderMonkey);
            System.out.print("Individu Terbaik SMO: ");
            System.out.println(smo.bestIndividu.toString());
            double totalJarakSMO = smo.bestIndividu.getTotalJarak();
            SumTourCostSMO = SumTourCostSMO + totalJarakSMO;
            if(totalJarakSMO<MinTourCostSMO){
                MinTourCostSMO = totalJarakSMO;
            }
            //END OF SMO------------------------------------------------------------
        }//end of loop for testing
        
        //Hitung rata-rata pencapaian total jarak
        AverageTourCostGA = SumTourCostGA/numTest;
        AverageTourCostSMO = SumTourCostSMO/numTest;
        
        System.out.println("=========================================================");
        System.out.println("H A S I L    P E R B A N D I N G A N    A L G O R I T M A");
        System.out.println("=========================================================");
        System.out.println("Average Tour Cost GA  = "+AverageTourCostGA);
        System.out.println("Average Tour Cost SMO = "+AverageTourCostSMO);
        System.out.println("---------------------------------------------------------");
        System.out.println("Minimum Tour Cost GA  = "+MinTourCostGA);
        System.out.println("Minimum Tour Cost SMO = "+MinTourCostSMO);
        System.out.println("---------------------------------------------------------");

    }
}
