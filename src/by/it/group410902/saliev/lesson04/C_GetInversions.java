package by.it.group410902.saliev.lesson04;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!

        // реализация слияния с подсчетом инверсий
        class MergeInversion {
            int mergeSort(int[] array, int[] temp, int left, int right) {
                int invCount = 0;
                if (left < right) {
                    int mid = (left + right) / 2;

                    invCount += mergeSort(array, temp, left, mid);
                    invCount += mergeSort(array, temp, mid + 1, right);
                    invCount += merge(array, temp, left, mid, right);
                }
                return invCount;
            }

            int merge(int[] array, int[] temp, int left, int mid, int right) {
                int i = left;
                int j = mid + 1;
                int k = left;
                int invCount = 0;

                while (i <= mid && j <= right) {
                    if (array[i] <= array[j]) {
                        temp[k++] = array[i++];
                    } else {
                        temp[k++] = array[j++];
                        invCount += (mid - i + 1); // все элементы от i до mid больше
                    }
                }

                while (i <= mid) {
                    temp[k++] = array[i++];
                }

                while (j <= right) {
                    temp[k++] = array[j++];
                }

                for (int t = left; t <= right; t++) {
                    array[t] = temp[t];
                }

                return invCount;
            }
        }

        MergeInversion mi = new MergeInversion();
        int[] temp = new int[n];
        result = mi.mergeSort(a, temp, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
