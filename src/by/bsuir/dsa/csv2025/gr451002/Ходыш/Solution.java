package by.bsuir.dsa.csv2025.gr451002.Ходыш;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class Solution {
    public String invertBitOperation(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int number = scanner.nextInt();
        int bitPosition = scanner.nextInt();
        scanner.close();

        if (bitPosition < 0 || bitPosition > 31) {
            return "Ошибка: позиция бита должна быть в диапазоне 0-31";
        }

        // инвертировать бит
        int mask = 1 << bitPosition;
        int result = number ^ mask;

        String binaryResult = toFormattedBinary(result);
        return result + " " + binaryResult;
    }

    // бинарного представления
    public static String toFormattedBinary(int number) {
        String binary = Integer.toBinaryString(number);

        // Если число отрицательное, оставляем все 32 бита
        if (number < 0) {
            binary = String.format("%32s", binary).replace(' ', '0');
        }

        return binary;
    }

    public static void main(String[] args) {
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

        // инвертировать бит
        int mask = 1 << bitPosition;
        int result = number ^ mask;

        System.out.println("РЕЗУЛЬТАТ:");
        System.out.printf("Десятичное представление: %,d%n", result);
        System.out.printf("Побитовое представление:  %s%n", toFormattedBinary(result));
    }

    @Test
    public void test1() {
        Solution solver = new Solution();
        assertEquals("11 1011", solver.invertBitOperation("10 0"));
    }

    @Test
    public void test2() {
        Solution solver = new Solution();
        assertEquals("8 1000", solver.invertBitOperation("10 1"));
    }

    @Test
    public void test3() {
        Solution solver = new Solution();
        assertEquals("8 1000", solver.invertBitOperation("0 3"));
    }

    @Test
    public void test4() {
        Solution solver = new Solution();
        assertEquals("239 11101111", solver.invertBitOperation("255 4"));
    }

    @Test
    public void test5() {
        Solution solver = new Solution();
        assertEquals("255 11111111", solver.invertBitOperation("127 7"));
    }

    @Test
    public void test6() {
        Solution solver = new Solution();
        assertEquals("-33 11111111111111111111111111011111", solver.invertBitOperation("-1 5"));
    }

    @Test
    public void test7() {
        Solution solver = new Solution();
        assertEquals("14 1110", solver.invertBitOperation("15 0"));
    }

    @Test
    public void test8() {
        Solution solver = new Solution();
        assertEquals("0 0", solver.invertBitOperation("8 3"));
    }

    @Test
    public void test9() {
        Solution solver = new Solution();
        assertEquals("186 10111010", solver.invertBitOperation("170 4"));
    }

    @Test
    public void test10() {
        Solution solver = new Solution();
        assertEquals("0 0", solver.invertBitOperation("1 0"));
    }
}