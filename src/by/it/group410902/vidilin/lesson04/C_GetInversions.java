package by.it.group410902.vidilin.lesson04;

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
        int[] aux = new int[n]; // Вспомогательный массив для сортировки слиянием
        result = mergeSortAndCount(a, aux, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private int mergeSortAndCount(int[] arr, int[] aux, int left, int right) {
        int inversions = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;

            inversions += mergeSortAndCount(arr, aux, left, mid);
            inversions += mergeSortAndCount(arr, aux, mid + 1, right);

            inversions += mergeAndCount(arr, aux, left, mid, right);
        }
        return inversions;
    }
    private int mergeAndCount(int[] arr, int[] aux, int left, int mid, int right) {
        int inversions = 0;

        // Копируем элементы во вспомогательный массив
        for (int i = left; i <= right; i++) {
            aux[i] = arr[i];
        }

        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (aux[i] <= aux[j]) {
                arr[k] = aux[i];
                i++;
            } else {
                arr[k] = aux[j];
                j++;
                inversions += (mid - i + 1);  // Важно: считаем инверсии здесь!
            }
            k++;
        }


        while (i <= mid) {
            arr[k] = aux[i];
            i++;
            k++;
        }

        while (j <= right) {
            arr[k] = aux[j];
            j++;
            k++;
        }

        return inversions;
    }
}
