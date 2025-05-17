package by.it.group410902.bobrovskaya.lesson04;

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
        result = countInversions(a);
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int countInversions(int[] array) {
        if (array == null || array.length <= 1) {
            return 0;
        }

        int[] temp = new int[array.length]; // временный массив
        return mergeSortAndCount(array, temp, 0, array.length - 1);
    }
    // рекурсивное разделение массива и подсчет инверсий в процессе сортировки.
    private int mergeSortAndCount(int[] array, int[] temp, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2; // Находим середину массива:

            // Рекурсивно считаем инверсии в левой и правой частях
            count += mergeSortAndCount(array, temp, left, mid);
            count += mergeSortAndCount(array, temp, mid + 1, right);

            // Считаем инверсии при слиянии
            count += mergeAndCount(array, temp, left, mid, right);
        }
        return count;
    }

    private int mergeAndCount(int[] array, int[] temp, int left, int mid, int right) {
        // Копируем элементы во временный массив
        System.arraycopy(array, left, temp, left, right - left + 1);
        int i = left; // указывает на начало левой части массива.
        int j = mid + 1; // указывает на начало правой части массива.
        int k = left; // указывает на текущую позицию в исходном массиве array, куда вставляются отсортированные элементы.
        int count = 0; // переменная, подсчитывающая количество инверсий.

        while (i <= mid && j <= right) { // пока оба подмассива (левая и правая часть) содержат элементы.
            if (temp[i] <= temp[j]) {
                array[k++] = temp[i++];
                // Если левый элемент меньше или равен правому,
                // копируем его обратно в array и двигаем i.
            } else {
                array[k++] = temp[j++];
                // Если правый элемент меньше, значит, он должен стоять перед оставшимися элементами левой части
                count += (mid - i + 1);
                // Увеличиваем count на число элементов, оставшихся в левой части.
            }
        }
        // Копируем оставшиеся элементы левого подмассива
        while (i <= mid) {
            array[k++] = temp[i++];
        }

        return count;
    }
}



