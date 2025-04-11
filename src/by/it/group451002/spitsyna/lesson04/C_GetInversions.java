package by.it.group451002.spitsyna.lesson04;

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
        result = mergeSort(a, 0, n-1, result);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int mergeSort(int[] a, int l, int r, int result) {
      if (l<r) {
          int m = (l + r )/2;

          mergeSort(a,l,m, result);
          mergeSort(a,m+1, r, result);

          result = merge(a, l, m,r, result);
      }
      return result;
    }

    private int merge(int[] a, int l,int m, int r, int result){
        int n1 = m-l+1;
        int n2 = r-m;

        int[] aLeft = new int [n1];
        int[] aRight = new int[n2];

        //заполнение левого подмассива
        for (int i = 0; i < n1; i++){
            aLeft[i] = a[l+i];
        }

        //заполнение правого подмассива
        for (int i = 0; i < n2; i++){
            aRight[i] = a[m+1+i];
        }

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2){
           if (aLeft[i]<= aRight[j]){
               a[k] = aLeft[i];
               i++;
           }
           else {
               a[k] = aRight[j];
               j++;
               result += n1 - i;
           }
           k++;
        }

        while (i < n1){
            a[k] = aLeft[i];
            i++;
            k++;
        }

        while (j < n2) {
            a[k] = aRight[j];
            j++;
            k++;
        }
        return result;
    }
}
