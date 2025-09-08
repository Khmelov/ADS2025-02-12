package by.it.group451001.serganovskij.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна

Описание:
Найти минимальное количество операций (вставка, удаление, замена),
необходимых для преобразования одной строки в другую.

Метод:
Используется классическое динамическое программирование:
dp[i][j] — минимальное количество операций, чтобы преобразовать
первые i символов строки one в первые j символов строки two.
*/

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int result = 0;

        int n = one.length(); // Длина первой строки
        int m = two.length(); // Длина второй строки

        // Создаем таблицу DP размера (n+1)x(m+1)
        int[][] dp = new int[n + 1][m + 1];

        // Заполняем первую строку таблицы (преобразование пустой строки в two)
        for (int i = 0; i <= m; i++) {
            dp[0][i] = i; // Нужно i вставок
        }

        // Заполняем первый столбец таблицы (преобразование one в пустую строку)
        for (int j = 0; j <= n; j++) {
            dp[j][0] = j; // Нужно j удалений
        }

        // Заполняем основную часть таблицы
        for (int i = 1; i <= n; i++) {       // По символам строки one
            for (int j = 1; j <= m; j++) {   // По символам строки two

                int diff = 1; // Стоимость замены по умолчанию

                // Если символы совпадают — замена не нужна
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    diff = 0;
                }

                // Выбираем минимальную стоимость из:
                // - удаления: dp[i - 1][j] + 1
                // - вставки:  dp[i][j - 1] + 1
                // - замены (или ничего, если diff == 0): dp[i - 1][j - 1] + diff
                dp[i][j] = Math.min(
                        dp[i - 1][j] + 1, // удаление
                        Math.min(
                                dp[i][j - 1] + 1, // вставка
                                dp[i - 1][j - 1] + diff // замена
                        )
                );
            }
        }

        // Результат — в правом нижнем углу таблицы
        result = dp[n][m];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение строк из файла
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);

        // Считываем пары строк и выводим расстояние Левенштейна
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}
