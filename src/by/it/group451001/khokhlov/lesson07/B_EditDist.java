package by.it.group451001.khokhlov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // d[i][j] — расстояние редактирования между первыми i символами строки one и j символами строки two
        int[][] d = new int[n + 1][m + 1];

        // Заполнение базы — сравнение с пустой строкой
        for (int i = 0; i <= n; i++) {
            d[i][0] = i; // удаление всех символов
        }
        for (int j = 0; j <= m; j++) {
            d[0][j] = j; // вставка всех символов
        }

        // Основной цикл заполнения таблицы
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;

                d[i][j] = Math.min(
                        Math.min(
                                d[i - 1][j] + 1,    // удаление
                                d[i][j - 1] + 1     // вставка
                        ),
                        d[i - 1][j - 1] + cost     // замена (или ничего)
                );
            }
        }

        return d[n][m];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);

        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // ab, ab → 0
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // short, ports → 3
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // distance, editing → 5
    }
}
