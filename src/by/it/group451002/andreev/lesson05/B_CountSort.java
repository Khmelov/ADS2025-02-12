package by.it.group451002.andreev.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс B_CountSort реализует сортировку подсчетом.
 * Алгоритм работает за O(n) и подходит для сортировки небольших диапазонов чисел.
 * Он используется, когда известен максимальный возможный элемент (maxValue).
 */
public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataB.txt"
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();

        // Выполняем сортировку подсчетом и выводим результат
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    /**
     * Метод countSort выполняет сортировку подсчетом.
     * @param stream входной поток данных
     * @return отсортированный массив
     * @throws FileNotFoundException если файл не найден
     */
    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Читаем входные данные
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Устанавливаем максимальное значение элементов (из условия задачи)
        int maxValue = 10;
        int[] count = new int[maxValue + 1]; // Массив для хранения частот

        // Заполняем массив частот
        for (int num : points) {
            count[num]++; // Увеличиваем счетчик для каждого встреченного числа
        }

        // Восстанавливаем отсортированный массив
        int index = 0;
        for (int i = 0; i <= maxValue; i++) {
            while (count[i] > 0) { // Пока количество i > 0, добавляем его в массив
                points[index++] = i;
                count[i]--; // Уменьшаем счетчик
            }
        }

        return points; // Возвращаем отсортированный массив
    }
}
