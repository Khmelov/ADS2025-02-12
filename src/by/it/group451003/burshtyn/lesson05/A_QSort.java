package by.it.group451003.burshtyn.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

Первая строка задана два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки)       1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной камеры).
Последняя строка содержит m целых чисел — координаты точек.
Точка принадлежит отрезку, если она внутри него или на границе.

Для каждой точки в порядке входа выведите,
скольким отрезкам она принадлежит.
*/
public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int count : result) {
            System.out.print(count + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем количество отрезков и точек
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        // Массив для начала и конца каждого отрезка
        int[] starts = new int[n];
        int[] ends   = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            ends[i]   = scanner.nextInt();
        }

        // Массив точек и массив-результат
        int[] points = new int[m];
        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем начала и концы (двусвязный quicksort для примитивов)
        Arrays.sort(starts);
        Arrays.sort(ends);

        // Для каждой точки считаем:
        // countStarts = число start <= point
        // countEndsBefore = число end   <  point
        // ответ = countStarts - countEndsBefore
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int countStarts      = countLessOrEqual(starts, p);
            int countEndsBefore  = countLess(ends, p);
            result[i] = countStarts - countEndsBefore;
        }

        return result;
    }

    // количество элементов <= x
    private int countLessOrEqual(int[] a, int x) {
        int lo = 0, hi = a.length - 1, idx = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] <= x) {
                idx = mid;
                lo  = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return idx + 1;
    }

    // количество элементов < x
    private int countLess(int[] a, int x) {
        int lo = 0, hi = a.length - 1, idx = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < x) {
                idx = mid;
                lo  = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return idx + 1;
    }
}
