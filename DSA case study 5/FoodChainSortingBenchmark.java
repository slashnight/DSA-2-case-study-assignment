import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class FoodChainSortingBenchmark {

    // ==========================
    // MERGE SORT
    // ==========================

    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    public static void merge(int[] arr, int left, int mid, int right) {

        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++)
            L[i] = arr[left + i];

        for (int j = 0; j < n2; j++)
            R[j] = arr[mid + 1 + j];

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {

            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1)
            arr[k++] = L[i++];

        while (j < n2)
            arr[k++] = R[j++];
    }

    // ==========================
    // QUICK SORT
    // ==========================

    // Last Pivot
    public static void quickSortLast(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partitionLast(arr, low, high);

            quickSortLast(arr, low, pi - 1);
            quickSortLast(arr, pi + 1, high);
        }
    }

    private static int partitionLast(int[] arr, int low, int high) {

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);

        return i + 1;
    }

    // First Pivot
    public static void quickSortFirst(int[] arr, int low, int high) {
        if (low < high) {

            swap(arr, low, high);

            int pi = partitionLast(arr, low, high);

            quickSortFirst(arr, low, pi - 1);
            quickSortFirst(arr, pi + 1, high);
        }
    }

    // Random Pivot
    public static void quickSortRandom(int[] arr, int low, int high) {
        if (low < high) {

            int random = ThreadLocalRandom.current().nextInt(low, high + 1);

            swap(arr, random, high);

            int pi = partitionLast(arr, low, high);

            quickSortRandom(arr, low, pi - 1);
            quickSortRandom(arr, pi + 1, high);
        }
    }

    // Median of Three Pivot
    public static void quickSortMedian(int[] arr, int low, int high) {

        if (low < high) {

            int median = medianOfThree(arr, low, high);

            swap(arr, median, high);

            int pi = partitionLast(arr, low, high);

            quickSortMedian(arr, low, pi - 1);
            quickSortMedian(arr, pi + 1, high);
        }
    }

    private static int medianOfThree(int[] arr, int low, int high) {

        int mid = (low + high) / 2;

        if (arr[low] > arr[mid])
            swap(arr, low, mid);

        if (arr[low] > arr[high])
            swap(arr, low, high);

        if (arr[mid] > arr[high])
            swap(arr, mid, high);

        return mid;
    }

    // ==========================
    // HEAP SORT
    // ==========================

    public static void heapSort(int[] arr) {

        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        for (int i = n - 1; i > 0; i--) {

            swap(arr, 0, i);

            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int root) {

        int largest = root;

        int left = 2 * root + 1;
        int right = 2 * root + 2;

        if (left < n && arr[left] > arr[largest])
            largest = left;

        if (right < n && arr[right] > arr[largest])
            largest = right;

        if (largest != root) {

            swap(arr, root, largest);

            heapify(arr, n, largest);
        }
    }

    // ==========================
    // COUNTING SORT
    // ==========================

    public static void countingSort(int[] arr) {

        int max = Arrays.stream(arr).max().getAsInt();

        int[] count = new int[max + 1];

        for (int value : arr)
            count[value]++;

        for (int i = 1; i < count.length; i++)
            count[i] += count[i - 1];

        int[] output = new int[arr.length];

        for (int i = arr.length - 1; i >= 0; i--) {

            output[count[arr[i]] - 1] = arr[i];

            count[arr[i]]--;
        }

        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    // ==========================
    // RADIX SORT
    // ==========================

    public static void radixSort(int[] arr) {

        int max = Arrays.stream(arr).max().getAsInt();

        for (int exp = 1; max / exp > 0; exp *= 10)
            countingSortDigit(arr, exp);
    }

    private static void countingSortDigit(int[] arr, int exp) {

        int n = arr.length;

        int[] output = new int[n];

        int[] count = new int[10];

        for (int value : arr)
            count[(value / exp) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {

            int digit = (arr[i] / exp) % 10;

            output[count[digit] - 1] = arr[i];

            count[digit]--;
        }

        System.arraycopy(output, 0, arr, 0, n);
    }

    // ==========================
    // BENCHMARKING
    // ==========================

    private static void benchmark(String name, int[] arr) {

        long start = System.nanoTime();

        switch (name) {

            case "Merge":
                mergeSort(arr, 0, arr.length - 1);
                break;

            case "QuickLast":
                quickSortLast(arr, 0, arr.length - 1);
                break;

            case "QuickFirst":
                quickSortFirst(arr, 0, arr.length - 1);
                break;

            case "QuickRandom":
                quickSortRandom(arr, 0, arr.length - 1);
                break;

            case "QuickMedian":
                quickSortMedian(arr, 0, arr.length - 1);
                break;

            case "Heap":
                heapSort(arr);
                break;

            case "Counting":
                countingSort(arr);
                break;

            case "Radix":
                radixSort(arr);
                break;
        }

        long end = System.nanoTime();

        double ms = (end - start) / 1_000_000.0;

        System.out.printf("%-15s : %.3f ms%n", name, ms);
    }

    // ==========================
    // UTILITY METHODS
    // ==========================

    private static void swap(int[] arr, int i, int j) {

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static int[] generateDataset(int size) {

        Random random = new Random();

        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(1000000);
        }

        return arr;
    }

    // ==========================
    // MAIN
    // ==========================

    public static void main(String[] args) {

        int[] sizes = {
                10000,
                50000,
                100000,
                500000,
                1000000
        };

        for (int size : sizes) {

            System.out.println("\n=================================");
            System.out.println("Dataset Size : " + size);
            System.out.println("=================================");

            int[] original = generateDataset(size);

            benchmark("Merge",
                    Arrays.copyOf(original, original.length));

            benchmark("QuickFirst",
                    Arrays.copyOf(original, original.length));

            benchmark("QuickLast",
                    Arrays.copyOf(original, original.length));

            benchmark("QuickRandom",
                    Arrays.copyOf(original, original.length));

            benchmark("QuickMedian",
                    Arrays.copyOf(original, original.length));

            benchmark("Heap",
                    Arrays.copyOf(original, original.length));

            benchmark("Counting",
                    Arrays.copyOf(original, original.length));

            benchmark("Radix",
                    Arrays.copyOf(original, original.length));
        }
    }
}