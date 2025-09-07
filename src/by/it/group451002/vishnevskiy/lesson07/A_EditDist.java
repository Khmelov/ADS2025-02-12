package by.it.group451002.vishnevskiy.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {

    // Основной метод, который вызывается извне
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

    // Рекурсивная функция для вычисления расстояния редактирования
    int editDistance(String one, String two, int i, int j, int[][] memo) {
        // Если результат уже вычислен ранее, возвращаем его
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // Базовый случай: одна из строк пустая
        if (i == 0) {
            memo[i][j] = j; // Нужно j вставок
        } else if (j == 0) {
            memo[i][j] = i; // Нужно i удалений
        } else {
            // Если последние символы равны — просто рекурсивно вызываем для подстрок без последних символов
            if (one.charAt(i - 1) == two.charAt(j - 1)) {
                memo[i][j] = editDistance(one, two, i - 1, j - 1, memo);
            } else {
                // Иначе — берем минимум из трех возможных операций: вставка, удаление, замена
                int insert = editDistance(one, two, i, j - 1, memo);
                int delete = editDistance(one, two, i - 1, j, memo);
                int replace = editDistance(one, two, i - 1, j - 1, memo);
                memo[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
            }
        }

        // Возвращаем и запоминаем результат
        return memo[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
