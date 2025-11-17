package by.it.group451002.shutko.lesson04;

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

//countInversions рекурсивно делит массив на две половины до тех пор, пока каждая часть не будет содержать один элемент.
//После деления, метод mergeAndCount объединяет две половины, подсчитывая инверсии во время слияния.
//Слияние и подсчет инверсий:
//В методе mergeAndCount создается временный массив, который хранит элементы текущего подмассива.
//Сравниваются элементы из левой и правой половин:
//Если элемент из левой половины меньше или равен элементу из правой, он добавляется в основной массив.
//Если элемент из левой половины больше, то все оставшиеся элементы в левой половине образуют инверсии
//с текущим элементом правой половины, и количество инверсий увеличивается на количество оставшихся элементов в левой половине.

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int[] temp = new int[n];
        int result = countInversions(a, temp, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int countInversions(int[] array, int[] temp, int left, int right) {
        int inversions = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            inversions += countInversions(array, temp, left, mid);
            inversions += countInversions(array, temp, mid + 1, right);
            inversions += mergeAndCount(array, temp, left, mid, right);
        }
        return inversions;
    }

    private int mergeAndCount(int[] array, int[] temp, int left, int mid, int right) {
        System.arraycopy(array, left, temp, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;
        int inversions = 0;

        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                array[k++] = temp[i++];
            } else {
                array[k++] = temp[j++];
                inversions += (mid - i + 1);
            }
        }

        while (i <= mid) {
            array[k++] = temp[i++];
        }

        while (j <= right) {
            array[k++] = temp[j++];
        }

        return inversions;
    }
}
