package by.it.group451002.yeshmanski.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.
При сортировке реализуйте метод со сложностью O(n)
Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
*/

public class B_CountSort {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }


    //Создаем вспомогательный массив для подсчета количества вхождений каждого числа,
    //а затем собираем числа в упорядоченном виде.
    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] points = new int[n];
        int min = 11, max = 0;
        int[] numCount = new int[11];

        //Счет количества каждого числа
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
            numCount[points[i]]++;
            min = Math.min(min, points[i]);
            max = Math.max(max, points[i]);
        }

        //Для каждого числа i, встречающегося numCount[i] раз,
        //добавляем i в массив points соответствующее количество раз.

        n = 0;
        for (int i = min; i <= max; i++) {
            for (int j = 0; j < numCount[i]; j++) {
                points[n++] = i;
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}