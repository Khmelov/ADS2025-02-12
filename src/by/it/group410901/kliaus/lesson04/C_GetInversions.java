package by.it.group410901.kliaus.lesson04;

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

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
*/

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // тут ваше решение (сортировка с подсчетом инверсий)
        return countInversions(a);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    // Метод, возвращающий количество инверсий
    int countInversions(int[] array) {
        int[] temp = new int[array.length];
        return mergeSortAndCount(array, temp, 0, array.length - 1);
    }

    // Рекурсивная сортировка слиянием с подсчетом инверсий
    int mergeSortAndCount(int[] array, int[] temp, int left, int right) {
        int mid, invCount = 0;
        if (right > left) {
            mid = (right + left) / 2;

            invCount += mergeSortAndCount(array, temp, left, mid);
            invCount += mergeSortAndCount(array, temp, mid + 1, right);
            invCount += merge(array, temp, left, mid + 1, right);
        }
        return invCount;
    }

    // Слияние массивов с подсчетом инверсий
    int merge(int[] array, int[] temp, int left, int mid, int right) {
        int i = left;      // индекс в левой части
        int j = mid;       // индекс в правой части
        int k = left;      // индекс во временном массиве
        int invCount = 0;

        while (i <= mid - 1 && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
                invCount += (mid - i); // элементы слева > array[j]
            }
        }

        while (i <= mid - 1) {
            temp[k++] = array[i++];
        }

        while (j <= right) {
            temp[k++] = array[j++];
        }

        // Копирование обратно в оригинальный массив
        for (i = left; i <= right; i++) {
            array[i] = temp[i];
        }

        return invCount;
    }
}
