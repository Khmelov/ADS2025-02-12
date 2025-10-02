package by.it.group451003.bernat.lesson05;

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
        // Загружаем входные данные из файла "dataB.txt"
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        // Вызываем метод для сортировки массива
        int[] result = instance.countSort(stream);
        // Выводим отсортированный массив
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        // Создаем объект Scanner для чтения данных из входного потока
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Считываем размер массива (n)
        int n = scanner.nextInt();
        // Создаем массив для хранения чисел
        int[] points = new int[n];

        // Читаем n чисел из входных данных
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Реализуем сортировку подсчетом (Counting Sort)
        // Так как числа не превышают 10, создаем массив для подсчета частоты чисел от 0 до 10
        int[] count = new int[11]; // Размер 11, чтобы учесть числа от 0 до 10
        // Подсчитываем, сколько раз каждое число встречается в массиве
        for (int i = 0; i < n; i++) {
            count[points[i]]++; // Увеличиваем счетчик для соответствующего числа
        }

        // Формируем отсортированный массив
        int index = 0; // Индекс для заполнения массива points
        // Проходим по всем возможным числам от 0 до 10
        for (int i = 0; i <= 10; i++) {
            // Пока есть элементы с текущим значением i, добавляем их в массив
            while (count[i] > 0) {
                points[index++] = i; // Записываем число i в массив
                count[i]--; // Уменьшаем счетчик для числа i
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Возвращаем отсортированный массив
        return points;
    }
}