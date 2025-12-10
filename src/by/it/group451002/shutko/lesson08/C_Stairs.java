package by.it.group451002.shutko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Даны число 1<=n<=100 ступенек лестницы и
целые числа −10000<=a[1],…,a[n]<=10000, которыми помечены ступеньки (Как положительные так и отрицательные ).
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

    int getMaxSum(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int stairs[] = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }

        if (n == 0) return 0;  // Если ступенек нет, сумма 0.
        if (n == 1) return stairs[0]; // Если одна ступенька, вернуть её ценность.

        // Инициализация для первых двух ступенек
        int prev2 = stairs[0];          // для i-2
        int prev1 = Math.max(stairs[1], stairs[0] + stairs[1]); // для i-1

        // Цикл для i от 2 до n-1 (для 3-ей и последующих ступенек)
        for (int i = 2; i < n; i++) {
            int current = Math.max(prev1, prev2) + stairs[i]; // Основная формула
            // Сдвигаем указатели для следующей итерации
            prev2 = prev1; // теперь prev2 - это бывший dp[i-1]
            prev1 = current; // теперь prev1 - это только что вычисленный dp[i]
        }

        return prev1;
    }
}
