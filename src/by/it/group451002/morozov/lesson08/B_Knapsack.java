package by.it.group451002.morozov.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак без повторов

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        число золотых слитков
                    (каждый можно использовать только один раз).
Следующая строка содержит n целых чисел, задающих веса каждого из слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.

Sample Input:
10 3
1 4 8
Sample Output:
9

*/

public class B_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }


        int result = 0;
        
        // Создаём и инициализируем матрицу для сохранения пар D[w, i]
        int[][] D = new int[w+1][n+1];
        for (int i = 0; i <= w; i++) {
        	for (int j = 0; j <= n; j++) {
        		D[i][j] = 0;
        	}
        }
        
        // Основной алгоритм. Считаем стоимость равной весу
        for (int i = 1; i <= n; i++) {
        	for (int wt = 1; wt <= w; wt++) {
        		D[wt][i] = D[wt][i-1];
        		if (gold[i-1] <= wt) {
	        		if (D[wt-gold[i-1]][i-1]+gold[i-1] > D[wt][i])
	        			D[wt][i] = D[wt-gold[i-1]][i-1]+gold[i-1];
        		}
        	}
        }
        
        result = D[w][n];
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
