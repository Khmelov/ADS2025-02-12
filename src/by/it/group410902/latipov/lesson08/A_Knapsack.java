package by.it.group410902.latipov.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt(); // вместимость рюкзака
        int n = scanner.nextInt(); // количество вариантов слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] - максимальный вес, который можно набрать для вместимости i
        boolean[] dp = new boolean[W + 1];
        dp[0] = true; // рюкзак вместимостью 0 всегда можно заполнить

        // Заполняем массив dp
        for (int i = 1; i <= W; i++) {
            for (int j = 0; j < n; j++) {
                if (gold[j] <= i && dp[i - gold[j]]) {
                    dp[i] = true;
                }
            }
        }

        // Находим максимальную вместимость, которую можно достичь
        int result = 0;
        for (int i = W; i >= 0; i--) {
            if (dp[i]) {
                result = i;
                break;
            }
        }

        return result;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}