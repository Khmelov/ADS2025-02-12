package by.it.group410901.borisdubinin.lesson08;

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
        int numberOfSteps=scanner.nextInt();
        int points[]=new int[numberOfSteps];
        for (int i = 0; i < numberOfSteps; i++) {
            points[i]=scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int[] maxPossiblePoints = new int[numberOfSteps + 1];
        maxPossiblePoints[0] = 0;
        maxPossiblePoints[1] = points[0];

        for(int i = 2; i < maxPossiblePoints.length; i++){
            if(maxPossiblePoints[i - 1] > maxPossiblePoints[i - 2])
                maxPossiblePoints[i] = maxPossiblePoints[i - 1] + points[i - 1];
            else
                maxPossiblePoints[i] = maxPossiblePoints[i - 2] + points[i - 1];
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return maxPossiblePoints[numberOfSteps];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
