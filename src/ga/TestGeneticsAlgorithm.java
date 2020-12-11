 package ga;

import fosalgo.Data;
import fosalgo.DataReader;
import fosalgo.Vertex;
import java.io.File;

public class TestGeneticsAlgorithm {

    public static void main(String[] args) {
        //Dataset
        File file = new File("dataset/burma14.tsp");
        Vertex[] arrayVertex = DataReader.read(file);
        Data data = new Data(arrayVertex);

        //GA Parameters
        int nGenerasi = 100;
        int nPopulasi = 1000;
        int nIndividuTerseleksi = nPopulasi / 2;
        int nCrossoverPoint = 4;//arrayVertex.length / 4;
        double probabilitasMutasi = 0.8;//Mutation Rate
        int MAX_N_MUTATION = 24;

        GeneticAlgorithm ga = new GeneticAlgorithm(data, nGenerasi, nPopulasi, nIndividuTerseleksi, nCrossoverPoint, probabilitasMutasi, MAX_N_MUTATION);

        System.out.print("Individu Terbaik GA: ");
        System.out.println(ga.bestIndividu.toString());
    }
}
