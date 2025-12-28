package by.it.group410901.korneew.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

    public int memo[][];

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();
        memo = new int[n+1][m+1];

        for(int[] row: memo){
            Arrays.fill(row, -1);
        }
        return distance(one, two, n, m);
    }

    int distance(String one, String two, int i, int j){
        if (i == 0) return j;
        if (j == 0) return i;

        if (memo[i][j] != -1) return memo[i][j];

        if (one.charAt(i - 1) ==two.charAt(j - 1)) {
            memo[i][j] = distance(one, two, i - 1, j - 1);
        } else {
            memo[i][j] = 1 + Math.min(
                    Math.min(distance(one, two, i - 1, j),     // удаление
                            distance(one, two, i, j - 1)),    // вставка
                    distance(one, two, i - 1, j - 1)           // замена
            );
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
