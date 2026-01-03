package by.it.group451002.kita.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt();
        int n = scanner.nextInt();
        int gold[] = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        int[][] D = new int[n + 1][w + 1];

        // Инициализация
        for (int i = 0; i <= w; i++) {
            D[0][i] = 0;
        }
        for (int j = 0; j <= n; j++) {
            D[j][0] = 0;
        }

        // Заполняем таблицу
        for (int i = 1; i <= n; i++) {
            for (int weight = 1; weight <= w; weight++) {
                // Не берем i-й слиток
                D[i][weight] = D[i - 1][weight];

                // Пытаемся взять i-й слиток (если можно)
                if (gold[i - 1] <= weight) {
                    D[i][weight] = Math.max(D[i][weight],
                            D[i - 1][weight - gold[i - 1]] + gold[i - 1]);
                }
            }
        }

        return D[n][w];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}