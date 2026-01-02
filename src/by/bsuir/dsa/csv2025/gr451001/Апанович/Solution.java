package by.bsuir.dsa.csv2025.gr451001.Апанович;

import java.io.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] isConnected = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                isConnected[i][j] = scanner.nextInt();
            }
        }
        int result = findCircleNum(isConnected);
        System.out.println(result);
        scanner.close();
    }

    public static int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int provinces = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                provinces++;
                dfs(i, visited, isConnected);
            }
        }
        return provinces;
    }

    private static void dfs(int city, boolean[] visited, int[][] isConnected) {
        visited[city] = true;
        for (int neighbor = 0; neighbor < isConnected.length; neighbor++) {
            if (isConnected[city][neighbor] == 1 && !visited[neighbor]) {
                dfs(neighbor, visited, isConnected);
            }
        }
    }

    private static void runTest(String input, String expectedOutput) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            Solution.main(new String[0]);

            String actual = baos.toString().replace("\r\n", "\n");
            String expected = expectedOutput.replace("\r\n", "\n");

            assertEquals(expected, actual);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    public void test1() throws Exception {
        runTest("4\n" +
                        "1 1 1 1\n" +
                        "1 1 1 1\n" +
                        "1 1 1 1\n" +
                        "1 1 1 1",
                "1\n");
    }

    @Test
    public void test2() throws Exception {
        runTest("5\n" +
                        "1 0 0 0 0\n" +
                        "0 1 0 0 0\n" +
                        "0 0 1 0 0\n" +
                        "0 0 0 1 0\n" +
                        "0 0 0 0 1",
                "5\n");
    }

    @Test
    public void test3() throws Exception {
        runTest("6\n" +
                        "1 1 0 0 0 0\n" +
                        "1 1 0 0 0 0\n" +
                        "0 0 1 1 0 0\n" +
                        "0 0 1 1 0 0\n" +
                        "0 0 0 0 1 0\n" +
                        "0 0 0 0 0 1",
                "4\n");
    }

    @Test
    public void test4() throws Exception {
        runTest("4\n" +
                        "1 1 0 0\n" +
                        "1 1 1 0\n" +
                        "0 1 1 1\n" +
                        "0 0 1 1",
                "1\n");
    }

    @Test
    public void test5() throws Exception {
        runTest("5\n" +
                        "1 1 1 1 1\n" +
                        "1 1 0 0 0\n" +
                        "1 0 1 0 0\n" +
                        "1 0 0 1 0\n" +
                        "1 0 0 0 1",
                "1\n");
    }

    @Test
    public void test6() throws Exception {
        runTest("6\n" +
                        "1 1 1 0 0 0\n" +
                        "1 1 1 0 0 0\n" +
                        "1 1 1 0 0 0\n" +
                        "0 0 0 1 1 1\n" +
                        "0 0 0 1 1 1\n" +
                        "0 0 0 1 1 1",
                "2\n");
    }

    @Test
    public void test7() throws Exception {
        runTest("1\n" +
                        "1",
                "1\n");
    }

    @Test
    public void test8() throws Exception {
        runTest("5\n" +
                        "1 1 0 0 1\n" +
                        "1 1 1 0 0\n" +
                        "0 1 1 1 0\n" +
                        "0 0 1 1 1\n" +
                        "1 0 0 1 1",
                "1\n");
    }

    @Test
    public void test9() throws Exception {
        runTest("7\n" +
                        "1 1 0 0 0 0 0\n" +
                        "1 1 1 0 0 0 0\n" +
                        "0 1 1 0 0 0 0\n" +
                        "0 0 0 1 1 0 0\n" +
                        "0 0 0 1 1 1 0\n" +
                        "0 0 0 0 1 1 1\n" +
                        "0 0 0 0 0 1 1",
                "2\n");
    }

    @Test
    public void test10() throws Exception {
        runTest("8\n" +
                        "1 0 1 0 0 0 0 0\n" +
                        "0 1 0 1 0 0 0 0\n" +
                        "1 0 1 0 0 0 0 0\n" +
                        "0 1 0 1 0 0 0 0\n" +
                        "0 0 0 0 1 1 0 0\n" +
                        "0 0 0 0 1 1 1 0\n" +
                        "0 0 0 0 0 1 1 1\n" +
                        "0 0 0 0 0 0 1 1",
                "3\n");
    }
}