package by.it.group451004.kaberdin.lesson04;

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
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        // long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        // long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt(); // Считываем размер массива
        int[] a = new int[n]; // Создаём массив для хранения чисел
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt(); // Заполняем массив числами из входного потока
            System.out.println(a[i]); // Выводим элементы массива (для отладки)
        }
        mergeSort(a, 0, n - 1); // Вызываем сортировку слиянием для всего массива
        return a; // Возвращаем отсортированный массив
    }

    // Рекурсивная функция сортировки слиянием
    void mergeSort(int[] a, int minA, int maxA) {
        // Если в подмассиве 1 или 2 элемента, сортируем их вручную
        if (maxA - minA < 2) {
            if (a[maxA] < a[minA]) { // Если элементы не в порядке, меняем их местами
                int temp = a[minA];
                a[minA] = a[maxA];
                a[maxA] = temp;
            }
        } else {
            // Разделяем массив на две части и рекурсивно сортируем каждую
            int mid = (maxA + minA) / 2; // Находим середину подмассива
            mergeSort(a, minA, mid); // Сортируем левую половину
            mergeSort(a, mid + 1, maxA); // Сортируем правую половину
            mergeSubarrays(a, minA, mid, maxA); // Сливаем две отсортированные половины
        }
    }

    // Функция для слияния двух отсортированных подмассивов
    void mergeSubarrays(int[] source, int minA, int maxA, int maxB) {
        int ai = minA; // Указатель на начало левого подмассива
        int bi = maxA + 1; // Указатель на начало правого подмассива
        int length = maxB - minA + 1; // Длина результирующего подмассива
        int[] buf = new int[length]; // Временный буфер для слияния

        // Сливаем подмассивы, выбирая наименьший элемент из текущих позиций ai и bi
        for (int i = 0; i < length; i++) {
            // Если правый подмассив закончился или текущий элемент левого меньше
            if ((bi > maxB) || (ai <= maxA && source[ai] < source[bi])) {
                buf[i] = source[ai]; // Берём элемент из левого подмассива
                ai++;
            } else {
                buf[i] = source[bi]; // Берём элемент из правого подмассива
                bi++;
            }
        }

        // Копируем отсортированные элементы из буфера обратно в исходный массив
        System.arraycopy(buf, 0, source, minA, length);
    }
}