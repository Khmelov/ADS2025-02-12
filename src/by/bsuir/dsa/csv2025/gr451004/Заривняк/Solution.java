package by.bsuir.dsa.csv2025.gr451004.Заривняк;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class Solution {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        outContent.reset();
    }

    private static boolean canDistribute(int[] tasks, int m, int maxLoad) {
        Integer[] sorted = Arrays.stream(tasks).boxed().toArray(Integer[]::new);
        Arrays.sort(sorted, Collections.reverseOrder());

        int[] loads = new int[m];

        for (int task : sorted) {
            int best = 0;
            for (int i = 1; i < m; i++) {
                if (loads[i] < loads[best]) {
                    best = i;
                }
            }

            if (loads[best] + task > maxLoad) {
                return false;
            }
            loads[best] += task;
        }
        return true;
    }

    public static int minMaxLoad(int[] tasks, int m) {
        int left = Arrays.stream(tasks).max().orElse(0);
        int right = Arrays.stream(tasks).sum();

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canDistribute(tasks, m, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String[] input = scan.nextLine().split("\\s+");

        int index = 0;
        int n = Integer.parseInt(input[index++]);
        int[] tasks = new int[n];
        for (int i = 0; i < n; i++) {
            tasks[i] = Integer.parseInt(input[index++]);
        }
        int m = Integer.parseInt(input[index]);

        int result = minMaxLoad(tasks, m);
        System.out.println(result);

        scan.close();
    }

    public static void runProgram(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        main(new String[0]);
    }

    @Test
    public void testCase1() {
        String input = "4 1 2 3 4 2";
        runProgram(input);
        assertEquals("5", outContent.toString().trim());
    }

    @Test
    public void testCase2() {
        String input = "7 2 1 3 1 4 2 3 2";
        runProgram(input);
        assertEquals("8", outContent.toString().trim());
    }

    @Test
    public void testCase3() {
        String input = "5 10 10 10 10 10 2";
        runProgram(input);
        assertEquals("30", outContent.toString().trim());
    }

    @Test
    public void testCase4() {
        String input = "3 1 100 1 4";
        runProgram(input);
        assertEquals("100", outContent.toString().trim());
    }

    @Test
    public void testCase5() {
        String input = "6 5 5 5 5 5 5 3";
        runProgram(input);
        assertEquals("10", outContent.toString().trim());
    }

    @Test
    public void testCase6() {
        String input = "1 1000000000 1";
        runProgram(input);
        assertEquals("1000000000", outContent.toString().trim());
    }

    @Test
    public void testCase7() {
        String input = "9 7 5 9 3 8 2 4 1 6 3";
        runProgram(input);
        assertEquals("16", outContent.toString().trim());
    }

    @Test
    public void testCase8() {
        String input = "10 1 1 1 1 1 1 1 1 1 1 10";
        runProgram(input);
        assertEquals("1", outContent.toString().trim());
    }

    @Test
    public void testCase9() {
        String input = "4 4 4 4 4 4";
        runProgram(input);
        assertEquals("4", outContent.toString().trim());
    }

    @Test
    public void testCase10() {
        String input = "8 1 3 5 7 9 2 4 6 5";
        runProgram(input);
        assertEquals("9", outContent.toString().trim());
    }
}