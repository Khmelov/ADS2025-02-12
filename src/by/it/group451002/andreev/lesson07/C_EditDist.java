package by.it.group451002.andreev.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс C_EditDist реализует итерационное вычисление расстояния Левенштейна
 * и восстановление редакционного предписания для преобразования одной строки в другую.
 * Используется динамическое программирование.
 */
public class C_EditDist {

    /**
     * Метод getDistanceEdinting вычисляет расстояние Левенштейна и формирует последовательность редактирования.
     * @param one Первая строка
     * @param two Вторая строка
     * @return Редакционное предписание (последовательность операций преобразования)
     */
    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        // Создаем двумерную таблицу dp[m+1][n+1] для хранения промежуточных расстояний редактирования
        int[][] dp = new int[m + 1][n + 1];

        // Заполняем базовые случаи: если одна из строк пустая,
        // количество операций равно длине другой строки (удаление или вставка)
        for (int i = 0; i <= m; i++) dp[i][0] = i; // Удаление всех символов из первой строки
        for (int j = 0; j <= n; j++) dp[0][j] = j; // Вставка всех символов второй строки

        // Заполняем таблицу динамического программирования
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Если символы равны, просто копируем значение из dp[i-1][j-1]
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Если символы различаются, выбираем минимальную операцию (вставка, удаление или замена)
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j], // Удаление
                            Math.min(
                                    dp[i][j - 1], // Вставка
                                    dp[i - 1][j - 1] // Замена
                            )
                    );
                }
            }
        }

        // Восстанавливаем последовательность операций преобразования (редакционное предписание)
        StringBuilder result = new StringBuilder();
        int i = m;
        int j = n;

        while (i > 0 || j > 0) {
            // Копирование (если символы одинаковые)
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                result.insert(0, "#,");
                i--;
                j--;
            }
            // Замена (если требуется заменить символ)
            else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                result.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            }
            // Удаление (если нужно удалить символ из первой строки)
            else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            // Вставка (если нужно добавить символ из второй строки)
            else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        return result.toString(); // Возвращаем последовательность операций
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Читаем входные данные из файла "dataABC.txt"
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);

        // Выполняем вычисление редакционного предписания для трех пар строк и выводим результат
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
