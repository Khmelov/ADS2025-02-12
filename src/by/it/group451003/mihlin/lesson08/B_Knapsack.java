package by.it.group451003.mihlin.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++)
            gold[i] = scanner.nextInt();


        int[] dp = new int[W + 1];

        for (int i = 0; i < n; i++) {
            int weight = gold[i];
            for (int j = W; j >= weight; j--)
                dp[j] = Math.max(dp[j], dp[j - weight] + weight);

        }

        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
