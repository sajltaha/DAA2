package cli;

import algorithms.SelectionSort;
import metrics.PerformanceTracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * CLI Benchmark Runner for Selection Sort.
 * Usage: java -cp target/classes cli.BenchmarkRunner -n 100 -t random -r 10 -o benchmarks.csv
 * Supports input types: random, sorted, reverse, nearly (90% sorted with random swaps).
 * Exports to CSV: timestamp,time_ns,comparisons,swaps,arrayAccesses,memoryAllocations
 */
public class BenchmarkRunner {
    private static final Random random = new Random(42); // Fixed seed for reproducibility

    public static void main(String[] args) {
        // Manual arg parsing
        Map<String, String> params = parseArgs(args);
        int n = Integer.parseInt(params.getOrDefault("n", "100"));
        String type = params.getOrDefault("t", "random");
        int repeats = Integer.parseInt(params.getOrDefault("r", "1"));
        String output = params.getOrDefault("o", "benchmarks.csv");

        // Help if -h or --help
        if (params.containsKey("h") || params.containsKey("help")) {
            System.out.println("Usage: java -cp target/classes cli.BenchmarkRunner [-n <size>] [-t <type>] [-r <repeats>] [-o <output>] [-h]");
            System.out.println("  -n: Input size (default 100)");
            System.out.println("  -t: Type (random/sorted/reverse/nearly, default random)");
            System.out.println("  -r: Repeats (default 1)");
            System.out.println("  -o: CSV output (default benchmarks.csv)");
            System.exit(0);
        }

        // Initialize CSV header if file doesn't exist
        initializeCSV(output);

        long totalTime = 0;
        long totalComparisons = 0;
        long totalSwaps = 0;
        long totalAccesses = 0;

        for (int rep = 0; rep < repeats; rep++) {
            Integer[] array = generateArray(n, type);
            Integer[] copy = Arrays.copyOf(array, n); // For verification

            SelectionSort<Integer> sorter = new SelectionSort<>();
            PerformanceTracker.reset(); // Reset metrics
            long start = System.nanoTime();
            sorter.sort(array);
            long end = System.nanoTime();
            long timeNs = end - start;

            // Verify sorted
            Arrays.sort(copy);
            if (!Arrays.equals(array, copy)) {
                System.err.println("Sort failed for repeat " + rep + " (type: " + type + ")");
                System.exit(1);
            }

            totalTime += timeNs;
            totalComparisons += sorter.getComparisons();
            totalSwaps += sorter.getSwaps();
            totalAccesses += sorter.getArrayAccesses();

            // Export per run to CSV (includes time)
            exportToCSV(output, timeNs, sorter.getComparisons(), sorter.getSwaps(), sorter.getArrayAccesses());
        }

        // Average results
        double avgTimeMs = (totalTime / 1_000_000.0) / repeats;
        double avgComp = totalComparisons / (double) repeats;
        double avgSwaps = totalSwaps / (double) repeats;
        double avgAccesses = totalAccesses / (double) repeats;

        System.out.printf("Benchmark Results (n=%d, type=%s, repeats=%d):\n", n, type, repeats);
        System.out.printf("Average Time: %.2f ms\n", avgTimeMs);
        System.out.printf("Average Comparisons: %.0f\n", avgComp);
        System.out.printf("Average Swaps: %.0f\n", avgSwaps);
        System.out.printf("Average Array Accesses: %.0f\n", avgAccesses);
        System.out.println("Data exported to " + output);
    }

    /**
     * Simple manual arg parsing: -key value
     */
    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                String key = args[i].substring(1); // Remove -
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    params.put(key, args[i + 1]);
                    i++; // Skip value
                } else {
                    params.put(key, "true"); // Flag without value
                }
            }
        }
        return params;
    }

    /**
     * Generate array based on type.
     * - random: 0 to 100*n-1
     * - sorted: 0 to n-1
     * - reverse: n-1 to 0
     * - nearly: sorted + ~10% random swaps
     */
    private static Integer[] generateArray(int n, String type) {
        Integer[] array = new Integer[n];
        if ("nearly".equals(type)) {
            // First fill as sorted
            for (int i = 0; i < n; i++) {
                array[i] = i;
            }
            // Add ~10% random swaps for nearly-sorted
            int numSwaps = (int) (n * 0.1);
            for (int s = 0; s < numSwaps; s++) {
                int idx1 = random.nextInt(n);
                int idx2 = random.nextInt(n);
                int temp = array[idx1];
                array[idx1] = array[idx2];
                array[idx2] = temp;
            }
        } else {
            // Traditional switch for Java 11
            for (int i = 0; i < n; i++) {
                switch (type) {
                    case "sorted":
                        array[i] = i;
                        break;
                    case "reverse":
                        array[i] = n - 1 - i;
                        break;
                    default: // random
                        array[i] = random.nextInt(100 * n);
                        break;
                }
            }
        }
        return array;
    }

    /**
     * Initialize CSV with header (if file doesn't exist).
     */
    private static void initializeCSV(String filename) {
        java.io.File file = new java.io.File(filename);
        if (!file.exists() || file.length() == 0) {
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("timestamp,time_ns,comparisons,swaps,arrayAccesses,memoryAllocations\n");
            } catch (IOException e) {
                System.err.println("Failed to initialize CSV: " + e.getMessage());
            }
        }
    }

    /**
     * Export single run metrics to CSV (includes time).
     */
    private static void exportToCSV(String filename, long timeNs, long comp, long swaps, long accesses) {
        try (FileWriter writer = new FileWriter(filename, true)) { // Append
            String line = System.currentTimeMillis() + "," +
                    timeNs + "," +
                    comp + "," +
                    swaps + "," +
                    accesses + "," +
                    PerformanceTracker.getMemoryAllocations() + "\n";
            writer.write(line);
        } catch (IOException e) {
            System.err.println("Failed to export to CSV: " + e.getMessage());
        }
    }
}