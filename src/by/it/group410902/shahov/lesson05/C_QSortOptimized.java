package by.it.group410902.shahov.lesson05;

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

        sort(segments,0, segments.length - 1);
        for(int i = 0; i < m; i++){
            int p = points[i];
            int fi = search_f(segments,p);
            if( fi == -1){
                result[i] = 0;
                continue;
            }
            int count = 0;
            for(int j = fi; j >=0; j--){
                if(segments[j].stop >= p) count++;
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    public int search_f(Segment[] arr, int point){
        int left = 0;
        int right = arr.length-1;
        int res = -1;
        while(left<=right){
            int mid = (left + right) /2;
            if(arr[mid].start <= point){
                res = mid;
                left = mid + 1;
            }
            else{
                right = mid - 1;
            }
        }
        return res;
    }
    public void sort(Segment[] arr, int low, int high){
        while(low < high){
            int[] pivots = razd(arr,low,high);
            if(pivots[0] - low < high - pivots[1]){
                sort(arr,low,pivots[0]-1);
                low = pivots[1]+1;
            }
            else{
                sort(arr,pivots[1] + 1,high);
                high = pivots[0]-1;
            }
        }
    }
    public int[] razd(Segment[] arr, int low, int high){
        int[] pivots = new int[2];
        Segment pivotEl = arr[low];
        int pl = low;
        int i = low + 1;
        int ph = high;
        while(i < ph){
            int r = arr[i].compareTo(pivotEl);
            if(r < 0) {
                change(arr,pl,i);
                i++; pl++;
            } else if (r > 0) {
                change(arr,i,ph);
                ph--;
            }
            else{
                i++;
            }
        }
        pivots[0] = pl;
        pivots[1] = ph;
        return pivots;
    }
    public static void change(Segment[] arr, int i, int j){
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}
