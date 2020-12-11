package fosalgo;

import java.util.Random;

public class Individu {

    private int[] kromosom = null;
    private Data data = null;
    private double totalJarak = -1;
    private double nilaiFitness = 0;

    public Individu(Data data) {
        this.data = data;
    }

    public Individu clone() {
        Individu cloning = new Individu(data);
        cloning.kromosom = this.kromosom.clone();
        this.hitungNilaiFitness();
        return cloning;
    }
    
    public Individu(Data data, int[] kromosom) {
        this.data = data;
        this.kromosom = kromosom;
    }
    
    public Individu(int[] kromosom) {
        this.kromosom = kromosom;
    }

    public int[] getKromosom() {
        return kromosom;
    }

    public Data getData() {
        return data;
    }

    public double getTotalJarak() {
        return totalJarak;
    }

    public double getNilaiFitness() {
        return nilaiFitness;
    }

    public static int randomBetween(int min, int max) {
        if (min >= max) {
            //tukar
            int temp = min;
            min = max;
            max = temp;
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public int[] generateRandomKromosom() {
        if (data != null && data.getArrayVertex().length > 0) {
            int n = data.getArrayVertex().length;
            int min = 0;
            int max = n - 1;

            //random vertyex awal dan akhir
            int vertexAwal = randomBetween(min, max);
            int vertexAkhir = vertexAwal;

            //Kromosom
            this.kromosom = new int[n + 1];
            this.kromosom[0] = vertexAwal;//vertex awal
            this.kromosom[n] = vertexAkhir;//vertex akhir

            //random vertex antara
            for (int i = 1; i < n; i++) {
                boolean sama = true;
                while (sama) {
                    int r = randomBetween(min, max);
                    sama = false;
                    for (int j = 0; j < i; j++) {
                        if (r == this.kromosom[j]) {
                            sama = true;
                            break;
                        }
                    }
                    if (sama == false) {
                        this.kromosom[i] = r;
                    }
                }
            }
        }
        return this.kromosom;
    }

    public double hitungTotalJarak() {
        this.totalJarak = -1;
        if (kromosom != null) {
            double total = 0;
            for (int i = 1; i < kromosom.length; i++) {
                int indexVertex1 = kromosom[i - 1];
                int indexVertex2 = kromosom[i];
                double jarak = data.hitungJarak(indexVertex1, indexVertex2);
                total += jarak;
            }
            this.totalJarak = total;
        }
        return this.totalJarak;
    }

    public double hitungNilaiFitness() {
        this.nilaiFitness = 0;
        this.hitungTotalJarak();
        if (this.totalJarak > 0) {
            this.nilaiFitness = 1.0 / this.totalJarak;
        }
        return this.nilaiFitness;
    }

    @Override
    public String toString() {
        String result = "NULL";
        if (kromosom != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < kromosom.length; i++) {
                if (i > 0) {
                    sb.append(" - ");
                }
                int indexVertex = kromosom[i];
                String label = data.getArrayVertex()[indexVertex].label;
                sb.append(label);
            }
            sb.append(" Total Distance: "+this.totalJarak);
            sb.append(" Fitness: "+this.nilaiFitness);
            result = sb.toString();
        }
        return result;
    }    
}