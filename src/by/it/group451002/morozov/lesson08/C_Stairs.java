package by.it.group451002.morozov.lesson08;

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
        int result = 0;


        int i = -1;	// Индекс текущей ступеньки. -1 означает начало пути
        while (i < n-2) {
        	
        	// Переходим через 2 ступеньки
        	if ((stairs[i+2] > stairs[i+1]) && (stairs[i+2] > stairs[i+1]+stairs[i+2])) {
        		result += stairs[i+2];
        		i += 2;
        		
        	// Проходим на 1 ступеньку
        	} else if (stairs[i+1] > stairs[i+1]+stairs[i+2]) {
        		result += stairs[i+1];
        		i += 1;
        	} else {
        		
        		// Проходим по двум ступенькам
        		result += stairs[i+1] + stairs[i+2];
        		i += 2;
        	}
        }
        
        // Принимаем решение о последней ступеньке, если можно её пройти
        if (stairs[n-1] > 0 && (i < n-1)) {
        	result += stairs[n-1];
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
