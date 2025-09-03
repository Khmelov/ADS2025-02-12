package by.it.group410901.zaverach.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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

        result = countInversions(a, 0, n - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int countInversions(int[] a, int l, int r) {
        // список List будет отсортирован
        if (r > l + 1) {
            int s = (r - l) / 2 + l;
            int leftInv = countInversions(a, l, s);
            int rightInv = countInversions(a, s + 1, r);
            int splitInv = merge(l, r, a, s);
            return leftInv + rightInv + splitInv;
        } else return 0;
    }

    int merge(int l, int r, int [] a,int s){
        ArrayList<Integer> b=new ArrayList<>();
        int i = l;
        int j = s+1;
        int inversions=0;

        while (i <= s && j <= r) {
            if (a[i] < a[j]) {
                b.add(a[i]);
                i += 1;
            } else {
                b.add(a[j]);
                j += 1;
                inversions=inversions+(i-l);
            }
        }

        while (i <= s) {
            b.add(a[i]);
            i += 1;
        }


        while (j <= r) {
            b.add(a[j]);
            j += 1;
        }

        for (int k = l; k <= r; k++) {
            a[k] = b.getFirst();
            b.removeFirst();
        }
    return inversions;
    }

}
