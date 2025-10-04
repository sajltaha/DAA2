package metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PerformanceTracker for tracking key operations in algorithms.
 * Supports comparisons, swaps, array accesses, and memory allocations.
 * Thread-safe using AtomicLong.
 */
public class PerformanceTracker {
    private static final AtomicLong comparisons = new AtomicLong(0);
    private static final AtomicLong swaps = new AtomicLong(0);
    private static final AtomicLong arrayAccesses = new AtomicLong(0);
    private static final AtomicLong memoryAllocations = new AtomicLong(0); // Approximate, e.g., for new objects

    /**
     * Increment comparison count.
     */
    public static void incrementComparisons() {
        comparisons.incrementAndGet();
    }

    /**
     * Increment swap count.
     */
    public static void incrementSwaps() {
        swaps.incrementAndGet();
    }

    /**
     * Increment array access count.
     */
    public static void incrementArrayAccesses() {
        arrayAccesses.incrementAndGet();
    }

    /**
     * Increment memory allocation count (manual call when allocating).
     */
    public static void incrementMemoryAllocations() {
        memoryAllocations.incrementAndGet();
    }

    /**
     * Get current comparison count.
     */
    public static long getComparisons() {
        return comparisons.get();
    }

    /**
     * Get current swap count.
     */
    public static long getSwaps() {
        return swaps.get();
    }

    /**
     * Get current array access count.
     */
    public static long getArrayAccesses() {
        return arrayAccesses.get();
    }

    /**
     * Get current memory allocation count.
     */
    public static long getMemoryAllocations() {
        return memoryAllocations.get();
    }

    /**
     * Reset all counters to zero (call before each run).
     */
    public static void reset() {
        comparisons.set(0);
        swaps.set(0);
        arrayAccesses.set(0);
        memoryAllocations.set(0);
    }

    /**
     * Export metrics to CSV file.
     * Format: timestamp,comparisons,swaps,arrayAccesses,memoryAllocations
     * @param filename CSV file path
     * @throws IOException if write fails
     */
    public static void exportToCSV(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename, true)) { // Append mode
            String line = System.currentTimeMillis() + "," +
                    getComparisons() + "," +
                    getSwaps() + "," +
                    getArrayAccesses() + "," +
                    getMemoryAllocations() + "\n";
            writer.write(line);
        }
    }
}