package by.it.group451003.sorokin.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        int lenOne = one.length();
        int lenTwo = two.length();

        // Создаем двумерный массив для хранения расстояний
        int[][] dp = new int[lenOne + 1][lenTwo + 1];

        // Инициализация первой строки и первого столбца
        for (int i = 0; i <= lenOne; i++) {
            dp[i][0] = i; // удаление всех символов из one
        }
        for (int j = 0; j <= lenTwo; j++) {
            dp[0][j] = j; // добавление всех символов из two
        }

        // Заполняем массив dp
        for (int i = 1; i <= lenOne; i++) {
            for (int j = 1; j <= lenTwo; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // символы совпадают
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1,    // удаление
                                    dp[i][j - 1] + 1),   // добавление
                            dp[i - 1][j - 1] + 1); // замена
                }
            }
        }

        // Возвращаем расстояние редактирования
        return dp[lenOne][lenTwo];
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
