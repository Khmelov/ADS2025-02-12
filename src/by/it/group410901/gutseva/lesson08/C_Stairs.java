package by.it.group410901.gutseva.lesson08;

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
        if (n == 0) return 0;

        // dp[i] = максимальная сумма, если стоим на i-й ступеньке
        int[] dp = new int[n];

        // базовый случай — первая ступенька
        dp[0] = stairs[0];

        // если есть хотя бы две ступеньки
        if (n > 1) {
            // либо идём сразу на вторую, либо через первую
            dp[1] = Math.max(stairs[1], stairs[0] + stairs[1]);
        }

        // основной цикл: на i-ю ступеньку можно попасть с i-1 или i-2
        for (int i = 2; i < n; i++) {
            //выбираем, откуда выгоднее, и прибавляем число этой ступеньки
            dp[i] = Math.max(dp[i-1], dp[i-2]) + stairs[i];
        }

        int result = dp[n-1]; // итог — максимум на последней ступеньке
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
