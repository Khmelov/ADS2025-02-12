package by.it.group451002.vishnevskiy.lesson04;

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
            a[i] = scanner.nextInt(); // читаем элементы массива
        }
        int[] temp = new int[n]; // вспомогательный массив для слияния
        int result = mergeSortAndCount(a, temp, 0, n - 1); // запускаем сортировку и подсчёт инверсий

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Метод выполняет сортировку слиянием и одновременно считает инверсии
    int mergeSortAndCount(int[] a, int[] temp, int left, int right) {
        int mid, invCount = 0;
        if (right > left) {
            mid = (left + right) / 2; // делим массив пополам
            invCount += mergeSortAndCount(a, temp, left, mid); // рекурсивно сортируем левую часть
            invCount += mergeSortAndCount(a, temp, mid + 1, right); // рекурсивно сортируем правую часть
            invCount += mergeAndCount(a, temp, left, mid + 1, right); // сливаем и считаем инверсии
        }
        return invCount; // возвращаем общее количество инверсий
    }

    // Метод слияния двух отсортированных подмассивов и подсчета инверсий
    int mergeAndCount(int[] a, int[] temp, int left, int mid, int right) {
        int i = left;     // индекс в левом подмассиве
        int j = mid;      // индекс в правом подмассиве
        int k = left;     // индекс для результирующего массива
        int invCount = 0; // счетчик инверсий

        while ((i <= mid - 1) && (j <= right)) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++]; // если порядок правильный — просто копируем
            } else {
                temp[k++] = a[j++]; // если порядок нарушен — инверсия
                invCount += (mid - i); // количество инверсий равно числу оставшихся элементов в левом подмассиве
            }
        }

        while (i <= mid - 1) // копируем оставшиеся элементы из левого подмассива
            temp[k++] = a[i++];

        while (j <= right) // копируем оставшиеся элементы из правого подмассива
            temp[k++] = a[j++];

        for (i = left; i <= right; i++) // копируем обратно отсортированный участок в оригинальный массив
            a[i] = temp[i];

        return invCount; // возвращаем число инверсий, найденных при этом слиянии
    }
}
