package by.it.group451002.jasko.lesson05;

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
        // Получаем входной поток данных из файла
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        // Создаем экземпляр класса для сортировки
        B_CountSort instance = new B_CountSort();
        // Вызываем метод сортировки и получаем результат
        int[] result = instance.countSort(stream);
        // Выводим отсортированный массив
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    // Метод сортировки подсчетом (Counting Sort)
    int[] countSort(InputStream stream) {
        // Инициализация сканера для чтения данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение размера массива
        int n = scanner.nextInt();
        // Создание массива для хранения чисел
        int[] points = new int[n];

        // Чтение чисел из входного потока
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Реализация сортировки подсчетом

        // Максимальное значение в массиве (по условию задачи не превышает 10)
        final int MAX_VALUE = 10;

        // Создаем массив счетчиков размером MAX_VALUE + 1
        // (+1 потому что числа могут быть от 1 до 10 включительно)
        int[] count = new int[MAX_VALUE + 1];

        // Этап 1: Подсчет частоты встречаемости каждого числа
        // Проходим по всем элементам исходного массива
        for (int i = 0; i < n; i++) {
            // Увеличиваем счетчик для текущего числа
            count[points[i]]++;
        }

        // Этап 2: Восстановление отсортированного массива
        int index = 0; // Индекс для заполнения исходного массива

        // Проходим по всем возможным значениям чисел (от 1 до 10)
        for (int value = 1; value <= MAX_VALUE; value++) {
            // Добавляем число value столько раз, сколько оно встречалось
            while (count[value] > 0) {
                points[index++] = value; // Записываем число в массив
                count[value]--; // Уменьшаем счетчик
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return points; // Возвращаем отсортированный массив
    }
}