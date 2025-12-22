package by.it.group451004.volynets.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n=scanner.nextInt();
        int stairs[]=new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        if (n == 0) return 0;
        if (n == 1) return Math.max(0, stairs[0]);

        int[] dp = new int[n];
        dp[0] = stairs[0];
        if (n > 1) {
            dp[1] = Math.max(stairs[0] + stairs[1], stairs[1]);
        }

        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i-1], dp[i-2]) + stairs[i];
        }

        result = dp[n-1];

        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}