package by.bsuir.dsa.csv2025.gr451003.Кужик;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] firstLine = br.readLine().split(" ");
        int N = Integer.parseInt(firstLine[0]);
        int K = Integer.parseInt(firstLine[1]);

        int[] signal = new int[N];
        String[] secondLine = br.readLine().split(" ");
        for (int i = 0; i < N; i++) {
            signal[i] = Integer.parseInt(secondLine[i]);
        }
        int[] mins = slidingWindowMin(signal, K);
        int[] maxs = slidingWindowMax(signal, K);

        StringBuilder minOutput = new StringBuilder();
        StringBuilder maxOutput = new StringBuilder();

        for (int i = 0; i < mins.length; i++) {
            minOutput.append(mins[i]);
            maxOutput.append(maxs[i]);
            if (i < mins.length - 1) {
                minOutput.append(" ");
                maxOutput.append(" ");
            }
        }

        System.out.println(minOutput);
        System.out.println(maxOutput);
    }

    private static int[] slidingWindowMin(int[] arr, int k) {
        int n = arr.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }

            while (!deque.isEmpty() && arr[deque.peekLast()] >= arr[i]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            if (i >= k - 1) {
                result[i - k + 1] = arr[deque.peekFirst()];
            }
        }

        return result;
    }

    private static int[] slidingWindowMax(int[] arr, int k) {
        int n = arr.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }

            while (!deque.isEmpty() && arr[deque.peekLast()] <= arr[i]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            if (i >= k - 1) {
                result[i - k + 1] = arr[deque.peekFirst()];
            }
        }
        return result;
    }

    @Test
    public void testAllCases() throws IOException {
        testCase("10 5", "1 2 3 4 5 4 3 2 1 0", "1 2 3 2 1 0", "5 5 5 5 5 4");
        testCase("5 2", "5 4 3 2 1", "4 3 2 1", "5 4 3 2");
        testCase("5 2", "1 2 3 4 5", "1 2 3 4", "2 3 4 5");
        testCase("6 4", "3 1 4 1 5 9", "1 1 1", "4 5 9");
        testCase("4 4", "10 20 30 40", "10", "40");
        testCase("8 2", "-1 3 -2 5 -3 7 -4 9", "-1 -2 -2 -3 -3 -4 -4", "3 3 5 5 7 7 9");
        testCase("6 3", "15 -5 2 5 7 8", "-5 -5 2 5", "15 5 7 8");
        testCase("3 1", "7 8 9", "7 8 9", "7 8 9");
        testCase("6 3", "0 0 0 0 0 0", "0 0 0 0", "0 0 0 0");
        testCase("9 4", "9 8 7 6 5 4 3 2 1", "6 5 4 3 2 1", "9 8 7 6 5 4");
    }

    private void testCase(String firstLine, String secondLine, String expectedMin, String expectedMax) throws IOException {
        String input = firstLine + "\n" + secondLine;
        ByteArrayInputStream testInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(testInput);

        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(testOutput));

        try {
            Solution.main(new String[]{});

            String output = testOutput.toString();
            String normalizedOutput = output.replace("\r\n", "\n").replace("\r", "\n");
            String[] lines = normalizedOutput.split("\n");

            String actualMin = lines[0].trim();
            String actualMax = lines[1].trim();

            assertEquals("Минимумы не совпадают", expectedMin, actualMin);
            assertEquals("Максимумы не совпадают", expectedMax, actualMax);

        } finally {
            System.setOut(originalOut);
            System.setIn(System.in);
        }
    }
}