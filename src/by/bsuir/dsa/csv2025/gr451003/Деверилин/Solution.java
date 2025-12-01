package by.bsuir.dsa.csv2025.gr451003.Деверилин;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution {
    static class Sorter extends Thread {
        int[] arr;

        Sorter(int[] arr) {
            this.arr = arr;
        }

        public void run() {
            Arrays.sort(arr);
        }
    }

    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        while (i < left.length) {
            result[k++] = left[i++];
        }
        while (j < right.length) {
            result[k++] = right[j++];
        }
        return result;
    }

    private static int[] mergeAll(List<int[]> sortedParts) {
        while (sortedParts.size() > 1) {
            List<int[]> newParts = new ArrayList<>();
            for (int i = 0; i < sortedParts.size(); i += 2) {
                if (i + 1 < sortedParts.size()) {
                    newParts.add(merge(sortedParts.get(i), sortedParts.get(i + 1)));
                } else {
                    newParts.add(sortedParts.get(i));
                }
            }
            sortedParts = newParts;
        }
        return sortedParts.get(0);
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int K = scanner.nextInt();
        int N = scanner.nextInt();
        int[] array = new int[K];
        for (int i = 0; i < K; i++) {
            array[i] = scanner.nextInt();
        }

        List<int[]> parts = new ArrayList<>();
        List<Sorter> threads = new ArrayList<>();
        int partSize = K / N;
        int remainder = K % N;
        int start = 0;
        for (int i = 0; i < N; i++) {
            int end = start + partSize + (i < remainder ? 1 : 0);
            int[] part = Arrays.copyOfRange(array, start, end);
            parts.add(part);
            Sorter sorter = new Sorter(part);
            threads.add(sorter);
            sorter.start();
            start = end;
        }

        for (Sorter thread : threads) {
            thread.join();
        }

        int[] sortedArray = mergeAll(parts);

        for (int i = 0; i < sortedArray.length; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sortedArray[i]);
        }
        System.out.println();
    }

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setup() {
        System.setOut(new PrintStream(outputStreamCaptor, true, StandardCharsets.UTF_8));
    }

    @After
    public void returnAllStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private String runTest(String input) throws InterruptedException {
        outputStreamCaptor.reset();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        Solution.main(null);
        return outputStreamCaptor.toString(StandardCharsets.UTF_8).trim();
    }

    @Test
    public void test1() throws InterruptedException {
        String input =
                """
                3 1 3 1 2
                """;
        assertEquals("Test Case 1", "1 2 3", runTest(input));
    }

    @Test
    public void test2() throws InterruptedException {
        String input =
                """
                4 2 4 3 2 1
                """;
        assertEquals("Test Case 2", "1 2 3 4", runTest(input));
    }

    @Test
    public void test3() throws InterruptedException {
        String input =
                """
                5 3 5 4 3 2 1
                """;
        assertEquals("Test Case 3", "1 2 3 4 5", runTest(input));
    }

    @Test
    public void test4() throws InterruptedException {
        String input =
                """
                6 2 1 3 5 2 4 6
                """;
        assertEquals("Test Case 4", "1 2 3 4 5 6", runTest(input));
    }

    @Test
    public void test5() throws InterruptedException {
        String input =
                """
                7 4 7 6 5 4 3 2 1
                """;
        assertEquals("Test Case 5", "1 2 3 4 5 6 7", runTest(input));
    }

    @Test
    public void test6() throws InterruptedException {
        String input =
                """
                8 3 8 7 6 5 4 3 2 1
                """;
        assertEquals("Test Case 6", "1 2 3 4 5 6 7 8", runTest(input));
    }

    @Test
    public void test7() throws InterruptedException {
        String input =
                """
                2 1 2 1
                """;
        assertEquals("Test Case 7", "1 2", runTest(input));
    }

    @Test
    public void test8() throws InterruptedException {
        String input =
                """
                9 5 9 8 7 6 5 4 3 2 1
                """;
        assertEquals("Test Case 8", "1 2 3 4 5 6 7 8 9", runTest(input));
    }

    @Test
    public void test9() throws InterruptedException {
        String input =
                """
                10 2 10 9 8 7 6 5 4 3 2 1
                """;
        assertEquals("Test Case 9", "1 2 3 4 5 6 7 8 9 10", runTest(input));
    }

    @Test
    public void test10() throws InterruptedException {
        String input =
                """
                1 1 42
                """;
        assertEquals("Test Case 10", "42", runTest(input));
    }
}