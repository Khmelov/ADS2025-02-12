package by.it.group451002.vishnevskiy.lesson08;

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
        int[] stairs = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();  // "ценность" каждой ступеньки
        }

        // Если ступенька одна — ответ это её значение
        if (n == 1) return stairs[0];

        // prev2 = лучший результат до (i-2)-й ступеньки
        int prev2 = stairs[0];

        // prev1 = лучший результат до (i-1)-й ступеньки
        // сюда можно попасть либо сразу (stairs[1]),
        // либо через первую (stairs[0] + stairs[1])
        int prev1 = stairs[1] + Math.max(0, stairs[0]);

        // идём по лестнице с 3-й ступеньки до n-й
        for (int i = 2; i < n; i++) {
            // лучший путь на i-ю ступеньку =
            // её значение + максимум из двух предыдущих вариантов
            int cur = stairs[i] + Math.max(prev1, prev2);

            // сдвигаем "окно"
            prev2 = prev1;
            prev1 = cur;
        }

        // на последней ступеньке хранится ответ
        return prev1;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
