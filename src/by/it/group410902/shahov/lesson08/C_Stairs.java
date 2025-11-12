package by.it.group410902.shahov.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import static java.lang.Math.max;

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
        int max = 0;
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
            if (stairs[i] > max) max = stairs[i];
        }
        scanner.close();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int result = 0;
        int[] dp = new int[n + 1];
        dp[0] = stairs[0];
        dp[1] = max(stairs[0] + stairs[1], stairs[1]);
        for (int i = 2; i < stairs.length; i++){
            dp[i] = max(dp[i - 1] + stairs[i], dp[i - 2] + stairs[i]);
        }
        result = dp[0];
        for(int i = 0; i < dp.length; i++){
            if(result < dp[i]){
                result = dp[i];
            }
        }




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
