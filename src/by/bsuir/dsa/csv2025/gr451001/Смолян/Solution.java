package by.bsuir.dsa.csv2025.gr451001.Смолян;

import java.io.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class Solution {
    
    static class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            int n = s.length();
            
            boolean[][] dp = new boolean[n][8];
            
            dp[0][(s.charAt(0) - '0') % 8] = true;
            
            for (int i = 1; i < n; i++) {
                int currentDigit = s.charAt(i) - '0';
                dp[i][currentDigit % 8] = true;
                
                for (int j = 0; j < 8; j++) {
                    if (dp[i - 1][j]) {
                        int newRemainder = (j * 10 + currentDigit) % 8;
                        dp[i][newRemainder] = true;
                        dp[i][j] = true;
                    }
                }
            }
            
            for (int i = 0; i < n; i++) {
                if (dp[i][0]) {
                    System.out.println("YES");
                    return;
                }
            }
            
            System.out.println("NO");
        }
    }


    private void runTest(String input, String expectedOutput) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            Main.main(new String[0]);

            String actual = baos.toString().replace("\r\n", "\n").trim();
            String expected = expectedOutput.replace("\r\n", "\n").trim();

            assertEquals(expected, actual, "Input: " + input);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void test1() throws Exception {
        runTest("3454", "YES");
    }

    @Test
    void test2() throws Exception {
        runTest("10", "YES");
    }

    @Test
    void test3() throws Exception {
        runTest("111111", "NO");
    }

    @Test
    void test4() throws Exception {
        runTest("8", "YES");
    }

    @Test
    void test5() throws Exception {
        runTest("000", "YES");
    }

    @Test
    void test6() throws Exception {
        runTest("16", "YES");
    }

    @Test
    void test7() throws Exception {
        runTest("123456789", "YES");
    }

    @Test
    void test8() throws Exception {
        runTest("1111111232", "YES");
    }

    @Test
    void test9() throws Exception {
        runTest("777777777777777777777777777777777777", "NO");
    }

    @Test
    void test10() throws Exception {
        runTest("9000000009000000002", "YES");
    }
}