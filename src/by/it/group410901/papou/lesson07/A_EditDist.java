package by.it.group410901.papou.lesson07;

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

    private Integer[][] memo; // Memoization table

    // Recursive function with memoization to compute Levenshtein distance
    private int levenshtein(String one, String two, int i, int j) {
        // Base cases: if one string is empty, return length of the other
        if (i == 0) return j;
        if (j == 0) return i;

        // Check memoization table
        if (memo[i][j] != null) {
            return memo[i][j];
        }

        // If characters match, no edit is needed
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = levenshtein(one, two, i - 1, j - 1);
        } else {
            // Take minimum of three operations: insertion, deletion, substitution
            int insert = levenshtein(one, two, i, j - 1) + 1;
            int delete = levenshtein(one, two, i - 1, j) + 1;
            int replace = levenshtein(one, two, i - 1, j - 1) + 1;
            memo[i][j] = Math.min(insert, Math.min(delete, replace));
        }

        return memo[i][j];
    }

    int getDistanceEdinting(String one, String two) {
        // Initialize memoization table
        memo = new Integer[one.length() + 1][two.length() + 1];
        // Compute Levenshtein distance
        return levenshtein(one, two, one.length(), two.length());
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