package by.it.group451003.bernat.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2
*/
public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataC.txt"
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        // Вызываем метод для подсчета инверсий
        int result = instance.calc(stream);
        // Выводим количество инверсий
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        // Создаем объект Scanner для чтения данных из входного потока
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        // Считываем размер массива (n)
        int n = scanner.nextInt();
        // Создаем массив a размером n для хранения чисел
        int[] a = new int[n];
        // Заполняем массив a числами из входных данных
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        // Создаем массив для хранения количества инверсий (используем массив для передачи по ссылке)
        long[] inversions = {0};
        // Вызываем сортировку слиянием с подсчетом инверсий
        mergeSort(a, 0, n - 1, inversions);
        // Возвращаем количество инверсий как целое число
        return (int) inversions[0];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    // Рекурсивная функция сортировки слиянием с подсчетом инверсий
    private void mergeSort(int[] array, int left, int right, long[] inversions) {
        // Если левая граница меньше правой, продолжаем разделение
        if (left < right) {
            // Находим середину массива
            int mid = left + (right - left) / 2;
            // Рекурсивно сортируем левую половину
            mergeSort(array, left, mid, inversions);
            // Рекурсивно сортируем правую половину
            mergeSort(array, mid + 1, right, inversions);
            // Объединяем две половины с подсчетом инверсий
            merge(array, left, mid, right, inversions);
        }
    }

    // Функция слияния двух отсортированных подмассивов с подсчетом инверсий
    private void merge(int[] array, int left, int mid, int right, long[] inversions) {
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

        // Сравниваем элементы левого и правого подмассивов
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                // Если элемент левого подмассива меньше или равен, добавляем его
                array[k] = leftArray[i];
                i++;
            } else {
                // Если элемент правого подмассива меньше, добавляем его
                array[k] = rightArray[j];
                // Все оставшиеся элементы левого подмассива (от i до n1-1) больше rightArray[j],
                // поэтому добавляем количество инверсий
                inversions[0] += n1 - i;
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