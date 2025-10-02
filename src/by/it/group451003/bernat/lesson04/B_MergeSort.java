package by.it.group451003.bernat.lesson04;

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
        // Загружаем входные данные из файла "dataB.txt"
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        // Вызываем метод для сортировки массива
        int[] result = instance.getMergeSort(stream);
        // Выводим отсортированный массив
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        // Создаем объект Scanner для чтения данных из входного потока
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Считываем размер массива (n)
        int n = scanner.nextInt();
        // Создаем массив a размером n для хранения чисел
        int[] a = new int[n];
        // Заполняем массив a числами из входных данных
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем сортировку слиянием для всего массива
        mergeSort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Возвращаем отсортированный массив
        return a;
    }

    // Рекурсивная функция сортировки слиянием
    private void mergeSort(int[] array, int left, int right) {
        // Если левая граница меньше правой, продолжаем разделение
        if (left < right) {
            // Находим середину массива, чтобы разделить его на две части
            int mid = left + (right - left) / 2;
            // Рекурсивно сортируем левую половину
            mergeSort(array, left, mid);
            // Рекурсивно сортируем правую половину
            mergeSort(array, mid + 1, right);
            // Объединяем две отсортированные половины
            merge(array, left, mid, right);
        }
    }

    // Функция слияния двух отсортированных подмассивов
    private void merge(int[] array, int left, int mid, int right) {
        // Вычисляем размеры левого и правого подмассивов
        int n1 = mid - left + 1; // Размер левого подмассива
        int n2 = right - mid; // Размер правого подмассива

        // Создаем временные массивы для хранения левого и правого подмассивов
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные из основного массива во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        // Инициализируем индексы для слияния
        int i = 0; // Индекс для левого подмассива
        int j = 0; // Индекс для правого подмассива
        int k = left; // Индекс для основного массива

        // Сравниваем элементы левого и правого подмассивов и записываем меньший в основной массив
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i]; // Добавляем элемент из левого подмассива
                i++;
            } else {
                array[k] = rightArray[j]; // Добавляем элемент из правого подмассива
                j++;
            }
            k++;
        }

        // Если остались элементы в левом подмассиве, добавляем их
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Если остались элементы в правом подмассиве, добавляем их
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}