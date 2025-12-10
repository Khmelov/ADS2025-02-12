package by.bsuir.dsa.csv2025.gr451003.Мазалевич;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
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
    }

    public static int findPeakElement(int[] arr) {
        if (arr.length == 0) return -1;
        if (arr.length == 1) return 0;

        for (int i = 0; i < arr.length; i++) {
            boolean leftOk = (i == 0) || (arr[i] > arr[i - 1]);
            boolean rightOk = (i == arr.length - 1) || (arr[i] > arr[i + 1]);

            if (leftOk && rightOk) {
                return i;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите размер массива и элементы через пробел: ");
        String inputLine = scanner.nextLine();
        String[] parts = inputLine.split(" ");
        int n = Integer.parseInt(parts[0]);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(parts[i + 1]);
        }
        int result = findPeakElement(arr);
        System.out.println("Индекс пикового элемента: " + result);
        System.out.println("Пиковый элемент: " + arr[result]);
        System.out.println("Массив: " + Arrays.toString(arr));
        scanner.close();
    }

    public static void runTestProgram(String input) {
        Scanner scanner = new Scanner(input);
        int n = scanner.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        int result = findPeakElement(arr);
        System.out.println(result);
        scanner.close();
    }

    @Test
    public void testCase1() throws Exception {
        String input = "4 1 2 3 1";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("2", output);
        assertTrue(isPeak(new int[]{1, 2, 3, 1}, Integer.parseInt(output)));
    }

    @Test
    public void testCase2() throws Exception {
        String input = "5 1 3 2 4 5";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("1", output);
        assertTrue(isPeak(new int[]{1, 3, 2, 4, 5}, Integer.parseInt(output)));
    }

    @Test
    public void testCase3() throws Exception {
        String input = "3 5 1 2";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("0", output);
        assertTrue(isPeak(new int[]{5, 1, 2}, Integer.parseInt(output)));
    }

    @Test
    public void testCase4() throws Exception {
        String input = "3 1 2 3";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("2", output);
        assertTrue(isPeak(new int[]{1, 2, 3}, Integer.parseInt(output)));
    }

    @Test
    public void testCase5() throws Exception {
        String input = "1 10";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("0", output);
        assertTrue(isPeak(new int[]{10}, Integer.parseInt(output)));
    }

    @Test
    public void testCase6() throws Exception {
        String input = "5 1 2 5 3 2";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("2", output);
        assertTrue(isPeak(new int[]{1, 2, 5, 3, 2}, Integer.parseInt(output)));
    }

    @Test
    public void testCase7() throws Exception {
        String input = "6 10 9 8 7 6 5";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("0", output);
        assertTrue(isPeak(new int[]{10, 9, 8, 7, 6, 5}, Integer.parseInt(output)));
    }

    @Test
    public void testCase8() throws Exception {
        String input = "5 1 2 3 4 5";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("4", output);
        assertTrue(isPeak(new int[]{1, 2, 3, 4, 5}, Integer.parseInt(output)));
    }

    @Test
    public void testCase9() throws Exception {
        String input = "5 -5 -3 0 -2 -4";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("2", output);
        assertTrue(isPeak(new int[]{-5, -3, 0, -2, -4}, Integer.parseInt(output)));
    }

    @Test
    public void testCase10() throws Exception {
        String input = "8 1 2 3 4 5 4 3 2";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        outContent.reset();
        runTestProgram(input);
        String output = outContent.toString().trim();
        assertEquals("4", output);
        assertTrue(isPeak(new int[]{1, 2, 3, 4, 5, 4, 3, 2}, Integer.parseInt(output)));
    }

    private static boolean isPeak(int[] arr, int index) {
        if (arr.length == 1) return true;
        if (index == 0) {
            return arr[index] > arr[index + 1];
        } else if (index == arr.length - 1) {
            return arr[index] > arr[index - 1];
        } else {
            return arr[index] > arr[index - 1] && arr[index] > arr[index + 1];
        }
    }
}