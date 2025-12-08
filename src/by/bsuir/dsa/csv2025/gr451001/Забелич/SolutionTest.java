package by.bsuir.dsa.csv2025.gr451001.Забелич;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SolutionTest {

    // Вынесем логику в отдельный метод для тестов
    private int run(int[] arr) {
        int minLeft = arr[0];
        int maxDiff = 0;

        for (int j = 1; j < arr.length; j++) {
            maxDiff = Math.max(maxDiff, arr[j] - minLeft);
            minLeft = Math.min(minLeft, arr[j]);
        }
        return maxDiff;
    }

    @Test
    public void testIncreasing() {
        // массив строго возрастающий
        assertEquals(9, run(new int[]{1, 2, 3, 10}));
    }

    @Test
    public void testDecreasing() {
        // массив строго убывающий → разница всегда 0
        assertEquals(0, run(new int[]{10, 9, 8, 7}));
    }

    @Test
    public void testMixed() {
        // минимум слева = 2, максимум справа = 11 → разница 9
        assertEquals(10, run(new int[]{5, 2, 6, 1, 11}));
    }

    @Test
    public void testSingleElement() {
        // один элемент → разницы нет
        assertEquals(0, run(new int[]{7}));
    }

    @Test
    public void testTwoElements() {
        // разница = 8 - 3 = 5
        assertEquals(5, run(new int[]{3, 8}));
    }
}
