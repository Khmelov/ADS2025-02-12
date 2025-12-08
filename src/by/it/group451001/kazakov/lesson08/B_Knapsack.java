package by.it.group451001.kazakov.lesson08;

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

        // результат (максимальный вес золота)
        int result = 0;

        // создаём и инициализируем таблицу динамики D[вместимость][кол-во предметов]
        // D[wt][i] — максимальный вес золота при вместимости wt и использовании первых i предметов
        int[][] D = new int[w + 1][n + 1];

        // инициализация таблицы (все значения по умолчанию равны 0)
        for (int i = 0; i <= w; i++) {
            for (int j = 0; j <= n; j++) {
                D[i][j] = 0;
            }
        }

        // основной цикл динамического программирования
        // перебираем предметы (от 1 до n)
        for (int i = 1; i <= n; i++) {
            // перебираем возможные вместимости (от 1 до W)
            for (int wt = 1; wt <= w; wt++) {
                // по умолчанию — не берем i-й предмет
                D[wt][i] = D[wt][i - 1];

                // если вес i-го предмета помещается в рюкзак вместимости wt
                if (gold[i - 1] <= wt) {
                    // проверяем, выгоднее ли взять его
                    if (D[wt - gold[i - 1]][i - 1] + gold[i - 1] > D[wt][i]) {
                        // если да — обновляем значение
                        D[wt][i] = D[wt - gold[i - 1]][i - 1] + gold[i - 1];
                    }
                }
            }
        }

        // ответ находится в ячейке для полной вместимости W и всех n предметов
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