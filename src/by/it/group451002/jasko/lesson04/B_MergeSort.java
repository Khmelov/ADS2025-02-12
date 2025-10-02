package by.it.group451002.jasko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        // Получаем входной поток данных из файла
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        // Создаем экземпляр класса для сортировки
        B_MergeSort instance = new B_MergeSort();
        // Вызываем метод сортировки и получаем результат
        int[] result = instance.getMergeSort(stream);
        // Выводим отсортированный массив
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) {
        // Инициализация сканера для чтения данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение размера массива
        int n = scanner.nextInt();
        // Создание и заполнение массива
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]); // Отладочный вывод элементов (можно удалить)
        }

        // Вызов рекурсивной функции сортировки слиянием для всего массива
        mergeSort(a, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a; // Возвращаем отсортированный массив
    }

    // Рекурсивная функция сортировки слиянием
    private void mergeSort(int[] array, int left, int right) {
        // Базовый случай рекурсии: если в подмассиве 1 элемент или меньше
        if (left < right) {
            // Находим средний индекс для разделения массива
            int mid = left + (right - left) / 2;

            // Рекурсивно делим левую половину (от left до mid)
            mergeSort(array, left, mid);
            // Рекурсивно делим правую половину (от mid+1 до right)
            mergeSort(array, mid + 1, right);

            // Объединяем две отсортированные половины
            merge(array, left, mid, right);
        }
    }

    // Функция слияния двух отсортированных подмассивов
    private void merge(int[] array, int left, int mid, int right) {
        // Вычисляем размеры временных подмассивов
        int n1 = mid - left + 1;  // Размер левого подмассива
        int n2 = right - mid;     // Размер правого подмассива

        // Создаем временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные во временные массивы
        System.arraycopy(array, left, leftArray, 0, n1);      // Копируем левую часть
        System.arraycopy(array, mid + 1, rightArray, 0, n2);  // Копируем правую часть

        // Инициализация индексов для слияния
        int i = 0;      // Индекс для leftArray
        int j = 0;      // Индекс для rightArray
        int k = left;   // Индекс для основного массива (начинается с left)

        // Слияние временных массивов обратно в основной массив
        while (i < n1 && j < n2) {
            // Выбираем меньший элемент из двух подмассивов
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];  // Берем элемент из левого подмассива
                i++;
            } else {
                array[k] = rightArray[j]; // Берем элемент из правого подмассива
                j++;
            }
            k++; // Переходим к следующей позиции в основном массиве
        }

        // Копируем оставшиеся элементы из leftArray (если они есть)
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Копируем оставшиеся элементы из rightArray (если они есть)
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}