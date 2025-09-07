package by.it.group451003.bernat.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    // Поле для хранения мемоизации (таблицы результатов рекурсивных вызовов)
    private int[][] memo;

    // Метод для вычисления расстояния Левенштейна между строками one и two
    int getDistanceEdinting(String one, String two) {
        // Получаем длины строк
        int len1 = one.length();
        int len2 = two.length();
        // Инициализируем массив мемоизации размером (len1+1)x(len2+1)
        // +1 для учета пустых строк
        memo = new int[len1 + 1][len2 + 1];

        // Заполняем массив значениями -1, чтобы обозначить непосещенные ячейки
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                memo[i][j] = -1;
            }
        }

        // Вызываем рекурсивный метод для вычисления расстояния
        return editDistance(one, two, len1, len2);
    }

    // Рекурсивный метод для вычисления расстояния Левенштейна с мемоизацией
    private int editDistance(String one, String two, int i, int j) {
        // Если результат для текущих индексов уже вычислен, возвращаем его
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // Базовые случаи:
        // Если первая строка пуста (i == 0), нужно вставить j символов из второй строки
        if (i == 0) {
            memo[i][j] = j;
        }
        // Если вторая строка пуста (j == 0), нужно удалить i символов из первой строки
        else if (j == 0) {
            memo[i][j] = i;
        }
        // Основной случай: вычисляем минимальную стоимость операций
        else {
            // Определяем стоимость замены: 0, если символы совпадают, 1 — если различаются
            int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;

            // Вычисляем стоимость трех возможных операций:
            // 1. Вставка: добавляем символ из второй строки
            int insert = editDistance(one, two, i, j - 1) + 1;
            // 2. Удаление: убираем символ из первой строки
            int delete = editDistance(one, two, i - 1, j) + 1;
            // 3. Замена (или копирование, если символы совпадают)
            int replace = editDistance(one, two, i - 1, j - 1) + cost;

            // Сохраняем минимальную стоимость в таблицу мемоизации
            memo[i][j] = Math.min(Math.min(insert, delete), replace);
        }

        // Возвращаем результат для текущих индексов
        return memo[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataABC.txt"
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        // Создаем Scanner для чтения строк из файла
        Scanner scanner = new Scanner(stream);
        // Читаем и обрабатываем три пары строк, выводя расстояние Левенштейна для каждой
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}