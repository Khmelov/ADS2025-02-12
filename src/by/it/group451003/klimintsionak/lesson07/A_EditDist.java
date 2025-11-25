package by.it.group451003.klimintsionak.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    private int[][] memo;

    int getDistanceEdinting(String one, String two) {
        int len1 = one.length();
        int len2 = two.length();
        // Инициализируем массив мемоизации размером (len1+1)x(len2+1)
        memo = new int[len1 + 1][len2 + 1];

        // Заполняем массив -1, чтобы отличать непосещённые ячейки
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                memo[i][j] = -1;
            }
        }

        return editDistance(one, two, len1, len2);
    }

    private int editDistance(String one, String two, int i, int j) {
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        if (i == 0) {
            memo[i][j] = j;
        } else if (j == 0) {
            memo[i][j] = i;
        } else {
            int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;

            int insert = editDistance(one, two, i, j - 1) + 1;
            int delete = editDistance(one, two, i - 1, j) + 1;
            int replace = editDistance(one, two, i - 1, j - 1) + cost;

            memo[i][j] = Math.min(Math.min(insert, delete), replace);
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
