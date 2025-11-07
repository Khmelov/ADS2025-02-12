package by.it.group410901.zubchonak.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Даны число 1<=n<=100 ступенек лестницы и
целые числа −10000<=a[1],…,a[n]<=10000, которыми помечены ступеньки.
Найдите максимальную сумму, которую можно получить, идя по лестнице
снизу вверх (от нулевой до n-й ступеньки), каждый раз поднимаясь на
одну или на две ступеньки.

Sample Input 1:
2
1 2
Sample Output 1:
3

Sample Input 2:
2
2 -1
Sample Output 2:
1

Sample Input 3:
3
-1 2 1
Sample Output 3:
3

*/

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n=scanner.nextInt();
        int stairs[]=new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int[] dp = new int[n + 1];
        if (n == 0) {
            return 0;
        }

        for (int i = 1; i <= n; i++) {
            int currentStairValue = stairs[i - 1];
            int sumFromPrevious = Integer.MIN_VALUE;
            if (i >= 1) {
                if (i == 1) {
                    sumFromPrevious = dp[0] + currentStairValue;
                } else {
                    if (dp[i - 1] != Integer.MIN_VALUE) {
                        sumFromPrevious = dp[i - 1] + currentStairValue;
                    }
                }
            }
            int sumFromTwoStepsAgo = Integer.MIN_VALUE;
            if (i >= 2) {

                if (dp[i - 2] != Integer.MIN_VALUE) {
                    sumFromTwoStepsAgo = dp[i - 2] + currentStairValue;
                }
            }

            if (i == 1) {
                dp[i] = sumFromPrevious;
            } else {
                dp[i] = Math.max(sumFromPrevious, sumFromTwoStepsAgo);
            }
        }

        if (n == 0) {
            return 0;
        }

        int[] dpCorrect = new int[n + 1];
        dpCorrect[0] = 0;
        dpCorrect[1] = stairs[0];
        for (int i = 2; i <= n; i++) {
            dpCorrect[i] = Math.max(dpCorrect[i - 1], dpCorrect[i - 2]) + stairs[i - 1];
        }
        int result = dpCorrect[n];




        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
