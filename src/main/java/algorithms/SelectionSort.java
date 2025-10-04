package algorithms;

import metrics.PerformanceTracker;

import java.util.Comparator;

/**
 * Baseline implementation of Selection Sort algorithm.
 * Sorts an array in ascending order by repeatedly finding the minimum element
 * from the unsorted part and swapping it with the first unsorted element.
 *
 * @param <T> the type of elements to be sorted, must implement Comparable
 */
public class SelectionSort<T extends Comparable<T>> {

    /**
     * Sorts the given array in ascending order using Selection Sort.
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
            // Find the index of the minimum element in the unsorted part [i..n-1]
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                PerformanceTracker.incrementArrayAccesses(); // Access array[j]
                PerformanceTracker.incrementArrayAccesses(); // Access array[minIndex]
                if (array[j].compareTo(array[minIndex]) < 0) {
                    PerformanceTracker.incrementComparisons();
                    minIndex = j;
                }
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
     * Returns the number of comparisons performed.
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