package by.it.group451003.kuzhik.lesson07;

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

    int[][] memo;  // Таблица для хранения промежуточных результатов

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        memo = new int[n + 1][m + 1];

        // Заполняем таблицу "значениями по умолчанию"
        for (int i = 0; i <= n; i++)
            for (int j = 0; j <= m; j++)
                memo[i][j] = -1;

        return dp(one, two, n, m);
    }

    int dp(String one, String two, int i, int j) {
        // Базовые случаи
        if (i == 0) return j;
        if (j == 0) return i;

        // Уже посчитано
        if (memo[i][j] != -1) return memo[i][j];

        // Если последние символы равны — не нужно менять
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = dp(one, two, i - 1, j - 1);
        } else {
            int insert = dp(one, two, i, j - 1) + 1;
            int delete = dp(one, two, i - 1, j) + 1;
            int replace = dp(one, two, i - 1, j - 1) + 1;
            memo[i][j] = Math.min(insert, Math.min(delete, replace));
        }

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

