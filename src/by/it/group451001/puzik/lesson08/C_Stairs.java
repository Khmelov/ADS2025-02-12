package by.it.group451001.puzik.lesson08;

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
        // dp[i] = maximum sum achievable reaching step i (0-indexed in stairs array)
        // We start at step 0 (value 0) and need to reach step n
        // stairs[i] is the value at step (i+1)
        // Can step 1 or 2 stairs at a time
        if (n == 0) {
            return 0;
        }
        
        // dp[i] represents maximum sum to reach step (i+1)
        int[] dp = new int[n];
        dp[0] = stairs[0]; // Step 1
        
        if (n > 1) {
            // For step 2: can come from step 0 (2 steps) or step 1 (1 step)
            dp[1] = Math.max(stairs[1], stairs[0] + stairs[1]);
        }
        
        for (int i = 2; i < n; i++) {
            // Can come from step i-1 (1 step) or step i-2 (2 steps)
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }
        
        int result = dp[n - 1];
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
