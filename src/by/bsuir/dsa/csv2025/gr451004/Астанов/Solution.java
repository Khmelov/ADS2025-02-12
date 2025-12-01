package by.bsuir.dsa.csv2025.gr451004.Астанов;

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

    public static int binarySearchFirst(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (arr[mid] == x) {
                result = mid;
                right = mid - 1;
            } else if (arr[mid] < x) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] parts = input.split(" ");
        
        int n = Integer.parseInt(parts[0]);
        int[] arr = new int[n];
        
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(parts[i + 1]);
        }
        
        int x = Integer.parseInt(parts[parts.length - 1]);
        int result = binarySearchFirst(arr, x);
        
        System.out.println(result);
    }

    public static void runProgram(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        main(new String[0]);
    }

    @Test
    public void testCase1() {
        String input = "5 1 3 3 5 7 3";
        runProgram(input);
        assertEquals("1", outContent.toString().trim());
    }

    @Test
    public void testCase2() {
        String input = "6 1 2 4 8 16 32 4";
        runProgram(input);
        assertEquals("2", outContent.toString().trim());
    }

    @Test
    public void testCase3() {
        String input = "4 10 20 30 40 25";
        runProgram(input);
        assertEquals("-1", outContent.toString().trim());
    }

    @Test
    public void testCase4() {
        String input = "3 100 100 100 100";
        runProgram(input);
        assertEquals("0", outContent.toString().trim());
    }

    @Test
    public void testCase5() {
        String input = "1 5 1";
        runProgram(input);
        assertEquals("-1", outContent.toString().trim());
    }

    @Test
    public void testCase6() {
        String input = "5 2 4 6 8 10 6";
        runProgram(input);
        assertEquals("2", outContent.toString().trim());
    }

    @Test
    public void testCase7() {
        String input = "4 1 1 1 1 1";
        runProgram(input);
        assertEquals("0", outContent.toString().trim());
    }

    @Test
    public void testCase8() {
        String input = "7 10 20 30 40 50 60 70 35";
        runProgram(input);
        assertEquals("-1", outContent.toString().trim());
    }

    @Test
    public void testCase9() {
        String input = "3 15 25 35 25";
        runProgram(input);
        assertEquals("1", outContent.toString().trim());
    }

    @Test
    public void testCase10() {
        String input = "2 8 12 12";
        runProgram(input);
        assertEquals("1", outContent.toString().trim());
    }
}