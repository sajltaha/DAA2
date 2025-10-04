## Selection Sort Implementation - Sailaubekov Talgat SE-2434

## Metrics
- PerformanceTracker: Tracks comparisons, swaps, array accesses, and memory allocations.
- Export to CSV for benchmarking.

## Algorithm
- SelectionSort: Baseline in-place selection sort with performance tracking.
- Supports generic types (T extends Comparable<T>).
- Metrics: comparisons, swaps, array accesses.

## Testing
- Unit tests with JUnit 5 and AssertJ: Edge cases (empty, single, duplicates, sorted/reverse-sorted).
- Property-based: Random arrays compared to Arrays.sort().
- Run: mvn test