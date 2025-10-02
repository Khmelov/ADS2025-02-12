package by.it.group410902.derzhavskaya_e.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/* Задача на программирование: расстояние Левенштейна
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
        int n = one.length();
        int m = two.length();
        int[][] memo = new int[n + 1][m + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        int result = rec(0, 0, one, two, memo);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int rec(int i, int j, String a, String b, int[][] memo) {
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        int n = a.length();
        int m = b.length();
        int res;
        if (i == n) {
            res = m - j;
        } else if (j == m) {
            res = n - i;
        } else {
            int costDelete = rec(i + 1, j, a, b, memo) + 1;
            int costInsert = rec(i, j + 1, a, b, memo) + 1;
            int costReplace = rec(i + 1, j + 1, a, b, memo) + (a.charAt(i) == b.charAt(j) ? 0 : 1);
            res = Math.min(Math.min(costDelete, costInsert), costReplace);
        }
        memo[i][j] = res;
        return res;
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