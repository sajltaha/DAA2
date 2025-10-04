package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SelectionSortTest {

    private SelectionSort<Integer> sorter;
    private Random random;

    @BeforeEach
    void setUp() {
        sorter = new SelectionSort<>();
        random = new Random(42); // Fixed seed for reproducibility
    }

    @Test
    void shouldThrowExceptionOnNullArray() {
        assertThatThrownBy(() -> sorter.sort(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Array must not be null or empty");
    }

    @Test
    void shouldThrowExceptionOnEmptyArray() {
        assertThatThrownBy(() -> sorter.sort(new Integer[]{}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Array must not be null or empty");
    }

    @Test
    void shouldSortSingleElement() {
        Integer[] array = {5};
        sorter.sort(array);
        assertThat(array).containsExactly(5);
        assertThat(sorter.getComparisons()).isZero();
        assertThat(sorter.getSwaps()).isZero();
        assertThat(sorter.getArrayAccesses()).isEqualTo(0); // No accesses needed
    }

    @Test
    void shouldSortWithDuplicates() {
        Integer[] array = {3, 1, 3, 2};
        Integer[] expected = {1, 2, 3, 3};
        sorter.sort(array);
        assertThat(array).containsExactlyElementsOf(Arrays.asList(expected));
        assertThat(sorter.getComparisons()).isEqualTo(12L); // Full for unsorted n=4 with opt
        assertThat(sorter.getSwaps()).isEqualTo(2L); // 2 swaps due to optimization
    }

    @ParameterizedTest
    @MethodSource("sortedAndReverseSortedArrays")
    void shouldSortSortedAndReverseSortedWithOptimization(String name, Integer[] input, long expectedComparisons, long expectedSwaps) {
        Integer[] original = Arrays.copyOf(input, input.length);
        sorter.sort(original);
        assertThat(original).isSorted();
        assertThat(sorter.getComparisons()).isEqualTo(expectedComparisons);
        assertThat(sorter.getSwaps()).isEqualTo(expectedSwaps);
    }

    static Stream<Arguments> sortedAndReverseSortedArrays() {
        return Stream.of(
                Arguments.of("sorted", new Integer[]{1, 2, 3, 4}, 6L, 0L),   // Optimized: 3 min + 3 adj comp, early break
                Arguments.of("reverse", new Integer[]{4, 3, 2, 1}, 12L, 2L)  // Full: 6+4+2 comp, 2 swaps, early break at i=2
        );
    }

    @Test
    void shouldEarlyTerminateOnNearlySorted() {
        Integer[] array = {1, 3, 2, 4};  // Needs one swap at i=1, then sorted
        sorter.sort(array);
        assertThat(array).containsExactly(1, 2, 3, 4);
        assertThat(sorter.getComparisons()).isEqualTo(12L);  // 6 (i=0) + 4 (i=1) + 2 (i=2 early check)
        assertThat(sorter.getSwaps()).isEqualTo(1L);
    }

    @Test
    void shouldSortRandomArrayAndMatchBuiltIn() {
        // Property-based: test 5 random arrays
        for (int i = 0; i < 5; i++) {
            Integer[] original = generateRandomArray(10);
            Integer[] toSort = Arrays.copyOf(original, original.length);
            Integer[] expected = Arrays.copyOf(original, original.length);
            Arrays.sort(expected);

            sorter.sort(toSort);
            assertThat(toSort).containsExactlyElementsOf(Arrays.asList(expected));
        }
    }

    private Integer[] generateRandomArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100);
        }
        return array;
    }
}