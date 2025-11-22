package by.it.group451003.kharkevich.lesson08;

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
        int[] dp = new int[n + 1]; /* dp[i] будет хранить максимальную сумму, которую можно получить, дойдя до i-й ступеньки
        Размер n + 1 начальная позиция*/
        dp[0] = 0;
        dp[1] = stairs[0];

        for (int i = 2; i <= n; i++) { //вычисления максимальных сумм для всех ступенек
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i - 1]; /*
            dp[i - 1] — максимальная сумма, если пришли на i-ю ступеньку с предыдущей (шаг 1 ступеньку)
            dp[i - 2] — максимальная сумма, если пришли на i-ю ступеньку через одну (шаг 2 ступеньки)
            Math.max(dp[i - 1], dp[i - 2]) — выбираем лучший путь к текущей ступеньке
            + stairs[i - 1] — добавляем значение текущей ступеньки*/
        }

        return dp[n];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
