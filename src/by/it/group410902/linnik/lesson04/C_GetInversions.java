package by.it.group410902.linnik.lesson04;

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


public class C_GetInversions{

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
        result = MergeSort(a, 0, n-1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    int MergeSort(int[] ar, int start, int end){
        int inver = 0;
        if(start < end) {
            int mid = (start + end)/2;
            inver += MergeSort(ar, start, mid);
            inver += MergeSort(ar, mid +1, end);

            inver+= Merge(ar, start, mid, end);
        }
        return inver;
    }
    int Merge(int[] ar, int start, int mid, int end) {
        int[] arL = new int[mid - start +1];
        int[] arR = new int[end - mid];

        System.arraycopy(ar, start, arL, 0, arL.length);
        System.arraycopy(ar, mid +1, arR, 0, arR.length);

        int i = 0, j = 0, inver = 0;
        for (int k = start; k < end; k++) {
            if (i < arL.length && j < arR.length) {
                if (arL[i] > arR[j]) {
                    inver += arL.length - i;
                    ar[k] = arR[j++];
                } else {
                    ar[k] = arL[i++];
                }
            }
            else {
                if (i <arL.length){
                    ar[k] = arL[i++];
                }
                else if (j < arR.length){
                    ar[k] = arR[j++];
                }
            }
        }
        return inver;
    }
}
