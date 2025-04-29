package by.it.group410901.tomashevich.lesson04;

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

        result = Sort(a, 0, n - 1, result);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    int Sort(int[] a, int low, int high, int inversions) {
        int n = high - low + 1;
        if (n <= 1) {
            return 0;
        }
        int mid = (low + high)/ 2;
        Sort(a, low, mid, inversions);
        Sort(a, mid + 1, high, inversions);
        int[] temp = new int[n];
        int l1 = low, l2 = mid + 1;
        for(int i = 0; i < n; i++) {
            if(l1 > mid) {
                temp[i] = a[l2];
                l2++;
            }
            else if(l2 > high) {
                temp[i] = a[l1];
                l1++;
            }
            else {
                if(a[l1] <= a[l2]) {
                    temp[i] = a[l1];
                    l1++;
                }
                else {
                    //System.out.println(high);
                    //System.out.println(l2);
                    inversions += high - l2 + 1;
                    temp[i] = a[l2];
                    l2++;
                }
            }
        }
        for(int i = 0; i < n; i++) {
            a[low + i] = temp[i];
        }
        return inversions;
    }
}
