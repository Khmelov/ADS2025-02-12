package by.bsuir.dsa.csv2025.gr451001.Клевко;



import org.junit.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class Solution {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = System.in;
        Solution.solve(inputStream);
    }

    public static void solve(InputStream in) {
        Scanner sc = new Scanner(in);

        int t = sc.nextInt();
        for (int test = 0; test < t; test++) {
            int n = sc.nextInt();
            int d = sc.nextInt();
            int k = sc.nextInt();

            int[] left_i = new int[n];
            int[] right_i = new int[n];

            for (int j = 0; j < k; j++) {
                int l = sc.nextInt();
                int r = sc.nextInt();
                left_i[l - 1]++;
                right_i[r - 1]++;
            }

            int work_in_d = 0;
            int best_sun_day = 1;
            int best_mum_day = 1;

            for (int j = 0; j < d; j++) {
                work_in_d += left_i[j];
            }

            int best_sun_work_in_d = work_in_d;
            int best_mum_work_in_d = work_in_d;

            for (int j = 1; j <= n - d; j++) {
                work_in_d += left_i[j + d - 1];
                work_in_d -= right_i[j - 1];

                if (best_sun_work_in_d < work_in_d) {
                    best_sun_work_in_d = work_in_d;
                    best_sun_day = j + 1;
                }
                if (best_mum_work_in_d > work_in_d) {
                    best_mum_work_in_d = work_in_d;
                    best_mum_day = j + 1;
                }
            }

            System.out.print(best_sun_day + " " + best_mum_day + "\n");
        }
    }

    @Test(timeout = 2000)
    public void checkRobin() throws Exception {
        String simulatedInput = "6\n" +
                "2 1 1\n" +
                "1 2\n" +
                "4 1 2\n" +
                "1 2\n" +
                "2 4\n" +
                "7 2 3\n" +
                "1 2\n" +
                "1 3\n" +
                "6 7\n" +
                "5 1 2\n" +
                "1 2\n" +
                "3 5\n" +
                "9 2 1\n" +
                "2 8\n" +
                "9 2 4\n" +
                "7 9\n" +
                "4 8\n" +
                "1 3\n" +
                "2 3\n";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        PrintStream originalOut = System.out;
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setOut(new PrintStream(outContent));
        Solution.solve(inputStream);
        System.setOut(originalOut);

        String expectedOutput =
                "1 1\n" +
                "2 1\n" +
                "1 4\n" +
                "1 1\n" +
                "1 1\n" +
                "3 4";

        String actualOutput = outContent.toString().trim();

        assertEquals(expectedOutput, actualOutput);
    }
}
