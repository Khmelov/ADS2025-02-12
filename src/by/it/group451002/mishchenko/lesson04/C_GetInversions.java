package by.it.group451002.mishchenko.lesson04;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;

        // Локальный класс для подсчёта инверсий с помощью сортировки слиянием
        class InversionCounter {
            int mergeSortAndCount(int[] arr, int left, int right) {
                if (left >= right) {
                    return 0;
                }
                int mid = left + (right - left) / 2;
                int invCount = mergeSortAndCount(arr, left, mid);
                invCount += mergeSortAndCount(arr, mid + 1, right);
                invCount += mergeAndCount(arr, left, mid, right);
                return invCount;
            }

            int mergeAndCount(int[] arr, int left, int mid, int right) {
                int[] temp = new int[right - left + 1];
                int i = left, j = mid + 1, k = 0;
                int count = 0;
                while (i <= mid && j <= right) {
                    if (arr[i] <= arr[j]) {
                        temp[k++] = arr[i++];
                    } else {
                        // Если arr[i] > arr[j], добавляем все оставшиеся элементы из левой части
                        count += (mid - i + 1);
                        temp[k++] = arr[j++];
                    }
                }
                while (i <= mid) {
                    temp[k++] = arr[i++];
                }
                while (j <= right) {
                    temp[k++] = arr[j++];
                }
                // Копируем отсортированный участок обратно в исходный массив
                System.arraycopy(temp, 0, arr, left, temp.length);
                return count;
            }
        }

        InversionCounter counter = new InversionCounter();
        result = counter.mergeSortAndCount(a, 0, n - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}
