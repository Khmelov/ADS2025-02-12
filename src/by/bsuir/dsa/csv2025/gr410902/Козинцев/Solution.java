package by.bsuir.dsa.csv2025.gr410902.Козинцев;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;


public class Solution {

    private static double f(double x, double a, double b, double c, double d) {
        return a * Math.pow(x, 3) + b * Math.pow(x, 2) + c * x + d;
    }

    private static double df(double x, double a, double b, double c) {
        return 3 * a * Math.pow(x, 2) + 2 * b * x + c;
    }

    public static String findRootByNewtonGeneral(String input) {
        String[] parts = input.split(" ");

        double a, b, c, d, x;
        int maxIterations;

        a = Double.parseDouble(parts[0]);
        b = Double.parseDouble(parts[1]);
        c = Double.parseDouble(parts[2]);
        d = Double.parseDouble(parts[3]);
        x = Double.parseDouble(parts[4]);
        maxIterations = Integer.parseInt(parts[5]);


        for (int i = 0; i < maxIterations; i++) {
            double fx = f(x, a, b, c, d);
            double dfx = df(x, a, b, c);

            if (Math.abs(dfx) < 1e-9) {
                break;
            }

            double x_new = x - fx / dfx;

            if (Math.abs(x_new - x) < 1e-5) {
                x = x_new;
                break;
            }

            x = x_new;
        }

        x *= 10000;

        int res = (int) Math.round(x);

        return Integer.toString(res);
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        String output = findRootByNewtonGeneral(input);

        System.out.println(output);

    }

    @Test
    public void test() {
  
        Object[][] testCases = {
                {"1.0 -2.0 3.0 -5.0 2.0 5", "18437"},
                {"1.5 2.3 4.2 2.1 0.0 10", "-6273"},
                {"1.0 -4.0 4.0 -1.0 1.5 5", "10000"},
                {"1.0 0.0 -2.0 2.0 -2.0 5", "-17693"},
                {"2.0 3.0 4.0 1.0 2.0 15", "-3059"},
                {"1.0 7.0 3.0 0.0 -5.0 10", "-65414"},
                {"1.0 0.0 -2.0 -5.0 2.0 5", "20946"},
        };

        for (Object[] testCase : testCases) {
            String input = (String) testCase[0];
            String expected = (String) testCase[1];

            String actual = findRootByNewtonGeneral(input);

            assertEquals("(input: " + input + ")", expected, actual);
        }
    }


}
