package ga;

import fosalgo.Quick;
import fosalgo.Data;
import fosalgo.Individu;
import fosalgo.Swap;
import fosalgo.Vertex;
import java.util.Random;

public class GeneticAlgorithm {

    //INPUT-----------------------
    //Dataset
    private Data data = null;
    private Vertex[] arrayVertex = null;
    private int nVertex = 0;

    //Parameter GA
    private int nGenerasi;
    private int nPopulasi;
    private int nIndividuTerseleksi;
    private int nCrossoverPoint;
    private double probabilitasMutasi;
    private int MAX_N_MUTATION;
    private int panjangKromosom;

    //OUTPUT----------------------
    double bestFitness = 0;
    double bestTotalJarak = 0;
    public Individu bestIndividu = null;

    //Random
    private Random random = new Random();

    public GeneticAlgorithm(Data data, int nGenerasi, int nPopulasi, int nIndividuTerseleksi, int nCrossoverPoint, double probabilitasMutasi, int MAX_N_MUTATION) {
        
        //baca dataset
        this.data = data;
        this.arrayVertex = data.getArrayVertex();
        this.nVertex = this.arrayVertex.length;
        this.panjangKromosom = this.nVertex + 1;
        //System.out.println(data.toString());
        
        this.nGenerasi = nGenerasi;
        this.nPopulasi = nPopulasi;
        this.nIndividuTerseleksi = nIndividuTerseleksi;
        this.nCrossoverPoint = nCrossoverPoint;
        this.MAX_N_MUTATION = MAX_N_MUTATION;
        this.probabilitasMutasi = probabilitasMutasi;

        //RUN GA
        this.run();
    }

    public boolean validasiParameters() {
        boolean valid = true;
        if (data == null
                || nGenerasi < 0
                || nPopulasi < 0) {
            valid = false;
        }
        if (nIndividuTerseleksi == 1) {
            nIndividuTerseleksi = 2;
        }
        if (this.nIndividuTerseleksi > this.nPopulasi) {
            this.nIndividuTerseleksi = this.nPopulasi;
        }
        if (nCrossoverPoint <= 0) {
            nCrossoverPoint = 1;
        }
        if (nCrossoverPoint > data.getArrayVertex().length) {
            nCrossoverPoint = data.getArrayVertex().length;
        }
        if (this.probabilitasMutasi < 0) {
            this.probabilitasMutasi = 0;
        } else if (this.probabilitasMutasi > 1) {
            this.probabilitasMutasi = 1;
        }
        if (this.MAX_N_MUTATION < 0) {
            this.MAX_N_MUTATION = 0;
        } else if (this.MAX_N_MUTATION > data.getArrayVertex().length) {
            this.MAX_N_MUTATION = data.getArrayVertex().length;
        }
        return valid;
    }

    public Individu[] generateRandomPopulasi() {
        Individu[] populasi = null;
        if (this.data != null && nPopulasi > 0) {
            populasi = new Individu[nPopulasi];
            for (int p = 0; p < populasi.length; p++) {
                populasi[p] = new Individu(data);
                populasi[p].generateRandomKromosom();
                populasi[p].hitungNilaiFitness();
            }
        }
        return populasi;
    }

    public Individu[] sortByFitness(Individu[] populasi) {
        //Fitness dimasing-masing individu harus sudah dihitung terlebih dahulu
        double[][] value = new double[populasi.length][2];
        for (int i = 0; i < value.length; i++) {
            value[i][0] = populasi[i].getNilaiFitness();
            value[i][1] = i;
        }
        //Sorting menggunakan Algoritma Quick Sort
        Quick.sort(value);

        //susun ulang populasi berdasarkan hasil sorting
        Individu[] newPopulasi = new Individu[populasi.length];
        for (int i = 0; i < newPopulasi.length; i++) {
            int index = (int) value[i][1];
            newPopulasi[i] = populasi[index];
        }

        //return new populasi hasil sorting berdasarkan nilai fitness
        return newPopulasi;
    }

    private int randomBetween(int min, int max) {
        if (min >= max) {
            //tukar
            int temp = min;
            min = max;
            max = temp;
        }
        return random.nextInt((max - min) + 1) + min;
    }

    private int[] randomCrossoverPoint() {
        int[] crossoverPoint = new int[nCrossoverPoint];
        for (int i = 0; i < crossoverPoint.length; i++) {
            boolean sama = true;
            while (sama) {
                int r = randomBetween(0, panjangKromosom - 2);
                sama = false;
                for (int j = 0; j < i; j++) {
                    if (r == crossoverPoint[j]) {
                        sama = true;
                        break;
                    }
                }
                if (sama == false) {
                    crossoverPoint[i] = r;
                }
            }
        }
        return crossoverPoint;
    }

    public void printIndividu(Individu individu) {
        System.out.println(individu.toString());
    }

    public void printPopulasi(Individu[] populasi) {
        for (int p = 0; p < populasi.length; p++) {
            System.out.print("Individu: ");
            printIndividu(populasi[p]);
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void run() {

        if (validasiParameters()) {
            //Generate Random Populasi
            Individu[] populasi = generateRandomPopulasi();
            //Sorting berdasarkan nilai fitness
            populasi = sortByFitness(populasi);

            //printPopulasi(populasi);
            //Melakukan Proses Evolusi
            for (int g = 1; g <= nGenerasi; g++) {

                //Inisialisasi Populasi Baru
                Individu[] nextGeneration = new Individu[nPopulasi];

                //PROSES SELEKSI TOURNAMENT
                for (int i = 0; i < nIndividuTerseleksi; i++) {
                    nextGeneration[i] = populasi[i];
                }

                int k = nIndividuTerseleksi;

                //PROSES CROSSOVER jika nIndividuTerseleksi< nPopulasi
                //Proses Crossover akan mencukupkan jumlah individu di populasi baru menjadi sebanyak nPopulasi lagi
                //Kita gunakan Partially Mapped Crossover (PMX)
                while (k < nPopulasi) {
                    //pilih pasangan parent secara random dari individu-individu hasil seleksi
                    int iParentA = randomBetween(0, nIndividuTerseleksi - 1);
                    int iParentB = randomBetween(0, nIndividuTerseleksi - 1);
                    //iParentA tidak boleh sama dengan iParentB
                    while (iParentA == iParentB) {
                        iParentB = randomBetween(0, nIndividuTerseleksi - 1);
                    }
                    //random posisi crossover point sebanyak nCrossoverPoint
                    int[] crossoverPoint = randomCrossoverPoint();

                    int[] kromosomParentA = nextGeneration[iParentA].getKromosom();
                    int[] kromosomParentB = nextGeneration[iParentB].getKromosom();

                    int[] kromosomOffspringA = kromosomParentA.clone();
                    int[] kromosomOffspringB = kromosomParentB.clone();

                    for (int c = 0; c < crossoverPoint.length; c++) {
                        int index = crossoverPoint[c];

                        int keyA = kromosomParentB[index];
                        int keyB = kromosomParentA[index];

                        int valueA = kromosomOffspringA[index];
                        int valueB = kromosomOffspringB[index];

                        //cross value
                        if (keyA != valueA) {
                            //cari key lalu ganti nilainya dg value
                            for (int i = 0; i < kromosomOffspringA.length - 1; i++) {
                                if (kromosomOffspringA[i] == keyA) {
                                    kromosomOffspringA[index] = keyA;
                                    kromosomOffspringA[i] = valueA;
                                    break;
                                }
                            }
                        }

                        if (keyB != valueB) {
                            for (int i = 0; i < kromosomOffspringB.length - 1; i++) {
                                if (kromosomOffspringB[i] == keyB) {
                                    kromosomOffspringB[index] = keyB;
                                    kromosomOffspringB[i] = valueB;
                                    break;
                                }
                            }
                        }
                    }
                    kromosomOffspringA[kromosomOffspringA.length - 1] = kromosomOffspringA[0];
                    kromosomOffspringB[kromosomOffspringB.length - 1] = kromosomOffspringB[0];

                    if (k < nPopulasi) {
                        nextGeneration[k] = new Individu(data, kromosomOffspringA);
                        k++;
                    }

                    if (k < nPopulasi) {
                        nextGeneration[k] = new Individu(data, kromosomOffspringB);
                        k++;
                    }

                }//end of while 
                //Proses Crossover selesai 

                //Proses Mutasi
                //kita gunakan Swap Mutation
                //untuk semua individu di populasi, bangkitkan sebuah bilangan random rm.
                //jika rm <= probabilitas mutasi maka lakukan swap mutation                
                for (int i = 0; i < nextGeneration.length; i++) {
                    double rm = random.nextDouble();
                    if (rm >= this.probabilitasMutasi) {
                        //lakukan swap mutasi
                        int nMutation = randomBetween(1, MAX_N_MUTATION);
                        //do swap operation sebanyak nMutation
                        for (int s = 0; s < nMutation; s++) {
                            //random dua titik untuk di swap
                            int indexVertex1 = randomBetween(0, panjangKromosom - 2);
                            int indexVertex2 = randomBetween(0, panjangKromosom - 2);
                            while (indexVertex1 == indexVertex2) {
                                indexVertex2 = randomBetween(0, panjangKromosom - 2);
                            }
                            //lakukan operasi swap dengan memanggil method swap di individu
                            //nextGeneration[i] = nextGeneration[i].swap(indexVertex1, indexVertex2);
                            
                            nextGeneration[i] = Swap.swapOperation(nextGeneration[i], indexVertex1, indexVertex2);
                        }
                    }
                }
                //Proses Mutasi Selesai   

                //Set populasiBaru sebagai populasi dan hitung nilai fitness
                populasi = new Individu[nPopulasi];
                for (int i = 0; i < populasi.length; i++) {
                    populasi[i] = nextGeneration[i];
                    populasi[i].hitungNilaiFitness();
                }

                //Sorting berdasarkan nilai fitness
                populasi = sortByFitness(populasi);
                
                //printPopulasi(populasi);

                //Lakukan Operasi Elitism
                //lakukan evaluasi elitism
                //Karena sudah terurut, berarti data yang paling atas yang paling besar nilai fitnessnya
                if (populasi[0].getNilaiFitness() > this.bestFitness) {
                    this.bestIndividu = populasi[0];
                    this.bestFitness = bestIndividu.getNilaiFitness();
                    this.bestTotalJarak = bestIndividu.getTotalJarak();
                }

                //print populasi baru yang dihasilkan
                //System.out.println("- + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + -");
                //System.out.println("Generasi_" + g);
                //System.out.println("Best Individu_" + g + ": " + this.bestIndividu.toString());
                //System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
                //this.printPopulasi(populasi);

            }//end of for g
        }
    }//end of run GA    
}
