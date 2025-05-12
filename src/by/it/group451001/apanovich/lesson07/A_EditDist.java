package by.it.group451001.apanovich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;
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

    private int[][] memo;

    private int dp(String a, String b, int i, int j) {
        if (i == 0) return j;
        if (j == 0) return i;
        if (memo[i][j] != -1) return memo[i][j];
        int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
        int delete = dp(a, b, i - 1, j) + 1;
        int insert = dp(a, b, i, j - 1) + 1;
        int replace = dp(a, b, i - 1, j - 1) + cost;
        int res = Math.min(delete, Math.min(insert, replace));
        memo[i][j] = res;
        return res;
    }

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();
        memo = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            Arrays.fill(memo[i], -1);
        }
        return dp(one, two, n, m);
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
