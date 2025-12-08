package by.bsuir.dsa.csv2025.gr410902.Державская_Елена;


import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        System.out.println(assessSolution(password));
        scanner.close();
    }

    public static String assessSolution(String password) {
        // Проверка на пустой пароль
        if (password == null || password.isEmpty()) {
            return "NULL";
        }

        int N = encodePassword(password);

        if (N == 1) {
            return "NULL";
        }

        List<Integer> primeFactors = factorize(N);

        return analyzeSecurityLevel(primeFactors);
    }

    private static int encodePassword(String password) {
        int sum = 0;
        for (char c : password.toCharArray()) {
            sum += (int) c;
        }
        return sum;
    }

    private static List<Integer> factorize(int n) {
        List<Integer> factors = new ArrayList<>();

        while (n % 2 == 0) {
            factors.add(2);
            n /= 2;
        }

        for (int i = 3; i * i <= n; i += 2) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }

        if (n > 1) {
            factors.add(n);
        }

        return factors;
    }

    private static String analyzeSecurityLevel(List<Integer> factors) {
        if (factors.size() == 1) {
            return "HIGH";
        }

        if (factors.size() == 2) {
            return "MEDIUM";
        }

        return "LOW";
    }

    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;

    }

    public String runTest(String input){
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        main(new String[]{});
        String result = output.toString().trim();
        return result;
    }


    @Test
    public void RunAllTests() throws Exception{
        assertEquals("HIGH", runTest("AB\n"));
        assertEquals("MEDIUM", runTest("A\n"));
        assertEquals("LOW", runTest("abc\n"));
        assertEquals("MEDIUM", runTest("eee\n"));
        assertEquals("HIGH", runTest("password\n"));
        assertEquals("LOW", runTest("abc\n"));
    }

}