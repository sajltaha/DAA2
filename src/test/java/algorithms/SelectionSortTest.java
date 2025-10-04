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

public class SelectionSortTest {

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
    }

    @ParameterizedTest
    @MethodSource("sortedAndReverseSortedArrays")
    void shouldSortSortedAndReverseSorted(String name, Integer[] input, long expectedComparisons, long expectedSwaps) {
        Integer[] original = Arrays.copyOf(input, input.length);
        sorter.sort(original);
        assertThat(original).isSorted();
        assertThat(sorter.getComparisons()).isEqualTo(expectedComparisons);
        assertThat(sorter.getSwaps()).isEqualTo(expectedSwaps);
    }

    static Stream<Arguments> sortedAndReverseSortedArrays() {
        return Stream.of(
                Arguments.of("sorted", new Integer[]{1, 2, 3, 4}, 6L, 0L),
                Arguments.of("reverse", new Integer[]{4, 3, 2, 1}, 6L, 2L) // Правильно для n=4
        );
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