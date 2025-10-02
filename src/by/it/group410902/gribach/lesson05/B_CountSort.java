package by.it.group410902.gribach.lesson05;

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

        // Читаем n целых чисел и сохраняем их в массив points
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        // Создаем массив для подсчета количества вхождений каждого числа
        // Предполагается, что числа находятся в диапазоне от 1 до 10, поэтому размер — 11
        int[] count = new int[11];

        // Подсчитываем количество вхождений каждого числа
        for (int num : points) {
            count[num]++;
        }
        // Формируем отсортированный массив на основе массива подсчета
        int[] result = new int[n];
        int index = 0;
        // Идем по возможным значениям от 1 до 10 (по условию задачи)
        for (int i = 1; i <= 10; i++) {
            // Добавляем число i столько раз, сколько оно встречается
            for (int j = 0; j < count[i]; j++) {
                result[index++] = i;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Возвращаем отсортированный массив
        return result;
    }

}
