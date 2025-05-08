package by.it.group451002.sidarchuk.lesson04;

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
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        // Переменная для хранения количества инверсий
        int[] temp = new int[n]; // Временный массив для слияния
        return mergeSortAndCount(a, temp, 0, n - 1);
    }

    // Метод для сортировки и подсчета инверсий
    private int mergeSortAndCount(int[] array, int[] temp, int left, int right) {
        int mid, invCount = 0;
        if (left < right) {
            mid = left + (right - left) / 2;

            // Подсчет инверсий в левой и правой половинах
            invCount += mergeSortAndCount(array, temp, left, mid);
            invCount += mergeSortAndCount(array, temp, mid + 1, right);

            // Слияние и подсчет инверсий
            invCount += mergeAndCount(array, temp, left, mid, right);
        }
        return invCount;
    }

    // Метод для слияния двух подмассивов и подсчета инверсий
    private int mergeAndCount(int[] array, int[] temp, int left, int mid, int right) {
        int i = left; // Начальный индекс для левого подмассива
        int j = mid + 1; // Начальный индекс для правого подмассива
        int k = left; // Начальный индекс для слияния
        int invCount = 0;

        // Слияние подмассивов
        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                // Все элементы в левом подмассиве после текущего элемента
                // образуют инверсии с элементом array[j]
                temp[k++] = array[j++];
                invCount += (mid - i + 1);
            }
        }

        // Копируем оставшиеся элементы левого подмассива, если есть
        while (i <= mid) {
            temp[k++] = array[i++];
        }

        // Копируем оставшиеся элементы правого подмассива, если есть
        while (j <= right) {
            temp[k++] = array[j++];
        }

        // Копируем отсортированные элементы обратно в оригинальный массив
        for (i = left; i <= right; i++) {
            array[i] = temp[i];
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return invCount;
    }
}
