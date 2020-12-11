package fosalgo;

public class Quick{
    
    public static void printArray(double arr[][]) {
        int n = arr.length;
        for (int i = 0; i < n; ++i) {
            System.out.println(arr[i][0] + " " + ((int)arr[i][1]));
        }
        System.out.println();
    }
    
    public static double[][] sort(double[][] arr){
        int n = arr.length;
        sort(arr, 0, n - 1);
        return arr;
    }

    private static int partition(double arr[][], int low, int high) {
        double pivot = arr[high][0];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j][0] > pivot) {
                i++;
                // swap arr[i] and arr[j] 
                double temp0 = arr[i][0];
                double temp1 = arr[i][1];
                arr[i][0] = arr[j][0];
                arr[i][1] = arr[j][1];
                arr[j][0] = temp0;
                arr[j][1] = temp1;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot) 
        double temp0 = arr[i + 1][0];
        double temp1 = arr[i + 1][1];
        arr[i + 1][0] = arr[high][0];
        arr[i + 1][1] = arr[high][1];
        arr[high][0] = temp0;
        arr[high][1] = temp1;

        return i + 1;
    }

    private static void sort(double arr[][], int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            sort(arr, low, pi - 1);
            sort(arr, pi + 1, high);
        }
    }    
}
