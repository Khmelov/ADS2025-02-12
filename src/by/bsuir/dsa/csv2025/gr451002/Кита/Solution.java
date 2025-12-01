package by.bsuir.dsa.csv2025.gr451002.Кита;

import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertTrue;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine().replaceAll("\\s", "");
        scanner.close();

        Solution instance = new Solution();
        int[] result = instance.calculateMinMax(expression);
        System.out.println(result[0] + " " + result[1]);
    }

    // Основной метод вычисления
    public int[] calculateMinMax(String expression) {
        // Длина всей строки len = 2n + 1, тогда количество чисел равно (len - 1)/2 + 1
        int n = (expression.length() - 1) / 2 + 1;
        // Разделяем выражение на числа и операторы
        int[] numbers = new int[n]; // n + 1 чисел
        char[] operators = new char[n - 1]; // n операций

        // Заполняем массивы
        int numInd = 0;
        int opInd = 0;
        StringBuilder currNum = new StringBuilder();

        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c)) {
                currNum.append(c);
            } else {
                numbers[numInd++] = Integer.parseInt(currNum.toString());
                currNum = new StringBuilder();
                operators[opInd++] = c;
            }
        }
        numbers[numInd] = Integer.parseInt(currNum.toString());

        // Создаем 2 таблицы для хранения минимальных и максимальных значений
        int[][] minDP = new int[n][n];
        int[][] maxDP = new int[n][n];

        // Инициализируем диагональ числами из выражения
        for (int i = 0; i < n; i++) {
            minDP[i][i] = numbers[i];
            maxDP[i][i] = numbers[i];
        }

        // Заполняем таблицы для подвыражений разной длины
        for (int length = 2; length <= n; length++) {
            for (int i = 0; i <= n - length; i++) {
                int j = i + length - 1;
                minDP[i][j] = Integer.MAX_VALUE;
                maxDP[i][j] = Integer.MIN_VALUE;

                for (int k = i; k < j; k++) {
                    char operator = operators[k];

                    // Вычисляем все возможные комбинации
                    int a = evaluate(minDP[i][k], minDP[k+1][j], operator);
                    int b = evaluate(minDP[i][k], maxDP[k+1][j], operator);
                    int c = evaluate(maxDP[i][k], minDP[k+1][j], operator);
                    int d = evaluate(maxDP[i][k], maxDP[k+1][j], operator);

                    minDP[i][j] = Math.min(minDP[i][j],
                            Math.min(a, Math.min(b, Math.min(c, d))));
                    maxDP[i][j] = Math.max(maxDP[i][j],
                            Math.max(a, Math.max(b, Math.max(c, d))));
                }
            }
        }

        return new int[]{minDP[0][n-1], maxDP[0][n-1]};
    }

    public int evaluate(int a, int b, char operator) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            default: throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    @Test
    public void check1() throws Exception {
        Solution instance = new Solution();
        int[] result = instance.calculateMinMax("1+2*3");
        boolean ok = (result[0] == 7 && result[1] == 9);
        assertTrue("Test 1 failed.", ok);
    }

    @Test
    public void check2() throws Exception {
        Solution instance = new Solution();
        int[] result = instance.calculateMinMax("7-2*5");
        boolean ok = (result[0] == -3 && result[1] == 25);
        assertTrue("Test 2 failed.", ok);
    }

    @Test
    public void check3() throws Exception {
        Solution instance = new Solution();
        int[] result = instance.calculateMinMax("1+2*3-4*5");
        boolean ok = (result[0] == -51 && result[1] == 25);
        assertTrue("Test 3 failed.", ok);
    }

    @Test
    public void check4() throws Exception {
        Solution instance = new Solution();
        int[] result = instance.calculateMinMax("1+1+1+1+1*1*1*1*1-1-1-1-1");
        boolean ok = (result[0] == -15 && result[1] == 15);
        assertTrue("Test 4 failed.", ok);
    }

    @Test
    public void check5() throws Exception {
        Solution instance = new Solution();
        int[] result = instance.calculateMinMax("12+4-7*4");
        boolean ok = (result[0] == -12 && result[1] == 36);
        assertTrue("Test 5 failed.", ok);
    }
}