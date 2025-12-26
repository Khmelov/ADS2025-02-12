package by.it.group410901.korneew.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;

/*
Задача на программирование: рюкзак без повторов
...
*/

public class B_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        java.util.Scanner scanner = new java.util.Scanner(stream);
        int w = scanner.nextInt();
        int n = scanner.nextInt();
        int gold[] = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        boolean[] dp = new boolean[w + 1];
        dp[0] = true;
        for (int i = 0; i < n; i++) {
            int weight = gold[i];
            if (weight > w) continue;
            for (int cap = w; cap >= weight; cap--) {
                if (dp[cap - weight]) dp[cap] = true;
            }
        }

        int result = 0;
        for (int i = w; i >= 0; i--) {
            if (dp[i]) { result = i; break; }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }

}