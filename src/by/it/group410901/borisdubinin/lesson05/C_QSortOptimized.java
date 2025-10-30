package by.it.group410901.borisdubinin.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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
        int[] starts = new int[n];
        int[] ends = new int[n];
        quickSort(segments, 0, n - 1);
        for(int i = 0; i < n; i++){
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }
        Arrays.sort(ends);
        for(int i = 0; i < m; i++){
            int point = points[i];
            result[i] = numberSmaller(starts, point) - numberSmaller(ends, point);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void quickSort(Segment[] arr, int begin, int end){
        while(begin < end){
            Segment pivot = arr[end];
            int less = begin;
            int more = end;
            for(int j = begin; j <= more;){
                int compr = arr[j].compareTo(pivot);
                if(compr < 0){
                    Segment temp = arr[j];
                    arr[j] = arr[less];
                    arr[less] = temp;
                    less++;
                    j++;
                }
                else if(compr > 0){
                    Segment temp = arr[j];
                    arr[j] = arr[more];
                    arr[more] = temp;
                    more--;
                }
                else
                    j++;
            }

            if(less-begin < end - more){
                quickSort(arr, begin, less-1);
                begin = more + 2;
            }
            else {
                quickSort(arr, more + 2, end);
                end = less - 1;
            }
        }
    }
    public static int numberSmaller(int[] arr, int key){
        int left = 0, right = arr.length - 1;
        int result = -1;
        while(left <= right){
            int mid = (right+left)/2;
            if(arr[mid] > key)
                right = mid - 1;
            else {
                result = mid;
                left = mid + 1;
            }
        }
        return result + 1;
    }
    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }

}
