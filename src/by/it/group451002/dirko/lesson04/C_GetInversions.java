package by.it.group451002.dirko.lesson04;

import javax.sound.midi.SysexMessage;
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

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!

        // Читаем размер массива из файла
        int n = scanner.nextInt();

        // Если длина массива нулевая, то возвращаем нулевой результат
        if (n == 0) { return 0; }

        // Читаем массив из файла
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Определяем количество инверсий
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!

        // Вызываем функцию для подсчета инверсий
        result = mergeSort(a, 0, n-1).total_invers;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Функция для сортировки слиянием
    Pair mergeSort(int[] a, int leftBorder, int rightBorder) {
        // Если длина массива = 1, то возвращаем массив
        if (rightBorder == leftBorder) {
            return new Pair(Arrays.copyOfRange(a, leftBorder, rightBorder + 1),0);
        }
        // Иначе делим массив на 2 части и снова вызываем эту функцию
        else {
            // Находим центральный элемент (чтобы поделить массив пополам)
            int midInd = (rightBorder + leftBorder) / 2;

            // Делим массив на 2 части и вызываем функцию сортировки для каждой половины
            Pair firstPair = mergeSort(a, leftBorder, midInd);
            Pair secondPair = mergeSort(a, midInd + 1, rightBorder);

            // Общее количество инверсий (с первой и второй половин массива)
            int total_invers = firstPair.total_invers + secondPair.total_invers;

            // Создаем массив для сложения двух отсортированных массивов (половин)
            int[] b = new int[rightBorder - leftBorder + 1];

            // Индексы в отсортированных массивах (левой и правой половине)
            int l_ind = 0, r_ind = 0;

            // Записываем в массив b по 1 наименьшему элементу из 2 массивов (сортируем)
            for (int i = 0; i < b.length; i++) {
                // Если в первом массиве закончились элементы, то записываем оставшиеся элементы со второго массива
                if (l_ind == firstPair.a.length) {
                    System.arraycopy(secondPair.a, r_ind, b, i, secondPair.a.length-r_ind);
                    break;
                }
                // Если во втором массиве закончились элементы, то записываем оставшиеся элементы с первого массива
                else if (r_ind == secondPair.a.length) {
                    System.arraycopy(firstPair.a, l_ind, b, i, firstPair.a.length-l_ind);
                    break;
                }
                // Выбираем, какой элемент меньше, и записываем его в конечный отсортированный массив b
                else {
                    if (firstPair.a[l_ind] <= secondPair.a[r_ind]) {
                        b[i] = firstPair.a[l_ind];
                        l_ind++;
                    } else {
                        // Прибавляем к общему количеству инверсий оставшуюся длину первого массива
                        total_invers += firstPair.a.length - l_ind;
                        b[i] = secondPair.a[r_ind];
                        r_ind++;
                    }
                }
            }
            return new Pair(b, total_invers);
        }
    }

    // Класс Pair, совмещающий в себе массив и количество инверсий
    public static class Pair {
        public int[] a;
        public int total_invers;

        Pair(int[] a, int total_invers) {
            this.a = a;
            this.total_invers = total_invers;
        }
    }

}
