package by.it.group451001.kazakov.lesson08;

import java.io.FileInputStream;
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

        // если ступенек нет — сумма равна 0
        if (n == 0) return 0;

        // если всего одна ступенька — максимум это её значение
        if (n == 1) return stairs[0];

        // массив D, где D[i] = максимальная сумма до i-й ступеньки включительно
        int[] D = new int[n];

        // базовые случаи
        D[0] = stairs[0]; // на первую ступеньку можно попасть только напрямую
        D[1] = Math.max(stairs[0] + stairs[1], stairs[1]);
        // на вторую можно попасть:
        // 1) через первую (stairs[0] + stairs[1])
        // 2) сразу с нуля (stairs[1])
        // выбираем максимум

        // заполняем динамику для остальных ступенек
        for (int i = 2; i < n; i++) {
            // на i-ю ступеньку можно попасть либо с (i-1), либо с (i-2)
            // выбираем максимум из этих двух и добавляем значение текущей ступеньки
            D[i] = Math.max(D[i - 1], D[i - 2]) + stairs[i];
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return D[n-1];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}