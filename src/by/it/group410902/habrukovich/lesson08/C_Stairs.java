package by.it.group410902.habrukovich.lesson08;

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
        int result = 0;
        if (n > 0) {
            int[] dp = new int[n];

            dp[0] = stairs[0]; // Базовый случай: сумма на первой ступеньке - это значение этой ступеньки

            if (n > 1) {
                if (stairs[1]> stairs[0]+stairs[1]){
                    dp[1]= stairs[1];
                }else dp[1] = stairs[0] + stairs[1];

                for (int i = 2; i < n; i++) {

                    if (dp[i-1]>dp[i-2]){
                        dp[i]= stairs[i]+ dp[i-1];

                    }else {dp[i]= stairs[i] + dp[i-2];}

                }
                result = dp[n - 1]; // Результат - максимальная сумма на последней ступеньке
            } else {
                result = dp[0]; // Если всего одна ступенька
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
