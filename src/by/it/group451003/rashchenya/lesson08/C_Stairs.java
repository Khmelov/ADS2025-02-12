package by.it.group451003.rashchenya.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.lang.Math;

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
        //Мы можем прийти на текущую ступеньку двумя способами
        //С предыдущей ступеньки (шаг 1): stairs[i-1] + stairs[i]
        for (int i = 1; i < stairs.length; i++){
            if (i == 1) {
                stairs[i] = Math.max(stairs[i], stairs[i] + stairs[i - 1]);
            } //С пред-предыдущей ступеньки (шаг 2): stairs[i-2] + stairs[i]
            else {
                stairs[i] = Math.max(stairs[i - 1] + stairs[i], stairs[i] + stairs[i - 2]);
            }
        } //Выбираем максимальный из этих двух вариантов
        int result = stairs[stairs.length - 1];




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
