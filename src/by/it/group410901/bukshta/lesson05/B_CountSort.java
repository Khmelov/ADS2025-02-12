package by.it.group410901.bukshta.lesson05;

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
        if (stream == null) {
            stream = System.in; // Fallback to standard input
        }
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        // Initialize scanner
        Scanner scanner = new Scanner(stream);
//Считываем количество чисел n и сами числа в массив.

        // Read size of array
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Read numbers into array
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
//Создаём массив count размером 11 для подсчёта частоты чисел от 1 до 10.
        // Counting Sort
        // Since numbers are 1 to 10, use a count array of size 11 (index 0 unused)
        int[] count = new int[11];
//Проходим по входному массиву, увеличивая count[i] для каждого числа i.
//Проходим по count от 1 до 10, записывая число i в результат столько раз, сколько указано в count[i].

        // Step 1: Count occurrences of each number
        for (int i = 0; i < n; i++) {
            count[points[i]]++;
        }

//Возвращаем отсортированный массив.
        // Step 2: Reconstruct sorted array
        int index = 0;
        for (int num = 1; num <= 10; num++) {
            while (count[num] > 0) {
                points[index++] = num;
                count[num]--;
            }
        }

        scanner.close();
        return points;
    }
}