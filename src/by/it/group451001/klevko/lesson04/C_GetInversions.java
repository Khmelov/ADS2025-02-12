package by.it.group451001.klevko.lesson04;

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
        /*for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (a[i] > a[j]) {
                    ++result;
                }
            }
        }*/
        int size = 1;
        int start1, start2, end1, end2, leftElem, i;
        //строго до end и начиная включая с start

        while (size < n) {
            //
            start1 = 0;
            end2 = 0;
            int[] a2 = new int[n];
            while (start1 < n) {
                start1 = end2;
                end1 = Math.min(start1 + size, n);
                start2 = end1;
                end2 = Math.min(start2 + size, n);

                //sort 1 part
                leftElem = 0;   i = 0;
                while ((start1 < end1) && (start2 < end2)) {
                    if (a[start1] > a[start2]) {
                        a2[i] = a[start2];
                        ++start2;
                        result+= leftElem;
                    } else {
                        a2[i] = a[start1];
                        ++start1;
                        ++leftElem;
                    }
                    ++i;
                }
                if (start1 == end1) {
                    System.arraycopy(a, start2, a2, i, end2-start2);
                } else {
                    System.arraycopy(a, start1, a2, i, end1-start1);
                }
            }
            a = a2;
            size*=2;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
