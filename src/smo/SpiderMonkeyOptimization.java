package smo;

import fosalgo.Data;
import fosalgo.Individu;
import fosalgo.Swap;
import fosalgo.Vertex;
import java.util.Random;

public class SpiderMonkeyOptimization {

    //INPUT-----------------------
    //Dataset
    private Data data = null;
    private Vertex[] arrayVertex = null;//Array of Vertices/Cities
    private int nVertex = 0;

    //Parameter SMO
    private int panjangKromosom;
    private int I;//Total Number of Iterations
    private int MG;//Allowed Maximum Group
    private double pr;//Perturbation Rate
    private int LLL;//Local Leader Limit
    private int GLL;//Global Leader Limit
    private int N;//Total Number of Spider Monkeys = nPopulasi     

    //Output
    public Individu bestIndividu = null;

    //Random
    private Random random = new Random();

    public SpiderMonkeyOptimization(
            Data data, int MAX_ITERATION, int allowedMaximumGroup, double perturbationRate,
            int localLeaderLimit, int globalLeaderLimit, int totalNumberOfSpiderMonkey) {

        //baca dataset
        this.data = data;
        this.arrayVertex = data.getArrayVertex();
        this.nVertex = this.arrayVertex.length;
        this.panjangKromosom = this.nVertex + 1;
        //System.out.println(data.toString());

        //set parameters SMO
        this.I = MAX_ITERATION;
        this.MG = allowedMaximumGroup;
        this.pr = perturbationRate;
        this.LLL = localLeaderLimit;
        this.GLL = globalLeaderLimit;
        this.N = totalNumberOfSpiderMonkey;

        //RUN SMO
        this.run();
    }

    public boolean validasiParameters() {
        boolean valid = true;
        if (this.MG >= this.N) {
            this.MG = this.N / 2;
        }
        return valid;
    }

    public Individu[] generateRandomPopulasi() {
        Individu[] populasi = null;
        if (this.data != null && this.N > 0) {
            populasi = new Individu[this.N];
            for (int p = 0; p < populasi.length; p++) {
                populasi[p] = new Individu(data);
                populasi[p].generateRandomKromosom();
                populasi[p].hitungNilaiFitness();
            }
        }
        return populasi;
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

    public void printIndividu(Individu individu) {
        System.out.println(individu.toString());
    }

    public void printPopulasiSpiderMonkey(Individu[] populasi, Individu[] localLeaders, Individu globalLeader) {
        System.out.println("=====================================================================================================================================");
        //print Global Leader
        if (globalLeader != null) {
            System.out.println("GL: " + globalLeader.toString());
        }
        System.out.println("-----------------------------------------------------------------");
        //print LocalLeader
        if (localLeaders != null) {
            for (int i = 0; i < localLeaders.length; i++) {
                if (localLeaders[i] != null) {
                    System.out.println("LL_" + (i + 1) + ": " + localLeaders[i].toString());
                }
            }
        }
        int groupSize = -1;
        if (populasi != null && localLeaders != null) {
            //groupSize = populasi.length / localLeaders.length;
            groupSize = (int) Math.floor((double) N / (double) localLeaders.length);
        }
        int gr = 0;
        for (int p = 0; p < populasi.length; p++) {
            if (groupSize > 0 && p % groupSize == 0 && gr < localLeaders.length) {
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Group_" + gr + ":");
                gr++;
            }
            //print SM
            System.out.println("SM_" + (p) + ": " + populasi[p].toString());
        }
        //System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("=====================================================================================================================================");
    }

    public Individu[] selectLocalLeader(Individu[] spiderMonkey, int g) {
        Individu[] LL = null;
        if (spiderMonkey != null && g > 0) {
            int groupSize = (int) Math.floor((double) N / (double) g);
            LL = new Individu[g];
            for (int k = 0; k < g; k++) {
                LL[k] = null;
                int indexLL = -1;
                double bFitness = 0;
                boolean inGroup = true;
                int index = 0;
                while (inGroup) {
                    int i = groupSize * k + index;
                    if (i >= spiderMonkey.length) {
                        inGroup = false;
                        break;
                    }
                    //System.out.println("TRACE " + groupSize + "*" + k + "+" + index + "=" + i);
                    Individu SMi = spiderMonkey[i];
                    if (SMi.getNilaiFitness() > bFitness) {
                        bFitness = SMi.getNilaiFitness();
                        indexLL = i;
                    }
                    index++;
                    if (index != 0 && index % groupSize == 0) {
                        inGroup = false;
                        if (k >= g - 1) {
                            inGroup = true;
                        } else {
                            break;
                        }
                    }
                }
                //set Local leader 
                LL[k] = spiderMonkey[indexLL].clone();
                LL[k].hitungNilaiFitness();
            }
        }
        return LL;
    }

    public Individu selectGlobalLeader(Individu[] LL) {
        Individu GL = null;
        if (LL != null) {
            int indexGL = -1;
            double bestFitness = 0;
            int g = LL.length;
            for (int k = 0; k < g; k++) {
                if (LL[k].getNilaiFitness() > bestFitness) {
                    bestFitness = LL[k].getNilaiFitness();
                    indexGL = k;
                }
            }
            GL = LL[indexGL].clone();
            GL.hitungNilaiFitness();
        }
        return GL;
    }

    public Individu[] updateSpiderMonkeyBaseOnLocalLeader(Individu[] spiderMonkey, Individu[] LL, Individu GL) {
        Individu[] SM = null;
        if (spiderMonkey != null && LL != null && GL != null) {
            double min_cost = Double.MAX_VALUE;
            SM = new Individu[spiderMonkey.length];
            int g = LL.length;
            if (g > 0) {
                int groupSize = (int) Math.floor((double) N / (double) g);
                //Update Spider Monkey base on Local Leader---------------------                
                for (int k = 0; k < g; k++) {
                    boolean inGroup = true;
                    int index = 0;
                    while (inGroup) {
                        int i = groupSize * k + index;
                        if (i >= spiderMonkey.length) {
                            inGroup = false;
                            break;
                        }
                        double U = random.nextDouble();
                        if (U >= pr) {
                            int MIN = k * groupSize;
                            int MAX = (k + 1) * groupSize - 1;
                            if (spiderMonkey.length - 1 - MAX < groupSize) {
                                MAX = spiderMonkey.length - 1;
                            }
                            int indexRandom = randomBetween(MIN, MAX);
                            while (indexRandom == i) {
                                indexRandom = randomBetween(MIN, MAX);
                            }
                            Individu LLk = LL[k];//LocalLeader ke k
                            SM[i] = spiderMonkey[i];
                            Individu RSM = spiderMonkey[indexRandom];

                            int[][] LLk_SMi = Swap.subtract(LLk, SM[i]);//LLk - SMi 
                            int[][] RSM_SMi = Swap.subtract(RSM, SM[i]);//RSM - SMi

                            int[][] SSi = Swap.mergeSwapSequence(LLk_SMi, RSM_SMi);//Persamaan 6 di paper
                            int[][] BSSi = Swap.callBasicSwapSequence(SSi, panjangKromosom);
                            Individu SMnewi = Swap.add(SM[i], BSSi);//diperoleh individu baru;
                            if (SMnewi.getNilaiFitness() > SM[i].getNilaiFitness()) {
                                SM[i] = SMnewi;
                            }
                        } else {
                            SM[i] = spiderMonkey[i];
                        }
                        //check min_cost
                        if (SM[i].getTotalJarak() >= 0 && spiderMonkey[index].getTotalJarak() < min_cost) {
                            min_cost = SM[i].getTotalJarak();
                        }
                        //increment index
                        index++;
                        if (index != 0 && index % groupSize == 0) {
                            inGroup = false;
                            if (k >= g - 1) {
                                inGroup = true;
                            } else {
                                break;
                            }
                        }
                    }
                }//end of for (int k = 0; k < g; k++)//end of Update Spider Monkey base on Local Leader
                //Update Spider Monkey base on Global Leader---------------------                
                for (int k = 0; k < g; k++) {
                    //System.out.println("g = "+g);
                    boolean inGroup = true;
                    int index = 0;
                    while (inGroup) {
                        int i = groupSize * k + index;
                        if (i >= SM.length) {
                            inGroup = false;
                            break;
                        }
                        double U = random.nextDouble();
                        double cost_i = SM[i].getTotalJarak();
                        double prob_i = 0.9 * (min_cost / cost_i) + 0.1;
                        if (U <= prob_i) {
                            int MIN = 0;
                            int MAX = SM.length - 1;
                            int indexRandom = randomBetween(MIN, MAX);
                            while (indexRandom == i) {
                                indexRandom = randomBetween(MIN, MAX);
                            }
                            Individu RSM = SM[indexRandom];

                            int[][] GL_SMi = Swap.subtract(GL, SM[i]);//LLk - SMi 
                            int[][] RSM_SMi = Swap.subtract(RSM, SM[i]);//RSM - SMi

                            int[][] SSi = Swap.mergeSwapSequence(GL_SMi, RSM_SMi);//Persamaan 6 di paper
                            int[][] BSSi = Swap.callBasicSwapSequence(SSi, panjangKromosom);
                            Individu SMnewi = Swap.add(SM[i], BSSi);//diperoleh individu baru;
                            SMnewi.hitungNilaiFitness();
                            if (SMnewi.getNilaiFitness() > SM[i].getNilaiFitness()) {
                                SM[i] = SMnewi;
                            }
                        }

                        //increment index
                        index++;
                        if (index != 0 && index % groupSize == 0) {
                            inGroup = false;
                            if (k >= g - 1) {
                                inGroup = true;
                            } else {
                                break;
                            }
                        }
                    }
                }//end of for (int k = 0; k < g; k++)//end of Update Spider Monkey base on Global Leader
            }//end of if(g>0)            
        }//end of if 
        return SM;
    }

    public void run() {
        //Variables
        if (validasiParameters()) {
            //Generate Random Populasi            
            Individu[] spiderMonkey = generateRandomPopulasi();//create N SpiderMonkey
            int g = 1;//initially consider all SM into one group  
            //Select local leader and global leader
            Individu[] LL = selectLocalLeader(spiderMonkey, g);
            Individu GL = selectGlobalLeader(LL);//Global Leader   
            //Limit
            int[] LLLc = new int[LL.length];//Local Leader Limit
            int GLLc = 0;//Global Leader Limit Count

            //printPopulasiSpiderMonkey(spiderMonkey, LL, GL);            
            //System.out.println("Global Leader: " + GL.toString());
            //memulai operasi iterasi
            for (int t = 1; t <= I; t++) {
                spiderMonkey = updateSpiderMonkeyBaseOnLocalLeader(spiderMonkey, LL, GL);

                //Update phase local leader------------------------------------
                if (spiderMonkey != null && g > 0) {
                    int groupSize = (int) Math.floor((double) N / (double) g);
                    for (int k = 0; k < g; k++) {
                        int indexLL = -1;
                        double bFitness = LL[k].getNilaiFitness();
                        boolean inGroup = true;
                        int index = 0;
                        while (inGroup) {
                            int i = groupSize * k + index;
                            if (i >= spiderMonkey.length) {
                                inGroup = false;
                                break;
                            }
                            //System.out.println("TRACE " + groupSize + "*" + k + "+" + index + "=" + i);
                            Individu SMi = spiderMonkey[i];
                            if (SMi.getNilaiFitness() > bFitness) {
                                bFitness = SMi.getNilaiFitness();
                                indexLL = i;
                            }
                            index++;
                            if (index != 0 && index % groupSize == 0) {
                                inGroup = false;
                                if (k >= g - 1) {
                                    inGroup = true;
                                } else {
                                    break;
                                }
                            }
                        }//end of while
                        //set Local leader 
                        if (indexLL >= 0) {
                            LL[k] = spiderMonkey[indexLL].clone();//Penetapan Local Leader baru
                            LL[k].hitungNilaiFitness();
                            LLLc[k] = 0;
                        } else {
                            LLLc[k]++;
                        }
                    }
                }
                //end of update phase local leader------------------------------

                //Update phase global leader------------------------------------
                if (LL != null) {
                    int indexGL = -1;
                    double bestFitness = GL.getNilaiFitness();
                    for (int k = 0; k < g; k++) {
                        if (LL[k].getNilaiFitness() > bestFitness) {
                            bestFitness = LL[k].getNilaiFitness();
                            indexGL = k;
                        }
                    }
                    if (indexGL >= 0) {
                        GL = LL[indexGL].clone();
                        GL.hitungNilaiFitness();
                        GLLc = 0;
                    } else {
                        GLLc++;
                    }
                }
                //end of update phase global leader-----------------------------

                //Decision Phase Local Leader-----------------------------------
                if (spiderMonkey != null && g > 0) {
                    int groupSize = (int) Math.floor((double) N / (double) g);
                    for (int k = 0; k < g; k++) {
                        if (LLLc[k] > LLL) {
                            LLLc[k] = 0;
                        }
                        boolean inGroup = true;
                        int index = 0;
                        while (inGroup) {
                            int i = groupSize * k + index;
                            if (i >= spiderMonkey.length) {
                                inGroup = false;
                                break;
                            }
                            double U = random.nextDouble();
                            if (U >= pr) {
                                spiderMonkey[i] = new Individu(data);
                                spiderMonkey[i].generateRandomKromosom();
                                spiderMonkey[i].hitungNilaiFitness();
                            } else {
                                //initialize SMi using EQ 13 
                                Individu SMi = spiderMonkey[i].clone();
                                U = random.nextDouble();
                                Individu SMi_A = null;
                                Individu SMi_B = null;
                                if (U >= pr) {
                                    int[][] ss = Swap.subtract(GL, SMi);
                                    SMi_A = Swap.add(SMi, ss);
                                }
                                if (U >= pr) {
                                    int[][] ss = Swap.subtract(SMi, LL[k]);
                                    SMi_B = Swap.add(SMi_A, ss);
                                }
                                Individu SMi_new = null;
                                if (SMi_B != null) {
                                    SMi_new = SMi_B;
                                } else if (SMi_A != null) {
                                    SMi_new = SMi_A;
                                } else {
                                    SMi_new = SMi;
                                }
                                if (SMi_new != null) {
                                    SMi_new.hitungNilaiFitness();
                                    spiderMonkey[i] = SMi_new;
                                }
                            }
                            index++;
                            if (index != 0 && index % groupSize == 0) {
                                inGroup = false;
                                if (k >= g - 1) {
                                    inGroup = true;
                                } else {
                                    break;
                                }
                            }
                        }//end of while
                    }
                }
                //End of Decision Phase Local Leader----------------------------

                //Decision Phase Global Leader----------------------------------
                if (GLLc > GLL) {
                    //System.out.println("DECISION PHASE");
                    if (g < MG) {
                        g++;//
                    } else {
                        g = 1;
                    }
                    //reset group 
                    //Limit
                    LLLc = new int[g];//Local Leader Limit
                    GLLc = 0;//Global Leader Limit

                    //Select local leader and global leader
                    LL = selectLocalLeader(spiderMonkey, g);
                    GL = selectGlobalLeader(LL);//Global Leader  

                }//End of Decision Phase Global Leader---------------------------

            }//end of proses iterasi      

            //System.out.println("Global Leader: " + GL.toString());
            bestIndividu = GL;

        }
    }//end of run()
}
