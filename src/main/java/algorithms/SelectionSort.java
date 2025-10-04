package algorithms;

import metrics.PerformanceTracker;

import java.util.Comparator;

/**
 * Optimized implementation of Selection Sort with early termination.
 * Sorts an array in ascending order by repeatedly finding the minimum element
 * from the unsorted part and swapping it with the first unsorted element.
 * Optimization: During min search, check if unsorted subarray [i..n-1] is already sorted
 * by verifying adjacent elements. If yes, terminate early.
 *
 * @param <T> the type of elements to be sorted, must implement Comparable
 */
public class SelectionSort<T extends Comparable<T>> {

    /**
     * Sorts the given array in ascending order using optimized Selection Sort.
     * Tracks performance metrics using PerformanceTracker.
     *
     * @param array the array to sort (must not be null or empty)
     * @throws IllegalArgumentException if array is null or empty
     */
    public void sort(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }

        PerformanceTracker.reset(); // Reset metrics before sorting

        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            // Find min in [i..n-1] and check if subarray is sorted
            int minIndex = i;
            boolean isSorted = true; // Assume subarray [i..n-1] is sorted
            for (int j = i + 1; j < n; j++) {
                // Access for min check
                PerformanceTracker.incrementArrayAccesses(); // array[j]
                PerformanceTracker.incrementArrayAccesses(); // array[minIndex]
                PerformanceTracker.incrementComparisons(); // compareTo for min
                if (array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }

                // Early termination check: adjacent comparison in subarray
                if (j > i) { // For j == i+1, j-1 == i
                    PerformanceTracker.incrementArrayAccesses(); // array[j-1]
                    PerformanceTracker.incrementArrayAccesses(); // array[j] (already counted, but for clarity)
                    PerformanceTracker.incrementComparisons(); // adjacent compare
                    if (array[j].compareTo(array[j - 1]) < 0) {
                        isSorted = false;
                    }
                }
            }

            // Early termination: if subarray [i..n-1] is sorted, done
            if (isSorted) {
                break;
            }

            // Swap if necessary
            if (minIndex != i) {
                swap(array, i, minIndex);
                PerformanceTracker.incrementSwaps();
            }
        }
    }

    /**
     * Swaps two elements in the array.
     * @param array the array
     * @param i first index
     * @param j second index
     */
    private void swap(T[] array, int i, int j) {
        PerformanceTracker.incrementArrayAccesses(); // Access array[i]
        PerformanceTracker.incrementArrayAccesses(); // Access array[j]
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Returns the number of comparisons performed (includes min and adjacent checks).
     * @return comparisons count
     */
    public long getComparisons() {
        return PerformanceTracker.getComparisons();
    }

    /**
     * Returns the number of swaps performed.
     * @return swaps count
     */
    public long getSwaps() {
        return PerformanceTracker.getSwaps();
    }

    /**
     * Returns the number of array accesses performed.
     * @return array accesses count
     */
    public long getArrayAccesses() {
        return PerformanceTracker.getArrayAccesses();
    }
}