package by.it.group451002.koltsov.lesson05;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // используя алгоритм QuickSort сортируем массив "segments"
        QuickSort(0, segments.length - 1, segments);

        // для каждой точки из массива points
        for (int i = 0; i < points.length; i++) {

            // инициализируем левую и правую границы для бинарного поиска
            int left = 0;
            int right = segments.length - 1;
            while (right - left > 1) {
                // рассчитываем середину
                int x = (left + right) / 2;

                // передвигаем на середину левую границу, если начало сегмента,
                // находящегося там <= точки
                if (segments[x].start <= points[i])
                    left = x;
                else
                    // передвигаем на середину правую границу, если начало сегмента,
                    // находящегося там > точки
                    right = x;
            }

            int index = 0;
            if (segments[right].start == points[i])
                index = right;
            else
                index = left;

            // вычисляем результат
            for (int k = index; k >= 0; k--) {
                if (segments[k].stop >= points[i])
                    result[i]++;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;
        public static List<Integer> bounds = new ArrayList<Integer>();
        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            }
            else {
                this.stop = start;
                this.start = stop;
            }
        }

        @Override
        public int compareTo(Object o) {
            // если начало < и конец <,  то и сегмент <
            if (this.start < ((Segment)o).start || this.start == ((Segment)o).start && this.stop < ((Segment)o).stop)
                return -1;
            // если начало = и конец =,  то и сегмент =
            if (this.start == ((Segment)o).start && this.stop == ((Segment)o).stop)
                return 0;
            // иначе сегмент >
            return 1;
        }
    }

    public void QuickSort(int l, int r, Segment[] arr) {
        // выбираем опорный элемент
        Segment pivot = arr[l];
        Segment tempSegment = new Segment(0, 0);


        int j = l;
        int i = l + 1;

        //  индекс первого элемента равного pivot
        int lastPivotInd = l;
        while (i <= r) {
            if (arr[i].compareTo(pivot) == 0)
            {
                // если элемент = pivot, то мы меняем его местами с j-ым эл-ом
                j++;
                tempSegment = arr[j];
                arr[j] = arr[i];
                arr[i] = tempSegment;
            }
            else if (arr[i].compareTo(pivot) == -1)
            {
                // если элемент < pivot, то мы сначала меняем его местами с j-ым эл-ом
                // и потом меняем j-ый с первым элементом равным pivot
                j++;
                tempSegment = arr[j];
                arr[j] = arr[i];
                arr[i] = tempSegment;

                tempSegment = arr[j];
                arr[j] = arr[lastPivotInd];
                arr[lastPivotInd] = tempSegment;
                lastPivotInd++;
            }
            i++;

            if(i > r)
                // если мы должны закончить данный этап сортировки
                if (lastPivotInd > l && j < r) {
                    // если сортировать нужно два подмассива
                    // для левой вызываем QuickSort
                    QuickSort(l, lastPivotInd - 1, arr);

                    // для правой части значения меняем по месту, таким образом мз цикла мы не
                    // выйдем, а будем её сортировать
                    j++;
                    l = j;
                    pivot = arr[l];
                    lastPivotInd = l;
                    i = l + 1;
                } else if (lastPivotInd > l) {
                    // если есть что сортировать в левом подмассиве
                    // для левой части значения меняем по месту, таким образом мз цикла мы не
                    // выйдем, а будем её сортировать
                    r = lastPivotInd - 1;
                    pivot = arr[l];
                    lastPivotInd = l;
                    j = l;
                    i = l + 1;
                } else if (j < r) {
                    // если есть что сортировать в правом подмассиве
                    // для правой части значения меняем по месту, таким образом мз цикла мы не
                    // выйдем, а будем её сортировать
                    j++;
                    l = j;
                    pivot = arr[l];
                    lastPivotInd = l;
                    i = l + 1;
                }
        }
    }
}
