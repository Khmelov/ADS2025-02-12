package by.it.group410901.kliaus.lesson07;

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
        if (one.isEmpty())
            return two.length();
        if (two.isEmpty())
            return one.length();

        int cost = (one.charAt(0) == two.charAt(0)) ? 0 : 1;

        return Math.min(
                Math.min(
                        getDistanceEdinting(one.substring(1), two) + 1,
                        getDistanceEdinting(one, two.substring(1)) + 1
                ),
                getDistanceEdinting(one.substring(1), two.substring(1)) + cost);
    }

    private int editDistance(String a, String b, int i, int j, int[][] memo) {
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        if (i == 0) {
            memo[i][j] = j;
        } else if (j == 0) {
            memo[i][j] = i;
        } else {
            int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;

            int insert = editDistance(a, b, i, j - 1, memo) + 1;
            int delete = editDistance(a, b, i - 1, j, memo) + 1;
            int substitute = editDistance(a, b, i - 1, j - 1, memo) + cost;

            memo[i][j] = Math.min(Math.min(insert, delete), substitute);
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
