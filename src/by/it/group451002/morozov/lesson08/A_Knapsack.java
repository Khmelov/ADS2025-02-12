package by.it.group451002.morozov.lesson08;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак с повторами

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        сколько есть вариантов золотых слитков
                     (каждый можно использовать множество раз).
Следующая строка содержит n целых чисел, задающих веса слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.


Sample Input:
10 3
1 4 8
Sample Output:
10

Sample Input 2:

15 3
2 8 16
Sample Output 2:
14

*/

public class A_Knapsack {

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
        
        // Создаём и инициализируем массив D (массив оптимальных решений для весов w)
        int[] D = new int[w+1];
        for (int i = 0; i <= w; i++)
        	D[i] = 0;
        
        
        // Основной алгоритм (вес равен стоимости)
        for (int wt = 1; wt <= w; wt++) {	// wt = текущий вес
        	for (int i = 0; i < gold.length; i++) { // gold[i] = вес слитка
        		
        		// Елси вес слитка меньше, чем текущий вес
        		if (gold[i] <= wt)
        			
        			// Считаем, что цена равна весу
        			D[wt] = D[wt] >= D[wt-gold[i]]+gold[i] ? D[wt] : D[wt-gold[i]]+gold[i];
    		}
        }

        result = D[w];
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
