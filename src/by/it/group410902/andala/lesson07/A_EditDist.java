package by.it.group410902.andala.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    // Метод для вычисления минимального количества операций преобразования строки one в two
    int getDistanceEdinting(String one, String two) {
        int n = one.length(); // длина первой строки
        int m = two.length(); // длина второй строки

        // dp[i][j] — минимальное количество операций для преобразования первых i символов one в j символов two
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация: преобразование строки длины i в пустую строку требует i удалений
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        // Инициализация: преобразование пустой строки в строку длины j требует j вставок
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        // Основное заполнение таблицы динамического программирования
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Если текущие символы совпадают, стоимость — 0, иначе — 1
                int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;

                // Выбираем минимальную стоимость из трех возможных операций:
                // - удаление (dp[i-1][j] + 1)
                // - вставка (dp[i][j-1] + 1)
                // - замена (dp[i-1][j-1] + cost)
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,      // удаление
                                dp[i][j - 1] + 1),     // вставка
                        dp[i - 1][j - 1] + cost         // замена (или отсутствие замены, если символы совпадают)
                );
            }
        }

        // Итоговое значение — минимальное количество операций для преобразования всей строки one в two
        return dp[n][m];
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Считываем пары строк из файла dataABC.txt
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);

        // Выводим результат для каждой пары строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
