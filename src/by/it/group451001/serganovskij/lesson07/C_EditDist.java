package by.it.group451001.serganovskij.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
Необходимо:
  Вычислить редакционное предписание преобразования строки one в строку two.
  Используется динамическое программирование и восстановление пути.

Обозначения операций:
  "+" — вставка символа (insert)
  "-" — удаление символа (delete)
  "~" — замена символа (replace)
  "#" — копирование символа (match)
*/

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        // Строка результата, в которую будем собирать редакционные действия
        String result = "";

        int n = one.length(); // Длина первой строки
        int m = two.length(); // Длина второй строки

        // Таблица для хранения минимального количества операций
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация первой строки: превращение пустой строки в two (вставки)
        for (int i = 0; i <= m; i++) {
            dp[0][i] = i;
        }

        // Инициализация первого столбца: превращение one в пустую строку (удаления)
        for (int j = 0; j <= n; j++) {
            dp[j][0] = j;
        }

        // Заполнение таблицы DP
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int diff = 1; // Стоимость замены

                // Если символы совпадают — замена не нужна
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    diff = 0;
                }

                // Выбор минимальной стоимости из трёх возможных операций
                dp[i][j] = Math.min(
                        dp[i - 1][j] + 1, // удаление
                        Math.min(
                                dp[i][j - 1] + 1, // вставка
                                dp[i - 1][j - 1] + diff // замена или копирование
                        )
                );
            }
        }

        // Восстановление редакционного пути из таблицы DP (начиная с правого нижнего угла)
        int i = n, j = m;
        while (i > 0 || j > 0) {
            // Проверка, была ли вставка символа из two
            if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                result = "+" + two.charAt(j - 1) + "," + result;
                --j;
                continue;
            }

            // Проверка, было ли удаление символа из one
            if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                result = "-" + one.charAt(i - 1) + "," + result;
                --i;
                continue;
            }

            // Проверка, была ли замена символа
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                result = "~" + two.charAt(j - 1) + "," + result;
                --i;
                --j;
                continue;
            }

            // Если символы совпали — копирование
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1]) {
                result = "#," + result;
                --i;
                --j;
            }
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение строк из файла
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);

        // Считываем пары строк и выводим редакционное предписание
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
