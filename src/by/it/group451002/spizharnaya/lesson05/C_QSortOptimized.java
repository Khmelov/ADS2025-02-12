package by.it.group451002.spizharnaya.lesson05;

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
        //сортируем отрезки по левому краю
        QuickSort(segments,0, n-1);
        for (int i=0; i<m; i++){       //перебираем все точки
            int j = BinSearch(segments,points[i]);     //индекс отрезка, покрывающего точку
            if (j != -1){
                result[i]++;
                int k = j-1;
                while((k>=0)&&(segments[k].stop>=points[i])){
                    result[i]++;
                    k--;
                }
                k = j+1;
                while((k<n)&&(segments[k].start<=points[i])){
                    result[i]++;
                    k++;
                }
            }
            else{
                result[i]=0;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    void QuickSort(Segment[] dist, int left, int right){
        int len=1;
        while (left < right){
            int m = Partition(dist,left,right,len);
            QuickSort(dist, left, m-1);
            left = m + len;
        }
    }

    int Partition(Segment[] dist, int left, int right, int numx){
        //numx - кол-во элементов равных опорному
        Segment x = dist[left];      //опорный элемент
        int j = left;
        for(int i=left+1; i<=right; i++){
            if (dist[i].start<=x.start){
                j++;
                Segment temp = dist[i];
                dist[i] = dist[j];
                dist[j] = temp;
            }
            if (dist[i].start==x.start){
                Segment temp = dist[left+numx];
                dist[left+numx] = dist[j];
                dist[j] = temp;
                numx++;
            }
        }
        //ставим опорный элемент (и равные ему) в нужное место
        //к этому моменту numx элементов =х вначале,
        //затем до j включительно элементы <x, затем >x
        for (int i=0; i<numx; i++){
            Segment temp = dist[left+i];
            dist[left+i] = dist[j-i];
            dist[j-i] = temp;
        }
        //возвращаем индекс первого опорного элемента
        return j-numx+1;
    }

    //возвращает индекс отрезка в массиве arr, который покрывает point
    int BinSearch(Segment[] arr, int point){
        boolean stop = false;
        int left = 0;
        int right = arr.length-1;
        int mid = -1;
        while (!stop){
            mid = (left+right)/2;

            if (arr[mid].start>point){
                right = mid;  //точка слева
            }
            else{
                if (arr[mid].stop<point){
                    left=mid;   //точка справа
                }
                else{
                    return mid;  //точка покрыта отрезком arr[mid]
                }
            }

            if ((left+right)/2 == mid){
                stop = true;      //если значение mid повторяется, значит искомого отрезка нет
            }
        }
        return -1;  //если не найдено
    }

    //отрезок
    private class Segment implements Comparable<Integer> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Integer o) {
            return 0;
        }
    }

}
