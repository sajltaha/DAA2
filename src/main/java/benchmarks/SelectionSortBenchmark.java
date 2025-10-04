package benchmarks;

import algorithms.SelectionSort;
import metrics.PerformanceTracker;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class SelectionSortBenchmark {

    @Param({"100", "1000", "10000"})
    private int size;

    private Integer[] array;

    @Setup(Level.Trial)
    public void setup() {
        array = new Integer[size];
    }

    @Benchmark
    public void sortRandom() {
        generateRandom();
        PerformanceTracker.reset();
        new SelectionSort<Integer>().sort(array);
    }

    @Benchmark
    public void sortSorted() {
        generateSorted();
        PerformanceTracker.reset();
        new SelectionSort<Integer>().sort(array);
    }

    @Benchmark
    public void sortReverse() {
        generateReverse();
        PerformanceTracker.reset();
        new SelectionSort<Integer>().sort(array);
    }

    @Benchmark
    public void sortNearly() {
        generateNearly();
        PerformanceTracker.reset();
        new SelectionSort<Integer>().sort(array);
    }

    private void generateRandom() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 100);
        }
    }

    private void generateSorted() {
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
    }

    private void generateReverse() {
        for (int i = 0; i < size; i++) {
            array[i] = size - 1 - i;
        }
    }

    private void generateNearly() {
        generateSorted();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int numSwaps = size / 10;
        for (int s = 0; s < numSwaps; s++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
    }
}