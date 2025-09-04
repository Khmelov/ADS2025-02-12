package by.it.group410901.shaidarov.lesson04;

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

        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
// Вспомогательный массив для слияния
        int[] temp = new int[n];
        // Запускаем модифицированный merge sort и возвращаем число инверсий
        return mergeSortAndCount(a, temp, 0, n - 1);
    }

    // Рекурсивная функция: сортирует и возвращает число инверсий
    int mergeSortAndCount(int[] a, int[] temp, int left, int right) {
        int invCount = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            // Инверсии в левой и правой частях
            invCount += mergeSortAndCount(a, temp, left, mid);
            invCount += mergeSortAndCount(a, temp, mid + 1, right);
            // Инверсии между частями при слиянии
            invCount += mergeAndCount(a, temp, left, mid, right);
        }
        return invCount;
    }

    // Слияние двух отсортированных частей и подсчёт инверсий
    int mergeAndCount(int[] a, int[] temp, int left, int mid, int right) {
        int i = left;     // указатель на левую часть
        int j = mid + 1;  // указатель на правую часть
        int k = left;     // указатель на temp
        int invCount = 0;

        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
                // Все оставшиеся элементы в левой части больше a[j-1]
                invCount += (mid - i + 1);
            }
        }
        // Копируем оставшиеся элементы
        while (i <= mid) temp[k++] = a[i++];
        while (j <= right) temp[k++] = a[j++];
        // Переносим обратно в исходный массив
        for (i = left; i <= right; i++) a[i] = temp[i];

        return invCount;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    }
}
