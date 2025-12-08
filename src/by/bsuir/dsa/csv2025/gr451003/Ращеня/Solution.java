package by.bsuir.dsa.csv2025.gr451003.Ращеня;

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
        outContent.reset();                    // <<< КРИТИЧНО: очищаем буфер перед каждым тестом
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private static final double A = 5000;
    private static final double B = 2000;
    private static final double C = 100;
    private static final double frequency = 2 * Math.PI;
    private static final double attenuation = 0.1;
    private static final double frequency_of_attenuation = 4 * Math.PI;

    public static double loadFunction(double t) {
        return A * Math.sin(frequency * t) +
                B * Math.exp(-attenuation * t) * Math.cos(frequency_of_attenuation * t) +
                C * t;
    }

    public static double simpsonIntegration(double a, double b, int n) {
        if (n % 2 != 0) n++;
        double h = (b - a) / n;
        double sum = loadFunction(a) + loadFunction(b);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            double value = loadFunction(x);
            sum += value * ((i % 2 == 0) ? 2 : 4);
        }
        return sum * h / 3;
    }

    public static double findMaxLoad(double a, double b, double step) {
        double maxLoad = Double.MIN_VALUE;
        for (double t = a; t <= b + 1e-9; t += step) {  // +1e-9 чтобы включить b
            double load = loadFunction(t);
            if (load > maxLoad) {
                maxLoad = load;
            }
        }
        return maxLoad;
    }

    public static void runProgram(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

        double startTime = scanner.nextDouble();
        double endTime = scanner.nextDouble();
        int integrationSteps = scanner.nextInt();
        double analysisStep = scanner.nextDouble();

        double totalLoad = simpsonIntegration(startTime, endTime, integrationSteps);
        double averageLoad = totalLoad / (endTime - startTime);
        double maxLoad = findMaxLoad(startTime, endTime, analysisStep);

        System.out.printf(Locale.US, "%.2f%n", averageLoad);
        System.out.printf(Locale.US, "%.2f%n", maxLoad);

        scanner.close();
    }

    // ==================== ТЕСТЫ ====================

    private void assertOutput(String input, String expectedAvg, String expectedMax) {
        outContent.reset();
        runProgram(input);
        String[] lines = outContent.toString().trim().split("\\r?\\n");
        assertEquals("Неправильное среднее значение", expectedAvg, lines[0].trim());
        assertEquals("Неправильное максимальное значение", expectedMax, lines[1].trim());
    }

    @Test public void testCase1()  { assertOutput("0.0 10.0 1000 0.01",  "500.08", "5131.94"); }
    @Test public void testCase2()  { assertOutput("0.0 5.0 500 0.01",    "250.10", "4132.74"); }
    @Test public void testCase3()  { assertOutput("0.0 8.0 800 0.01",    "400.09", "4756.35"); }
    @Test public void testCase4()  { assertOutput("0.0 12.0 1200 0.01",  "600.07", "5475.70"); }
    @Test public void testCase5()  { assertOutput("0.0 6.0 600 0.01",    "300.10", "4343.92"); }
    @Test public void testCase6()  { assertOutput("0.0 15.0 1500 0.01",  "750.07", "5943.98"); }
    @Test public void testCase7()  { assertOutput("0.0 4.0 400 0.01",    "200.10",  "3946.40"); }
    @Test public void testCase8()  { assertOutput("0.0 7.0 700 0.01",    "350.09", "4555.11"); }
    @Test public void testCase9()  { assertOutput("0.0 9.0 900 0.01",    "450.08", "4948.53"); }
    @Test public void testCase10() { assertOutput("0.0 11.0 1100 0.01", "550.08", "5307.41"); }
}