package by.it.group451001.puzik.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

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
        
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        
        qSortOptimized(segments, 0, n - 1);
        
        // Для каждой точки находим количество отрезков, которые ее содержат
        for (int i = 0; i < m; i++) {
            int point = points[i];
            
            // Используем бинарный поиск для нахождения первого подходящего отрезка
            int count = countSegmentsContainingPoint(segments, point);
            
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    
    // Метод для подсчета отрезков, содержащих точку
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        int count = 0;
        
        for (Segment segment : segments) {
            if (segment.start <= point && point <= segment.stop) {
                count++;
            } else if (segment.start > point) {
                // Если начало отрезка больше точки, то все последующие отрезки
                // (т.к. они отсортированы по началу) тоже будут начинаться после точки
                break;
            }
        }
        
        return count;
    }
    
    // Оптимизированная быстрая сортировка с элиминацией хвостовой рекурсии
    private void qSortOptimized(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] pivots = partition3(arr, low, high);
            
            if (pivots[0] - low < high - pivots[1]) {
                qSortOptimized(arr, low, pivots[0] - 1);
                low = pivots[1] + 1; // Элиминация хвостовой рекурсии для большей части
            } else {
                qSortOptimized(arr, pivots[1] + 1, high);
                high = pivots[0] - 1; // Элиминация хвостовой рекурсии для меньшей части
            }
        }
    }
    
    // Разбиение массива на три части: меньше опорного, равные опорному, больше опорного
    private int[] partition3(Segment[] arr, int low, int high) {
        int mid = low + (high - low) / 2;
        Segment pivot = medianOfThree(arr[low], arr[mid], arr[high]);
        
        int i = low;
        int lt = low;
        int gt = high;
        
        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            
            if (cmp < 0) {
                swap(arr, lt++, i++);
            } else if (cmp > 0) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }
        
        return new int[] {lt, gt};
    }
    
    // Выбор медианы из трех элементов
    private Segment medianOfThree(Segment a, Segment b, Segment c) {
        if (a.compareTo(b) < 0) {
            if (b.compareTo(c) < 0) return b;
            else if (a.compareTo(c) < 0) return c;
            else return a;
        } else {
            if (a.compareTo(c) < 0) return a;
            else if (b.compareTo(c) < 0) return c;
            else return b;
        }
    }
    
    // Обмен элементов массива
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }

}
