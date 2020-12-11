package fosalgo;

import java.awt.Point;
import java.util.ArrayList;

public class Swap {

    public static Individu swapOperation(Individu individu, int indexVertex1, int indexVertex2) {
        Individu newIndividu = null;
        if (individu != null
                && individu.getKromosom() != null
                && indexVertex1 >= 0
                && indexVertex2 >= 0
                && indexVertex1 < individu.getKromosom().length - 1
                && indexVertex2 < individu.getKromosom().length - 1
                && indexVertex1 != indexVertex2) {

            newIndividu = individu.clone();
            int[] kromosom = newIndividu.getKromosom();

            //SWAP OPERATION    
            int temp = kromosom[indexVertex1];
            kromosom[indexVertex1] = kromosom[indexVertex2];
            kromosom[indexVertex2] = temp;

            if (indexVertex1 == 0 || indexVertex2 == 0) {
                kromosom[kromosom.length - 1] = kromosom[0];
            }
        }
        return newIndividu;
    }

    public static Individu swapSequence(Individu individu, int[][] swapOperators) {
        Individu newIndividu = null;
        if (individu != null && swapOperators != null) {
            newIndividu = individu.clone();
            for (int i = 0; i < swapOperators.length; i++) {
                int indexVertex1 = swapOperators[i][0];
                int indexVertex2 = swapOperators[i][1];
                newIndividu = swapOperation(newIndividu, indexVertex1, indexVertex2);
            }
        }
        return newIndividu;
    }

    public static Individu bestSwap(Individu individu, int[][] swapOperators) {
        Individu best = null;
        if (individu != null) {
            if (swapOperators != null) {
                Individu newIndividu = individu.clone();
                double bestFitness = -1;
                for (int i = 0; i < swapOperators.length; i++) {
                    int indexVertex1 = swapOperators[i][0];
                    int indexVertex2 = swapOperators[i][1];
                    newIndividu = swapOperation(newIndividu, indexVertex1, indexVertex2);
                    newIndividu.hitungNilaiFitness();
                    if (newIndividu.getNilaiFitness() > bestFitness) {
                        best = newIndividu.clone();
                        bestFitness = newIndividu.getNilaiFitness();
                    }
                }
            } else {
                best = individu.clone();
            }
        }
        return best;
    }

    public static Individu add(Individu individu, int[][] swapOperators) {
        return bestSwap(individu, swapOperators);
    }

    public static int[][] subtract(Individu individu1, Individu individu2) {
        int[][] swapOperators = null;
        try {
            ArrayList<Point> listSwapOperator = new ArrayList<Point>();
            int[] kromosom1 = individu1.getKromosom().clone();
            int[] kromosom2 = individu2.getKromosom();
            for (int i = 0; i < kromosom1.length; i++) {
                int key = kromosom2[i];
                if (key != kromosom1[i]) {
                    for (int j = 1 + i; j < kromosom1.length; j++) {
                        if (key == kromosom1[j]) {
                            //swap
                            int temp = kromosom1[i];
                            kromosom1[i] = kromosom1[j];
                            kromosom1[j] = temp;

                            //save swap operator
                            listSwapOperator.add(new Point(i, j));

                            //break setelah menyimpan operator swap
                            break;
                        }
                    }
                }
            }

            //set output
            if (listSwapOperator.size() > 0) {
                int n = listSwapOperator.size();
                swapOperators = new int[n][2];
                for (int i = 0; i < n; i++) {
                    Point p = listSwapOperator.get(i);
                    swapOperators[i][0] = (int) p.getX();
                    swapOperators[i][1] = (int) p.getY();
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return swapOperators;
    }

    public static String swapOperatorsToString(int[][] swapOperators) {
        String result = "NULL";
        if (swapOperators != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            for (int i = 0; i < swapOperators.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("SO(" + swapOperators[i][0] + "," + swapOperators[i][1] + ")");
            }
            sb.append("}");
            result = sb.toString();
        }
        return result;
    }

    public static int[][] callBasicSwapSequence(int[][] swapOperators, int panjangKromosom) {
        int[][] basicSS = null;
        try {
            if (panjangKromosom > 1 && swapOperators != null) {
                int[] kromosom1 = new int[panjangKromosom];
                for (int i = 0; i < kromosom1.length; i++) {
                    kromosom1[i] = i;
                }
                Individu individu1 = new Individu(kromosom1);
                Individu individu2 = swapSequence(individu1, swapOperators);
                basicSS = subtract(individu1, individu2);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return basicSS;
    }

    public static int[][] mergeSwapSequence(int[][] swapOperators1, int[][] swapOperators2) {
        int[][] result = null;
        int size = 0;
        if (swapOperators1 != null) {
            size += swapOperators1.length;
        }
        if (swapOperators2 != null) {
            size += swapOperators2.length;
        }
        if (size > 0) {
            result = new int[size][2];
            int k = 0;
            if (swapOperators1 != null) {
                for (int i = 0; i < swapOperators1.length; i++) {
                    result[k][0] = swapOperators1[i][0];
                    result[k][1] = swapOperators1[i][1];
                    k++;
                }
            }
            if (swapOperators2 != null) {
                for (int i = 0; i < swapOperators2.length; i++) {
                    result[k][0] = swapOperators2[i][0];
                    result[k][1] = swapOperators2[i][1];
                    k++;
                }
            }
        }
        return result;
    }

}
