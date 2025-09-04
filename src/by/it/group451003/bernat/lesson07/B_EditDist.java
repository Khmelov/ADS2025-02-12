package by.it.group451003.bernat.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    // Метод для вычисления расстояния Левенштейна между строками one и two
    int getDistanceEdinting(String one, String two) {
        // Получаем длины строк
        int len1 = one.length();
        int len2 = two.length();

        // Создаем таблицу динамического программирования размером (len1+1)x(len2+1)
        // dp[i][j] хранит минимальное количество операций для преобразования
        // первых i символов строки one в первые j символов строки two
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Инициализируем первую строку и первый столбец
        // dp[i][0] — стоимость удаления i символов из первой строки
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        // dp[0][j] — стоимость вставки j символов из второй строки
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        // Заполняем таблицу динамического программирования
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                // Определяем стоимость замены: 0, если символы совпадают, 1 — если различаются
                int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;

                // Выбираем минимальную стоимость из трех операций:
                // 1. Удаление: стоимость предыдущего состояния + 1
                // 2. Вставка: стоимость предыдущего состояния + 1
                // 3. Замена (или копирование, если символы совпадают)
                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,        // удаление
                                dp[i][j - 1] + 1         // вставка
                        ),
                        dp[i - 1][j - 1] + cost         // замена
                );
            }
        }

        // Возвращаем расстояние Левенштейна — значение в правом нижнем углу таблицы
        return dp[len1][len2];
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataABC.txt"
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        // Создаем Scanner для чтения строк из файла
        Scanner scanner = new Scanner(stream);
        // Читаем и обрабатываем три пары строк, выводя расстояние Левенштейна для каждой
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}