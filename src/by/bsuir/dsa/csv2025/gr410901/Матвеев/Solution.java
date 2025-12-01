package by.bsuir.dsa.csv2025.gr410901.Матвеев;

import java.util.Scanner;

public class Solution {
    
    // Функция для вычисления a^b mod m
    public static long modPow(long a, long b, long m) {
        if (m == 1) return 0;
        long result = 1;
        a = a % m;
        while (b > 0) {
            if (b % 2 == 1) {
                result = (result * a) % m;
            }
            a = (a * a) % m;
            b = b / 2;
        }
        return result;
    }
    
    // Функция для вычисления обратного элемента по модулю
    public static long modInverse(long a, long m) {
        a = a % m;
        for (long x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // обратный элемент не существует
    }
    
    // Функция для решения системы сравнений (китайская теорема об остатках)
    public static long chineseRemainder(long[] num, long[] rem) {
        long product = 1;
        for (long n : num) {
            product *= n;
        }
        
        long result = 0;
        for (int i = 0; i < num.length; i++) {
            long pp = product / num[i];
            result += rem[i] * modInverse(pp, num[i]) * pp;
        }
        
        return result % product;
    }
    
    // Функция для вычисления НОД
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            
            long result = processInput(input);
            System.out.println(result);
        }
        
        scanner.close();
    }
    
    public static long processInput(String input) {
        long result = 0;
        
        // Обработка различных форматов ввода
        if (input.contains("mod")) {
            String[] parts = input.split(" mod ");
            String expression = parts[0];
            long modulus = Long.parseLong(parts[1]);
            
            if (expression.contains("+")) {
                String[] operands = expression.split(" \\+ ");
                long a = Long.parseLong(operands[0]);
                long b = Long.parseLong(operands[1]);
                result = (a + b) % modulus;
                if (result < 0) result += modulus;
            } else if (expression.contains("*")) {
                String[] operands = expression.split(" \\* ");
                long a = Long.parseLong(operands[0]);
                long b = Long.parseLong(operands[1]);
                result = (a * b) % modulus;
                if (result < 0) result += modulus;
            } else if (expression.contains("^")) {
                if (expression.contains("^-1")) {
                    String base = expression.replace("^-1", "");
                    long a = Long.parseLong(base);
                    result = modInverse(a, modulus);
                } else {
                    String[] operands = expression.split("\\^");
                    long a = Long.parseLong(operands[0]);
                    long b = Long.parseLong(operands[1]);
                    result = modPow(a, b, modulus);
                }
            } else {
                // Простое число по модулю
                long num = Long.parseLong(expression);
                result = num % modulus;
                if (result < 0) result += modulus;
            }
        } else if (input.contains("x ≡")) {
            // Система сравнений
            String[] equations = input.split(", ");
            long[] moduli = new long[equations.length];
            long[] remainders = new long[equations.length];
            
            for (int i = 0; i < equations.length; i++) {
                String eq = equations[i].replace("x ≡ ", "").replace(" mod ", " ");
                String[] parts = eq.split(" ");
                remainders[i] = Long.parseLong(parts[0]);
                moduli[i] = Long.parseLong(parts[1]);
            }
            
            result = chineseRemainder(moduli, remainders);
        } else {
            // Сложное арифметическое выражение
            result = evaluateExpression(input);
        }
        
        return result;
    }
    
    // Простой парсер арифметических выражений
    public static long evaluateExpression(String expr) {
        expr = expr.replace("(", "").replace(")", "");
        String[] parts = expr.split(" ");
        
        long result = Long.parseLong(parts[0]);
        for (int i = 1; i < parts.length; i += 2) {
            String operator = parts[i];
            long operand = Long.parseLong(parts[i + 1]);
            
            switch (operator) {
                case "+": result += operand; break;
                case "-": result -= operand; break;
                case "*": result *= operand; break;
                case "/": result /= operand; break;
            }
        }
        return result;
    }
}
