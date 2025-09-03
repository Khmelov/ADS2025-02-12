package by.it.group451002.mishchenko.lesson07;

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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Обозначим:
        // n - длина первой строки, m - длина второй строки
        int n = one.length();
        int m = two.length();
        // Создаем двумерный массив dp размером (n+1) x (m+1)
        // dp[i][j] будет хранить минимальное количество операций,
        // необходимых для преобразования первых i символов строки one в первые j символов строки two.
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация базовых случаев:
        // Если первая строка пуста (i == 0), для достижения второй строки нужно выполнить j вставок.
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }
        // Если вторая строка пуста (j == 0), для преобразования нужно i удалений.
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        // Основной цикл: заполняем таблицу dp итерационно.
        // Перебираем все позиции для i (от 1 до n) и j (от 1 до m)
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Если символы на позициях совпадают: просто переходим к предыдущим подстрокам.
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Если символы различаются, рассматриваем три операции:
                    // 1. Замена символа: dp[i-1][j-1] + 1,
                    // 2. Удаление символа из строки one: dp[i-1][j] + 1,
                    // 3. Вставка символа в строку one: dp[i][j-1] + 1.
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + 1,
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }

        int result = dp[n][m];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}
