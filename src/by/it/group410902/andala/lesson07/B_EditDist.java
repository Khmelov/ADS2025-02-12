package by.it.group410902.andala.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    // Метод для вычисления редакционного расстояния между двумя строками
    int getDistanceEdinting(String one, String two) {
        int m = one.length(); // длина первой строки
        int n = two.length(); // длина второй строки

        // dp[i][j] — минимальное количество операций, чтобы преобразовать первые i символов строки one
        // в первые j символов строки two
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация первой строки таблицы:
        // dp[i][0] — удаление i символов из строки one
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        // Инициализация первого столбца таблицы:
        // dp[0][j] — вставка j символов в строку one, чтобы получить строку two
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполнение всей таблицы
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы совпадают, операция не требуется
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Иначе выбираем минимальную стоимость из:
                    // замены (dp[i-1][j-1] + 1),
                    // вставки (dp[i][j-1] + 1),
                    // удаления (dp[i-1][j] + 1)
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1], // замена
                            Math.min(
                                    dp[i][j - 1], // вставка
                                    dp[i - 1][j]  // удаление
                            )
                    );
                }
            }
        }

        // Возвращаем минимальное количество операций для преобразования всей строки one в two
        return dp[m][n];
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Подгружаем входной файл с парами строк
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);

        // Обрабатываем 3 строки из файла (в парах) и выводим редакционное расстояние
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
