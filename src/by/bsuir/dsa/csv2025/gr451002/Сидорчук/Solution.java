package by.bsuir.dsa.csv2025.gr451002.Сидорчук;

import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение размерности матрицы
        int n = scanner.nextInt();

        // Чтение элементов матрицы
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }

        scanner.close();

        // Обработка матрицы
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);

        // Вывод результата
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) {
                System.out.print(" ");
            }
        }
    }

    // Rotates matrix 90 degrees clockwise
    public static int[][] rotateMatrix90(int[][] matrix) {
        int n = matrix.length;
        int[][] rotated = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }

        return rotated;
    }

    // Traverses matrix in clockwise spiral pattern
    public static int[] getSpiralArray(int[][] matrix) {
        int n = matrix.length;
        int[] result = new int[n * n];

        int top = 0, bottom = n - 1;
        int left = 0, right = n - 1;
        int index = 0;

        while (top <= bottom && left <= right) {
            // Top row (left to right)
            for (int i = left; i <= right; i++) {
                result[index++] = matrix[top][i];
            }
            top++;

            // Right column (top to bottom)
            for (int i = top; i <= bottom; i++) {
                result[index++] = matrix[i][right];
            }
            right--;

            // Bottom row (right to left)
            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    result[index++] = matrix[bottom][i];
                }
                bottom--;
            }

            // Left column (bottom to top)
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    result[index++] = matrix[i][left];
                }
                left++;
            }
        }

        return result;
    }

    // для преобразования массива в строку
    public static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    @Test
    public void testSimpleSequentialElem() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        int[] expected = {7, 4, 1, 2, 3, 6, 9, 8, 5};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("Test 1 failed", expected, result);
    }

    @Test
    public void testMatrix1x1() {
        int[][] matrix = {{5}};
        int[] expected = {5};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("Test 2 failed", expected, result);
    }

    @Test
    public void testSpiralMatrix() {
        int[][] matrix = {
                {2, 3},
                {1, 4}
        };
        int[] expected = {1, 2, 3, 4};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("Test 4 failed", expected, result);
    }

    @Test
    public void testSameElementsInMatrix() {
        int[][] matrix = {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        };
        int[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("Test 8 failed", expected, result);
    }

    @Test
    public void testSignedSequenceAndZeros() {
        int[][] matrix = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-3, 0, 3}
        };
        int[] expected = {-3, -2, -1, 0, 1, 2, 3, 0, 0};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("Test 9 failed", expected, result);
    }

    @Test
    public void testSameElemInRow() {
        int[][] matrix = {
                {1, 1, 1},
                {2, 2, 2},
                {3, 3, 3}
        };
        int[] expected = {3, 2, 1, 1, 1, 2, 3, 3, 2};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("Test 10 failed", expected, result);
    }

    @Test
    public void testNegativeNumbers() {
        int[][] matrix = {
                {-5, -10},
                {-15, -20}
        };
        int[] expected = {-15, -5, -10, -20};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("Negative numbers test failed", expected, result);
    }

    @Test
    public void testSequentialNumbers() {
        int[][] matrix = {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
        };
        int[] expected = {21, 16, 11, 6, 1, 2, 3, 4, 5, 10, 15, 20, 25, 24, 23, 22, 17, 12, 7, 8, 9, 14, 19, 18, 13};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("5x5 sequential numbers test failed", expected, result);
    }

    @Test
    public void testAlternatingPattern() {
        int[][] matrix = {
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}
        };
        int[] expected = {1, 0, 1, 0, 1, 0, 1, 0, 1};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("Alternating pattern test failed", expected, result);
    }

    @Test
    public void testMixedPositiveNegative() {
        int[][] matrix = {
                {1, -1, 2},
                {-2, 0, -3},
                {3, -4, 4}
        };
        int[] expected = {3, -2, 1, -1, 2, -3, 4, -4, 0};
        int[][] rotated = rotateMatrix90(matrix);
        int[] result = getSpiralArray(rotated);
        assertArrayEquals("Mixed positive-negative test failed", expected, result);
    }
}