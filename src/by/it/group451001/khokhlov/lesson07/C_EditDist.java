package by.it.group451001.khokhlov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // Таблица расстояний
        int[][] d = new int[n + 1][m + 1];

        // Таблица операций: '#' - match, '~' - replace, '+' - insert, '-' - delete
        char[][] ops = new char[n + 1][m + 1];

        // Базовые случаи: преобразование пустой строки
        for (int i = 0; i <= n; i++) {
            d[i][0] = i;
            ops[i][0] = '-';
        }
        for (int j = 0; j <= m; j++) {
            d[0][j] = j;
            ops[0][j] = '+';
        }
        ops[0][0] = '#';

        // Основной цикл — заполнение таблиц
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                int del = d[i - 1][j] + 1;
                int ins = d[i][j - 1] + 1;
                int rep = d[i - 1][j - 1] + cost;

                d[i][j] = Math.min(Math.min(del, ins), rep);

                if (d[i][j] == rep) {
                    ops[i][j] = (cost == 0) ? '#' : '~';
                } else if (d[i][j] == del) {
                    ops[i][j] = '-';
                } else {
                    ops[i][j] = '+';
                }
            }
        }

        // Восстановление редакционного пути
        StringBuilder result = new StringBuilder();
        int i = n, j = m;

        while (i > 0 || j > 0) {
            char op = ops[i][j];
            switch (op) {
                case '#':
                    result.insert(0, "#,");
                    i--;
                    j--;
                    break;
                case '~':
                    result.insert(0, "~" + two.charAt(j - 1) + ",");
                    i--;
                    j--;
                    break;
                case '+':
                    result.insert(0, "+" + two.charAt(j - 1) + ",");
                    j--;
                    break;
                case '-':
                    result.insert(0, "-" + one.charAt(i - 1) + ",");
                    i--;
                    break;
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
