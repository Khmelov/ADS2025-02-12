package by.it.group410901.garkusha.lesson07;

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


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int m = one.length();
        int n = two.length();

        // Создаем таблицу для хранения результатов подзадач
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация таблицы
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = -1; // Помечаем как невычисленное
            }
        }

        int result = calculateDistance(one, two, m, n, dp);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int calculateDistance(String one, String two, int i, int j, int[][] dp) {
        // Если результат уже вычислен, возвращаем его
        if (dp[i][j] != -1) {
            return dp[i][j];
        }

        // Базовые случаи
        if (i == 0) {
            dp[i][j] = j;
            return j;
        }
        if (j == 0) {
            dp[i][j] = i;
            return i;
        }

        // Если последние символы совпадают, рекурсивно вычисляем для оставшихся строк
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            dp[i][j] = calculateDistance(one, two, i - 1, j - 1, dp);
            return dp[i][j];
        }

        // Если символы не совпадают, находим минимальную стоимость операций
        int insert = calculateDistance(one, two, i, j - 1, dp);
        int delete = calculateDistance(one, two, i - 1, j, dp);
        int replace = calculateDistance(one, two, i - 1, j - 1, dp);

        dp[i][j] = 1 + Math.min(Math.min(insert, delete), replace);
        return dp[i][j];
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
