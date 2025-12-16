package by.it.group410901.kliaus.lesson08;

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
        int n = scanner.nextInt();          // количество ступенек
        int stairs[] = new int[n];          // массив значений на ступеньках
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();  // считываем значения
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        if (n == 0) return 0; // нет ступенек, сумма 0

        // Массив dp, где dp[i] = максимальная сумма, достигаемая на ступеньке i
        int[] dp = new int[n];

        // Инициализация
        dp[0] = stairs[0]; // максимальная сумма на первой ступеньке = значение на ней
        if (n > 1) {
            dp[1] = Math.max(stairs[0] + stairs[1], stairs[1]);
            // для второй ступеньки: либо через 1 ступеньку, либо только с этой ступеньки
        }

        // Динамическое программирование для всех остальных ступенек
        for (int i = 2; i < n; i++) {
            // максимальная сумма на ступеньке i = максимум из двух вариантов:
            // 1) пришли с предыдущей ступеньки (i-1) + текущая
            // 2) пришли с через одну ступеньку (i-2) + текущая
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }

        int result = dp[n - 1]; // максимальная сумма на последней ступеньке
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }

}
