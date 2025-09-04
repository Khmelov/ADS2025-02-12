package by.it.group410902.grigorev.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {
    int getDistanceEdinting(String one, String two) {
        // Определяем размеры строк
        int m = one.length();
        int n = two.length();

        // Создаем массив dp[m+1][n+1], где dp[i][j] хранит расстояние редактирования между
        // первыми i символами `one` и первыми j символами `two`
        int[][] dp = new int[m+1][n+1];

        // Заполняем таблицу dp
        for(int i = 0; i <= m; i++) {
            for(int j = 0; j <= n; j++) {
                if(i == 0) {
                    // Если первая строка пустая, то нужно j операций вставки
                    dp[i][j] = j;
                }
                else if(j == 0) {
                    // Если вторая строка пустая, то нужно i операций удаления
                    dp[i][j] = i;
                }
                else if(one.charAt(i-1) == two.charAt(j-1)) {
                    // Если символы совпадают, расстояние не увеличивается
                    dp[i][j] = dp[i-1][j-1];
                }
                else {
                    // Иначе берем минимум из трех операций (вставка, удаление, замена)
                    dp[i][j] = 1 + Math.min(Math.min(dp[i][j-1], dp[i-1][j]), dp[i-1][j-1]);
                }
            }
        }
        return dp[m][n]; // Итоговое расстояние редактирования
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);

        // Вычисляем расстояние редактирования для трех пар строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
