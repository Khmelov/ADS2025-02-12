package by.it.group451003.qtwix.lesson01.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        result = mergeSort(arr, 0, arr.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int merge(int[] arr, int left, int mid, int right){
        int[] arrFirst = Arrays.copyOfRange(arr, left, mid + 1);
        int[] arrSecond = Arrays.copyOfRange(arr, mid + 1, right + 1);

        int i = 0, j = 0;
        int k = left, inversions = 0;

        while (i < arrFirst.length && j < arrSecond.length){
            if (arrFirst[i] <= arrSecond[j]) {
                arr[k] = arrFirst[i];
                i++;
            }else{
                inversions += arrFirst.length - i;
                arr[k] = arrSecond[j];
                j++;
            }
            k++;
        }

        while (i < arrFirst.length){
            arr[k] = arrFirst[i];
            i++;
            k++;
        }

        while (j < arrSecond.length){
            arr[k] = arrSecond[j];
            j++;
            k++;
        }

        return inversions;
    }
    int mergeSort(int[] arr, int left, int right){
        if ( left >= right )
            return 0;

        int mid = left + (right - left) / 2;
        int rightInv = mergeSort(arr, left, mid);
        int leftInv = mergeSort(arr, mid + 1, right);
        int mergeInv = merge(arr, left, mid, right);
        return rightInv + leftInv + mergeInv;
    }

}
