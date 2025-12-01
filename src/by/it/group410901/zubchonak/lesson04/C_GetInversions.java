package by.it.group410901.zubchonak.lesson04;

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

    public static void main(String[] args) {
        try {
            InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
            if (stream == null) {
                System.err.println("Файл dataC.txt не найден в classpath!");
                return; // Или выбросить исключение, если это критично
            }
            C_GetInversions instance = new C_GetInversions();
            int result = instance.calc(stream);
            System.out.print(result);
        } catch (FileNotFoundException e) {
            System.err.println("Файл dataC.txt не найден: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
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

        int[] temp = new int[n];
        int inversions = mergeSortAndCount(a, temp, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return inversions;
    }


    private int mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        int inversions = 0;
        if (left < right) {
            int mid = (left + right) / 2;

            inversions += mergeSortAndCount(arr, temp, left, mid);
            inversions += mergeSortAndCount(arr, temp, mid + 1, right);
            inversions += mergeAndCount(arr, temp, left, mid, right);
        }
        return inversions;
    }

    private int mergeAndCount(int[] arr, int[] temp, int left, int mid, int right) {
        int inversions = 0;

        int i = left;
        int j = mid + 1;
        int k = left;
        //Сравнивает элементы из двух половин и вставляет их в временный массив temp.
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
                inversions += (mid - i + 1); // Ключевой момент подсчета инверсий
            }
        }
       // Когда элемент из правой половины меньше, это означает,
        // что все оставшиеся элементы в левой половине формируют инверсии с этим элементом.
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        while (j <= right) {
            temp[k++] = arr[j++];
        }
       // В конце временный массив копируется обратно в оригинальный массив.

        System.arraycopy(temp, left, arr, left, right - left + 1);

        return inversions;
    }

}
