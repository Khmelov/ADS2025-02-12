package by.bsuir.dsa.csv2025.gr451001.Буйко;

import java.io.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {
    
    public static boolean canFirstCompanyWin(int[] prices) {
        int n = prices.length;
        if (n == 0) return true;

        int[] dp = new int[n];

        // Обработка массива с конца
        for (int i = n - 1; i >= 0; i--) {
            dp[i] = prices[i];  // Базовый случай - одна акция

            for (int j = i + 1; j < n; j++) {
                // Выбор лучшего варианта: взять слева или справа
                int takeLeft = prices[i] - dp[j];      // Взять слева
                int takeRight = prices[j] - dp[j - 1]; // Взять справа
                dp[j] = Math.max(takeLeft, takeRight);
            }
        }

        return dp[n - 1] >= 0;  // Первый игрок выигрывает если преимущество >= 0
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод количества элементов
        int n = scanner.nextInt();

        // Ввод самих элементов
        int[] prices = new int[n];
        for (int i = 0; i < n; i++) {
            prices[i] = scanner.nextInt();
        }

        boolean result = canFirstCompanyWin(prices);
        System.out.println(result);
        scanner.close();
    }

    private void runTest(String input, String expectedOutput) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            Solution.main(new String[0]);

            String actual = baos.toString().replace("\r\n", "\n").trim();
            String expected = expectedOutput.replace("\r\n", "\n").trim();

            assertEquals(expected, actual);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    public void test1() throws Exception {
        runTest("0", "true");
    }

    @Test
    public void test2() throws Exception {
        runTest("1\n100", "true");
    }

    @Test
    public void test3() throws Exception {
        runTest("2\n100 50", "true");
    }

    @Test
    public void test4() throws Exception {
        runTest("3\n1 5 2", "false");
    }

    @Test
    public void test5() throws Exception {
        runTest("3\n100 50 100", "true");
    }

    @Test
    public void test6() throws Exception {
        runTest("3\n25 100 25", "false");
    }

    @Test
    public void test7() throws Exception {
        runTest("5\n10 10 10 10 10", "true");
    }

    @Test
    public void test8() throws Exception {
        runTest("4\n1 2 3 4", "true");
    }

    @Test
    public void test9() throws Exception {
        runTest("7\n5 2 8 1 6 3 7", "false");
    }

    @Test
    public void test10() throws Exception {
        runTest("3\n1 10 1", "false");
    }
}