package lesson04;

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
        result= mergeSort(a);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    public static int mergeSort(int[] array) {
        int result;
        if (array.length < 2) {
            return 0;
        }
        int[] helper = new int[array.length];
        result = mergeSort(array, 0, array.length - 1, helper);
        return result;
    }

    private static int mergeSort(int[] array, int left, int right, int[] helper) {
        int result = 0;
        if (left < right) {
            int mid = (left + right) / 2;
            result += mergeSort(array, left, mid, helper);
            result += mergeSort(array, mid + 1, right, helper);
            result += merge(array, left, mid, right, helper);
        }
        return result;
    }

    private static int merge(int[] array, int left, int mid, int right, int[] helper) {
        // Копируем обе части во вспомогательный массив
        for (int i = left; i <= right; i++) {
            helper[i] = array[i];
        }
        int result = 0;
        int i = left;
        int j = mid + 1;
        int k = left;

        // Сливаем части обратно в исходный массив
        while (i <= mid && j <= right) {
            if (helper[i] <= helper[j]) {
                array[k] = helper[i];
                i++;
            } else {
                array[k] = helper[j];
                j++;
                result += mid - i + 1;
            }
            k++;
        }

        // Копируем остатки левой части
        while (i <= mid) {
            array[k] = helper[i];
            k++;
            i++;

        }

        // Копируем остатки правой части (если есть)
        while (j <= right) {
            array[k] = helper[j];
            k++;
            j++;
        }
        return result;
    }

}
