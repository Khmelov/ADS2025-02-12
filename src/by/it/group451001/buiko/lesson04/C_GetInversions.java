package by.it.group451001.buiko.lesson04;

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
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        // Подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        // Размер массива
        int n = scanner.nextInt();
        // Сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Переменная для хранения количества инверсий
        int[] temp = new int[n]; // Временный массив для сортировки
        int result = mergeSortAndCount(a, temp, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Метод сортировки слиянием с подсчетом инверсий
    private int mergeSortAndCount(int[] array, int[] temp, int left, int right) {
        int mid, invCount = 0;
        if (left < right) {
            mid = (left + right) / 2;

            // Считаем инверсии в левой и правой половинах
            invCount += mergeSortAndCount(array, temp, left, mid);
            invCount += mergeSortAndCount(array, temp, mid + 1, right);

            // Считаем инверсии при слиянии
            invCount += mergeAndCount(array, temp, left, mid, right);
        }
        return invCount;
    }

    // Метод для слияния двух подмассивов и подсчета инверсий
    private int mergeAndCount(int[] array, int[] temp, int left, int mid, int right) {
        int i = left; // Начало левой половины
        int j = mid + 1; // Начало правой половины
        int k = left; // Индекс для временного массива
        int invCount = 0;

        // Слияние двух подмассивов
        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                // Все оставшиеся элементы в левой половине больше array[j]
                temp[k++] = array[j++];
                invCount += (mid - i + 1); // Увеличиваем счетчик инверсий
            }
        }

        // Копируем оставшиеся элементы левой половины
        while (i <= mid) {
            temp[k++] = array[i++];
        }

        // Копируем оставшиеся элементы правой половины
        while (j <= right) {
            temp[k++] = array[j++];
        }

        // Копируем отсортированные элементы обратно в оригинальный массив
                for (int index = left; index <= right; index++) {
                    array[index] = temp[index];
                }

                return invCount; // Возвращаем количество инверсий
            }
        }
