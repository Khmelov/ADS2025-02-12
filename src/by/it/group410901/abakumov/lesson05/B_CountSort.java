package by.it.group410901.abakumov.lesson05;

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

    int[] countSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        //читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением сортировки подсчетом
        int max = 10; // as per the problem statement, numbers do not exceed 10
        int[] count = new int[max + 1]; // indices 0..10

        // Step 1: Count the occurrences of each number
        for (int num : points) {
            count[num]++;
        }

        // Step 2: Calculate the starting positions (optional, can directly overwrite)
        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
        }

        // Step 3: Place the numbers in the output array in sorted order
        int[] output = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int num = points[i];
            output[count[num] - 1] = num;
            count[num]--;
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return output;
    }

}
