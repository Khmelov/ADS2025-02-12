package by.it.group451002.spitsyna.lesson05;

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

        int min, max = min= points[0];
        //Находим максимальное и минимальное значение элементов в массиве
        for (int i = 0; i <= n-1; i++){
            if (points[i] > max){
                max = points[i];
            }
            if (points[i] < min){
                min = points[i];
            }
        }

        //Создаем массив для хранения, как часто встречаются элементы исходного массива
        int[] count = new int[max-min+1];

        //Подсчитываем частоту встречания элементов
        for (int i = 0; i <= n-1; i++){
            count[points[i]-min]++;
        }

        //переменная для прохода по массиву points
        int arrind = 0;
        //Ставим элементы на свои места в зависимости от частоты их встречания
        for (int i = 0; i <= count.length-1; i++) {
            for (int j = 0; j < count[i]; j++){
                points[arrind] = i + min;
                arrind++;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
