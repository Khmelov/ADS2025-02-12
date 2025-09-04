package by.it.group451002.andreev.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс A_EditDist реализует вычисление расстояния Левенштейна
 * с использованием рекурсивного подхода и мемоизации (сохранения промежуточных результатов).
 * Расстояние Левенштейна показывает минимальное количество операций вставки, удаления или замены,
 * необходимых для превращения одной строки в другую.
 */
public class A_EditDist {

    /**
     * Основной метод, который вызывается для расчета расстояния Левенштейна.
     * @param one Первая строка
     * @param two Вторая строка
     * @return Минимальное количество операций для превращения одной строки в другую
     */
    int getDistanceEdinting(String one, String two) {
        // Создаем двумерный массив для хранения промежуточных результатов
        // Размерность: (длина строки one + 1) x (длина строки two + 1)
        int[][] memo = new int[one.length() + 1][two.length() + 1];

        // Инициализируем массив значениями -1, что означает, что результат еще не вычислен
        for (int i = 0; i <= one.length(); i++) {
            for (int j = 0; j <= two.length(); j++) {
                memo[i][j] = -1;
            }
        }

        // Запускаем рекурсивную функцию с мемоизацией
        return editDistance(one, two, one.length(), two.length(), memo);
    }

    /**
     * Рекурсивная функция для вычисления расстояния редактирования с мемоизацией.
     * @param one Первая строка
     * @param two Вторая строка
     * @param i Текущий индекс в первой строке
     * @param j Текущий индекс во второй строке
     * @param memo Двумерный массив для хранения промежуточных результатов
     * @return Минимальное количество операций для превращения одной строки в другую
     */
    int editDistance(String one, String two, int i, int j, int[][] memo) {
        // Если результат уже вычислен ранее, возвращаем его
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // Базовый случай: если одна из строк пустая
        if (i == 0) {
            memo[i][j] = j; // Нужно j вставок (если первая строка пустая)
        } else if (j == 0) {
            memo[i][j] = i; // Нужно i удалений (если вторая строка пустая)
        } else {
            // Если последние символы равны — просто вызываем рекурсию для подстрок без этих символов
            if (one.charAt(i - 1) == two.charAt(j - 1)) {
                memo[i][j] = editDistance(one, two, i - 1, j - 1, memo);
            } else {
                // Иначе берем минимум из трех возможных операций: вставка, удаление, замена
                int insert = editDistance(one, two, i, j - 1, memo);
                int delete = editDistance(one, two, i - 1, j, memo);
                int replace = editDistance(one, two, i - 1, j - 1, memo);
                memo[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
            }
        }

        // Запоминаем и возвращаем результат
        return memo[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входных данных из файла "dataABC.txt"
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);

        // Вызываем расчет расстояния Левенштейна для трех пар строк и выводим результат
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
