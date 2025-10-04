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

## CLI
- BenchmarkRunner: Run benchmarks with configurable n, type (random/sorted/reverse/nearly), repeats.
- Usage: mvn exec:java -Dexec.args="-n 1000 -t reverse -r 5 -o results.csv"
- Generates CSV with metrics.