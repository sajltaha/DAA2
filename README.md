# Assignment 2: Selection Sort Implementation

Implementation of Selection Sort with early termination optimizations for nearly-sorted data.
Includes performance tracking, unit tests, CLI benchmark runner, and complexity analysis.

## Repository Structure
- `src/main/java/algorithms/SelectionSort.java`: Core sorting algorithm (optimized).
- `src/main/java/metrics/PerformanceTracker.java`: Tracks comparisons, swaps, array accesses, memory allocations.
- `src/main/java/cli/BenchmarkRunner.java`: CLI for running benchmarks on different input sizes/types.
- `src/test/java/algorithms/SelectionSortTest.java`: Unit tests with edge cases and property-based testing.
- `pom.xml`: Maven configuration.

## Building and Running

### Prerequisites
- Java 11+
- Maven 3.6+

### Build

### Run Unit Tests
- Covers edge cases (empty, single, duplicates, sorted/reverse-sorted), random inputs, and optimization validation.

### Run Benchmarks (CLI)
Use `java` to run the benchmark runner:
- `-n <size>`: Input size (e.g., 100, 1000, 10000).
- `-t <type>`: Input type (random, sorted, reverse, nearly).
- `-r <repeats>`: Number of runs for average.
- `-o <output>`: CSV file for metrics (timestamp, time_ns, comparisons, swaps, accesses, allocations).
- `-h`: Show help.
- Example output: Average time, comparisons, swaps, accesses printed; data exported to CSV.

### Generate Plots
Use CSV data with tools like Excel/Python (matplotlib) to plot time vs n. Example sizes: n=100,1000,10000,100000.

## Algorithm Overview
**Selection Sort**: Iteratively finds the minimum in the unsorted subarray and swaps it to the front.
- **Baseline**: Full O(n²) passes.
- **Optimization**: During min search, check adjacent elements in subarray [i..n-1]. If sorted (no inversions), break early.
- Suitable for nearly-sorted data (e.g., early exit after few passes).

## Complexity Analysis

| Case          | Baseline Time | Optimized Time | Space |
|---------------|---------------|----------------|-------|
| Best (sorted) | Ω(n)          | Θ(n)           | O(1)  |
| Average       | Θ(n²)         | Θ(n²)          | O(1)  |
| Worst (reverse) | O(n²)      | O(n²)          | O(1)  |

- **Time Derivation**:
    - Baseline: n-1 passes, each with ~n/2 comparisons → Θ(n²) comparisons/swaps.
    - Optimized: Adds ~n adjacent checks per pass, but early break reduces passes for best/nearly cases → Ω(n) best, still O(n²) worst.
    - Recurrence: T(n) = T(n-1) + O(n), solves to O(n²).
- **Space**: In-place, auxiliary O(1) (temp variable in swap).
- Empirical: For sorted n=100, optimized ~200 comparisons vs baseline ~4950.

## Git Workflow
- Branches: main (releases), feature/* (develop).
- Commits: Conventional (feat, test, fix, docs).

## Next Steps
- Run benchmarks for report plots.
- Peer review partner's code.
- Submit analysis-report.pdf.

## JMH Benchmarks
- Accurate microbenchmarks using JMH.
- Run: mvn clean package; java -jar target/benchmarks.jar
- Outputs: Average time (ns/op) for random/sorted/reverse/nearly on n=100,1000,10000.
- Example: For sorted n=100: ~200 ns/op (optimized).