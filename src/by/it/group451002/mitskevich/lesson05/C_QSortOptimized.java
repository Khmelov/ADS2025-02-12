package by.it.group451002.mitskevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Найти, сколько камер записало каждое событие.
Используются:
- Быстрая сортировка с 3-разбиением и оптимизация хвостовой рекурсии
- Бинарный поиск
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
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = scanner.nextInt(); // число отрезков
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt(); // число точек
        int[] points = new int[m];
        int[] result = new int[m]; // массив для результата

        // читаем отрезки
        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            // если начало и конец заданы наоборот — поменять
            if (a > b) {
                int temp = a;
                a = b;
                b = temp;
            }
            segments[i] = new Segment(a, b);
        }

        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // сортировка отрезков по началу (start)
        quickSort3(segments, 0, segments.length - 1);

        // для каждой точки считаем, сколько отрезков её покрывают
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsContainingPoint(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // класс отрезка с методом сравнения по началу отрезка
    private static class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // сортировка по началу отрезка
            return Integer.compare(this.start, o.start);
        }
    }

    // Быстрая сортировка с 3-разбиением и устранением хвостовой рекурсии
    private void quickSort3(Segment[] a, int left, int right) {
        // хвостовая рекурсия вместо стандартной (уменьшаем стек вызовов)
        while (left < right) {
            int lt = left, gt = right;
            Segment pivot = a[left]; // опорный элемент
            int i = left;

            // 3-разбиение: < pivot | == pivot | > pivot
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(a, lt++, i++);
                } else if (cmp > 0) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }

            // вызываем меньшую часть рекурсивно, большую — через цикл (оптимизация)
            if (lt - left < right - gt) {
                quickSort3(a, left, lt - 1); // рекурсия на меньшую часть
                left = gt + 1;               // хвостовая рекурсия
            } else {
                quickSort3(a, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    // Вспомогательная функция для обмена элементов массива
    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // Метод подсчета, сколько отрезков содержат точку point
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        int first = -1;

        // Бинарный поиск последнего отрезка, у которого начало <= точке
        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point) {
                first = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // если нет ни одного отрезка, начинающегося до точки — выходим
        if (first == -1) return 0;

        // проходим с 0 по first и проверяем, входит ли point в отрезок
        int count = 0;
        for (int i = 0; i <= first; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }
}


