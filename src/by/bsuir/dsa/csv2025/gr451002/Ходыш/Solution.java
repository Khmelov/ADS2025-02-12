package by.bsuir.dsa.csv2025.gr451002.Ходыш;

import java.util.Scanner;
import java.lang.reflect.Method;

public class Solution {

    public static void main(String[] args) {
        runTests();

        // Для обычного использования оставьте код ниже:
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите исходное число: ");
        int number = scanner.nextInt();

        System.out.print("Введите позицию бита для инвертирования (0-31): ");
        int bitPosition = scanner.nextInt();

        if (bitPosition < 0 || bitPosition > 31) {
            System.out.println("Ошибка: позиция бита должна быть в диапазоне 0-31");
            return;
        }

        scanner.close();

        // Выполняем операцию инвертирования бита
        int mask = 1 << bitPosition;
        int result = number ^ mask;

        System.out.println("РЕЗУЛЬТАТ:");
        System.out.printf("Десятичное представление: %,d%n", result);
        System.out.printf("Побитовое представление:  %s%n", toFormattedBinary(result));
    }

    // Вспомогательный метод для бинарного представления
    public static String toFormattedBinary(int number) {
        String binary = Integer.toBinaryString(number);

        // Если число отрицательное, оставляем все 32 бита
        if (number < 0) {
            binary = String.format("%32s", binary).replace(' ', '0');
        }

        // Просто возвращаем бинарную строку без пробелов
        return binary;
    }

    // ==================== ТЕСТЫ ====================

    // Псевдо-аннотация для пометки тестовых методов
    private static @interface Test {}

    @Test
    public static void test1_BasicCase() {
        int number = 10;
        int position = 0;
        int expected = 11;
        String expectedBinary = "1011";
        runTest("Test 1 - Basic case (10, 0 → 11)", number, position, expected, expectedBinary);
    }

    @Test
    public static void test2_InvertFirstBit() {
        int number = 10;
        int position = 1;
        int expected = 8;
        String expectedBinary = "1000";
        runTest("Test 2 - Invert first bit (10, 1 → 8)", number, position, expected, expectedBinary);
    }

    @Test
    public static void test3_SetBitInZero() {
        int number = 0;
        int position = 3;
        int expected = 8;
        String expectedBinary = "1000";
        runTest("Test 3 - Set bit in zero (0, 3 → 8)", number, position, expected, expectedBinary);
    }

    @Test
    public static void test4_MaxByte() {
        int number = 255;
        int position = 4;
        int expected = 239;
        String expectedBinary = "11101111";
        runTest("Test 4 - Max byte (255, 4 → 239)", number, position, expected, expectedBinary);
    }

    @Test
    public static void test5_ChangeHighestBit() {
        int number = 127;
        int position = 7;
        int expected = 255;
        String expectedBinary = "11111111";
        runTest("Test 5 - Change highest bit (127, 7 → 255)", number, position, expected, expectedBinary);
    }

    @Test
    public static void test6_NegativeNumber() {
        int number = -1;
        int position = 5;
        int expected = -33;
        String expectedBinary = "11111111111111111111111111011111";
        runTest("Test 6 - Negative number (-1, 5 → -33)", number, position, expected, expectedBinary);
    }

    @Test
    public static void test7_InvertLowestBit() {
        int number = 15;
        int position = 0;
        int expected = 14;
        String expectedBinary = "1110";
        runTest("Test 7 - Invert lowest bit (15, 0 → 14)", number, position, expected, expectedBinary);
    }

    @Test
    public static void test8_ResetSingleBit() {
        int number = 8;
        int position = 3;
        int expected = 0;
        String expectedBinary = "0";
        runTest("Test 8 - Reset single bit (8, 3 → 0)", number, position, expected, expectedBinary);
    }

    @Test
    public static void test9_Pattern10101010() {
        int number = 170;
        int position = 4;
        int expected = 186;
        String expectedBinary = "10111010";
        runTest("Test 9 - Pattern 10101010 (170, 4 → 186)", number, position, expected, expectedBinary);
    }

    @Test
    public static void test10_ResetOnlyBit() {
        int number = 1;
        int position = 0;
        int expected = 0;
        String expectedBinary = "0";
        runTest("Test 10 - Reset only bit (1, 0 → 0)", number, position, expected, expectedBinary);
    }

    // Вспомогательный метод для запуска тестов
    private static void runTest(String testName, int number, int position, int expected, String expectedBinary) {
        int result = invertBit(number, position);
        String resultBinary = toFormattedBinary(result);

        boolean decimalPass = (result == expected);
        boolean binaryPass = resultBinary.equals(expectedBinary);

        System.out.println(testName);
        System.out.printf("Input: number=%,d, position=%d%n", number, position);
        System.out.printf("Expected: %,d (%s)%n", expected, expectedBinary);
        System.out.printf("Actual:   %,d (%s)%n", result, resultBinary);
        System.out.printf("Decimal test: %s%n", decimalPass ? "PASS" : "FAIL");
        System.out.printf("Binary test:  %s%n", binaryPass ? "PASS" : "FAIL");
        System.out.println("---");
    }

    // Метод инвертирования бита для тестов
    public static int invertBit(int number, int bitPosition) {
        int mask = 1 << bitPosition;
        return number ^ mask;
    }

    // Запуск всех тестов
    public static void runTests() {
        System.out.println("Running Solution Tests...");
        System.out.println("=============================");

        try {
            Method[] methods = Solution.class.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Test.class)) {
                    method.invoke(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("All tests completed!");
        System.out.println("=============================");
    }
}