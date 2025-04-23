package by.it.group410902.podryabinkin.lesson04;

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


        result = MySort(a).inv;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public class MyData {
        public int inv;
        public int[] ar;

        MyData(int[] ar, int inv) {
            this.ar = ar;
            this.inv = inv;
        }
    }

    MyData MySort(int[] inp) {
        if (inp.length <= 1) return new MyData(inp, 0);

        int mid = inp.length / 2;

        int[] arr1 = new int[mid];
        int[] arr2 = new int[inp.length - mid];

        for(int i = 0; i < inp.length / 2; i++){
            arr1[i] = inp[i];
        }
        for(int i = inp.length / 2, j = 0; i < inp.length; i++, j++){
            arr2[j] = inp[i];
        }

        MyData d1 = MySort(arr1);
        MyData d2 = MySort(arr2);

        int[] merged = new int[inp.length];
        int i = 0, j = 0, k = 0;
        int invCount = d1.inv + d2.inv;

        while (i < d1.ar.length && j < d2.ar.length) {
            if (d1.ar[i] <= d2.ar[j]) {
                merged[k++] = d1.ar[i++];
            } else {
                merged[k++] = d2.ar[j++];
                invCount += (d1.ar.length - i); // все оставшиеся в arr1 больше текущего из arr2
            }
        }
        while (i < d1.ar.length) {
            merged[k++] = d1.ar[i++];
        }
        while (j < d2.ar.length) {
            merged[k++] = d2.ar[j++];
        }

        return new MyData(merged, invCount);
    }
}
