package by.it.group451001.kolosun.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

    int per;

    int[] Merge(int[] arr1, int[] arr2){
        int f1 = 0;
        int f2 = 0;
        int r = 0;
        int[] res = new int[arr1.length + arr2.length];
        while (f1 < arr1.length && f2 < arr2.length){
            if(arr1[f1] <= arr2[f2]) {
                res[r] = arr1[f1];
                r++;
                f1++;
            }
            else{
                res[r] = arr2[f2];
                per += arr1.length - f1;
                r++;
                f2++;
            }
        }

        while (f1 < arr1.length){
            res[r] = arr1[f1];
            r++;
            f1++;
        }

        while (f2 < arr2.length){
            res[r] = arr2[f2];
            r++;
            f2++;
        }
        return  res;
    }

    int[] MergeSort(int[] array){
        if(array.length == 1){
            return array;
        }else{
            int[] l = MergeSort( Arrays.copyOfRange(array, 0, array.length/ 2));
            int[] r = MergeSort(Arrays.copyOfRange(array, array.length / 2, array.length));
            return Merge(l,r);
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
        per = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        MergeSort(a);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return per;
    }
}
