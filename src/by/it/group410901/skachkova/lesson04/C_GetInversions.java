package by.it.group410901.skachkova.lesson04;

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


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        result = mergeSortAndCount(a, 0, a.length - 1);
        return result;
    }
    // Рекурсивная функция для сортировки слиянием и подсчета инверсий
    private int mergeSortAndCount(int[] arr, int left, int right) {
        int count = 0;

        if (left < right) {
            int mid = left + (right - left) / 2;

            // Рекурсивный вызов для левой и правой половин
            count += mergeSortAndCount(arr, left, mid);
            count += mergeSortAndCount(arr, mid + 1, right);

            // Слияние с подсчетом инверсий
            count += mergeAndCount(arr, left, mid, right);
        }

        return count;
    }
    // Функция для слияния двух подмассивов и подсчета инверсий
    private int mergeAndCount(int[] arr, int left, int mid, int right) {
        // Размеры временных подмассивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаем временные массивы
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        // Копируем данные во временные массивы
        for (int i = 0; i < n1; i++) {
            leftArr[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArr[j] = arr[mid + 1 + j];
        }

        int i = 0, j = 0, k = left;
        int inversionCount = 0;

        // Слияние с подсчетом инверсий
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
                // Все оставшиеся элементы в leftArr[i..n1-1] будут больше rightArr[j]
                inversionCount += (n1 - i);
            }
        }
        // Копируем оставшиеся элементы leftArr, если они есть
        while (i < n1) {
            arr[k++] = leftArr[i++];
        }

        // Копируем оставшиеся элементы rightArr, если они есть
        while (j < n2) {
            arr[k++] = rightArr[j++];
        }

        return inversionCount;
    }
}
