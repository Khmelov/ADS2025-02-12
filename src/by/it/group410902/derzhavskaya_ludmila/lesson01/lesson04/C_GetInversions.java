package by.it.group410902.derzhavskaya_ludmila.lesson01.lesson04;

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
        result = countInversions(a, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    // Метод для рекурсивного подсчета инверсий с использованием сортировки слиянием
    private int countInversions(int[] arr, int start, int end)
    {
        int inversions = 0;
        if (start < end)
        {
            int mid = (start + end) / 2;
            // Рекурсивно считаем инверсии в левой и правой половинах
            inversions += countInversions(arr, start, mid);
            inversions += countInversions(arr, mid + 1, end);
            // Считаем инверсии при слиянии двух отсортированных половин
            inversions += mergeAndCount(arr, start, mid, end);
        }
        return inversions;
    }

    // Метод для слияния двух отсортированных подмассивов и подсчета инверсий
    private int mergeAndCount(int[] arr, int start, int mid, int end) {
        // Создаем временные подмассивы для левой и правой частей
        int[] leftArr = new int[mid - start + 1];
        int[] rightArr = new int[end - mid];

        // Копируем данные во временные массивы
        System.arraycopy(arr, start, leftArr, 0, leftArr.length);
        System.arraycopy(arr, mid + 1, rightArr, 0, rightArr.length);

        int i = 0, j = 0, k = start;
        int inversions = 0;

        // Слияние с подсчетом инверсий
        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
                // Все оставшиеся элементы в leftArr[i..mid] образуют инверсии с rightArr[j]
                inversions += (mid - start + 1) - i;
            }
        }

        // Дописываем оставшиеся элементы из leftArr (если есть)
        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
        }

        // Дописываем оставшиеся элементы из rightArr (если есть)
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
        }

        return inversions;
    }

}
