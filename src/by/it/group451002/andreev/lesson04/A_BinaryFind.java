package by.it.group451002.andreev.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс A_BinaryFind реализует поиск элементов в отсортированном массиве
 * с использованием бинарного поиска.
 */
public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataA.txt"
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        A_BinaryFind instance = new A_BinaryFind();

        // Выполняем поиск индексов элементов и выводим их
        int[] result = instance.findIndex(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    /**
     * Метод findIndex выполняет бинарный поиск для нескольких значений.
     * @param stream входной поток данных
     * @return массив найденных индексов
     * @throws FileNotFoundException если файл не найден
     */
    int[] findIndex(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем отсортированный массив
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Читаем количество искомых значений
        int k = scanner.nextInt();
        int[] result = new int[k];

        // Поиск каждого значения с помощью бинарного поиска
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            result[i] = binarySearch(a, value) + 1; // +1 переводит в 1-индексацию
        }

        return result;
    }

    /**
     * Метод binarySearch реализует классический бинарный поиск.
     * @param a отсортированный массив
     * @param value искомое значение
     * @return индекс элемента или -1, если не найден (с учетом +1 в findIndex)
     */
    private int binarySearch(int[] a, int value) {
        int left = 0; // Начало диапазона
        int right = a.length - 1; // Конец диапазона

        while (left <= right) {
            int mid = left + (right - left) / 2; // Находим средний элемент

            if (a[mid] == value) {
                return mid; // Если найден, возвращаем индекс
            } else if (a[mid] < value) {
                left = mid + 1; // Искомое значение больше, двигаем границу вправо
            } else {
                right = mid - 1; // Искомое значение меньше, двигаем границу влево
            }
        }

        return -2; // -2 превращается в -1 после прибавления +1 в findIndex, обозначая "не найдено"
    }
}
