package by.it.group410902.bobrovskaya.lesson08;

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
        int W =scanner.nextInt(); // вместимость рюкзака
        int n=scanner.nextInt(); // количество типов слитков
        int gold[]=new int[n]; // массив весов слитков
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }
        int[] bag = new int[W + 1]; // массив для определения макс веса

        for (int i = 0; i <= W; i++) { // перебор всех возможных весов рюкзака от 0 до W.
            for (int j = 0; j < n; j++) { // перебор всех типов слитков, доступных для использования.
                if (gold[j] <= i) { // помещается ли слиток в рюкзак с текущей вместимостью i
                    bag[i] = Math.max(bag[i], bag[i - gold[j]] + gold[j]);
                }
            }
        }
        int result = bag[W];
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
