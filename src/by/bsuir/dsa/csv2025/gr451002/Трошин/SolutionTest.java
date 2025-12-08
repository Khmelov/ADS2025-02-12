package by.bsuir.dsa.csv2025.gr451002.Трошин;
// Тестовые наборы из файла SolutionTest
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SolutionTest {

    private String input;
    private int expected;

    public SolutionTest(String input, int expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameters(name = "Тест {index}: строка \"{0}\" -> {1} палиндромов")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"", 0},
                {"a", 1},
                {"ab", 2},
                {"aa", 3},
                {"abc", 3},
                {"aaa", 6},
                {"aba", 4},
                {"abba", 6},
                {"aab", 4},
                {"racecar", 10}
        });
    }

    @Test
    public void testCountSolution() {
        assertEquals("Для строки: \"" + input + "\"",
                expected,
                Solution.countSolution(input));
    }
}