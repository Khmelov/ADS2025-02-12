package by.it.group451002.koltsov.lesson04;

import javax.print.attribute.IntegerSyntax;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        System.out.println(result);
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!


        int[] tempArray = new int[n];

        int num = 1;
        int i = 0, j = 0;
        int x = 0, y = 0;
        while (num < n)
        {
            for (int index = 0; index < n; index += num * 2) {
                for (int k = 0; k < num * 2 && k < n; k++) {
                    if (i < num && index + i < n || j < num && index + num + j < n) {
                        if (i < num)
                            x = a[index + i];
                        else
                            x = Integer.MAX_VALUE;

                        if (j < num && index + num + j < n)
                            y = a[index + num + j];
                        else
                            y = Integer.MAX_VALUE;

                        if (x <= y) {
                            tempArray[index + k] = x;
                            i++;
                        } else {
                            tempArray[index + k] = y;
                            result += num - i;  // если ставим на место элемент из правого подмассива,
                            j++;                // добавляем к result текущее кол-во элементов из левого
                        }
                    }
                }
                i = 0;
                j = 0;
            }
            System.arraycopy(tempArray, 0, a, 0, n);
            num *= 2;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        System.out.println(result);
        return result;
    }
}
