package by.it.group410901.abakumov.lesson07;

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
    int[][] dp;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int result = 0;
        int m = one.length();
        int n = two.length();
        dp = new int[m + 1][n + 1];

        // Заполняем массив значением -1, чтобы отслеживать необработанные состояния
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = -1;
            }
        }

        result = compute(one, two, m, n);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    int compute(String s1, String s2, int i, int j) {
        // Если одна из строк пустая — расстояние это длина другой строки
        if (i == 0) return j;
        if (j == 0) return i;

        // Если уже вычислено — возвращаем сохраненное значение
        if (dp[i][j] != -1) return dp[i][j];

        int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

        dp[i][j] = Math.min(
                Math.min(
                        compute(s1, s2, i - 1, j) + 1,     // удаление
                        compute(s1, s2, i, j - 1) + 1      // вставка
                ),
                compute(s1, s2, i - 1, j - 1) + cost       // замена
        );

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
