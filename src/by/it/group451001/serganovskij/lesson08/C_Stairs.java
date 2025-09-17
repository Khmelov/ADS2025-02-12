package by.it.group451001.serganovskij.lesson08;

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

        // Если ступенек нет, возвращаем 0
        if (n == 0) {
            return 0;
        }
        // Если только одна ступенька, возвращаем её значение (если оно положительное) или 0
        if (n == 1) {
            return Math.max(stairs[0], 0);
        }

        // Создаем массив для хранения максимальных сумм на каждой ступеньке
        int[] dp = new int[n];

        // Базовые случаи:
        // На первой ступеньке максимальная сумма - это значение первой ступеньки
        dp[0] = stairs[0];
        // На второй ступеньке максимальная сумма - это либо прямое восхождение на вторую,
        // либо через первую (но только если это дает большую сумму)
        dp[1] = Math.max(stairs[0] + stairs[1], stairs[1]);

        // Заполняем массив для остальных ступенек
        for (int i = 2; i < n; i++) {
            // На i-ю ступеньку можно попасть либо с (i-1)-й, либо с (i-2)-й
            // Выбираем тот путь, который дает максимальную сумму
            dp[i] = Math.max(dp[i - 1] + stairs[i], dp[i - 2] + stairs[i]);
        }

        // Максимальная сумма будет на последней ступеньке
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
