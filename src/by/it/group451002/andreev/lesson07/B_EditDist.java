package by.it.group451002.andreev.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс B_EditDist реализует итерационное вычисление расстояния Левенштейна
 * с использованием динамического программирования.
 * Расстояние Левенштейна показывает минимальное количество операций (вставка, удаление, замена),
 * необходимых для превращения одной строки в другую.
 */
public class B_EditDist {

    /**
     * Метод getDistanceEditing выполняет вычисление расстояния Левенштейна итерационным способом.
     * @param one Первая строка
     * @param two Вторая строка
     * @return Минимальное количество операций для превращения одной строки в другую
     */
    int getDistanceEdinting(String one, String two) {
        // Получаем длины строк
        int m = one.length();
        int n = two.length();

        // Создаем таблицу размером (m+1) x (n+1) для хранения промежуточных расстояний
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация первой строки и первого столбца таблицы
        // Если одна из строк пустая, расстояние равно длине другой строки
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // удаление i символов
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // вставка j символов
        }

        // Заполняем таблицу динамического программирования
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы совпадают, просто берем предыдущее значение
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Иначе, берем минимум из трех возможных операций: вставка, удаление, замена
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j],     // удаление
                            Math.min(
                                    dp[i][j - 1], // вставка
                                    dp[i - 1][j - 1] // замена
                            )
                    );
                }
            }
        }

        // Результат — в правом нижнем углу таблицы, он показывает минимальное количество правок
        return dp[m][n];
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Читаем входные данные из файла "dataABC.txt"
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);

        // Вызываем расчет расстояния Левенштейна для трех пар строк и выводим результат
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
