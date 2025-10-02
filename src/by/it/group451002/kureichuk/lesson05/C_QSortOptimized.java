package by.it.group451002.kureichuk.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

    void Partition(Segment[] Arr,int l,int r,int[] res){
        Segment x = Arr[l];
        int j = l-1;
        Segment temp;
        for (int i = l; i <= r; i++){
            if (Arr[i].compareTo(x) < 0) {
                j++;
                res[1]++;
                temp=Arr[res[1]];
                Arr[res[1]]=Arr[j];
                Arr[j]=temp;
                if(i!=res[1]) {
                    temp = Arr[i];
                    Arr[i] = Arr[j];
                    Arr[j] = temp;
                }
            }
            else{
                if(Arr[i].compareTo(x)==0){
                    res[1]++;
                    temp=Arr[i];
                    Arr[i]=Arr[res[1]];
                    Arr[res[1]]=temp;
                }
            }
        }
        res[0]=j+1;
    }
    void QSort(Segment[] Arr,int l,int r){
        while(l<r){
            int[] res = new int[2];
            res[0]=l-1;
            res[1]=l-1;
            Partition(Arr,l,r,res);
            QSort(Arr,l,res[0]-1);
            l=res[1]+1;
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

        QSort(segments,0,n-1);
        // Arrays.sort(segments);
        for(int i=0;i<m;i++){
            int j=0;
            result[i]=0;
            while((j<n)&&(segments[j].start<=points[i])){
                if (points[i]<=segments[j].stop){
                    result[i]++;
                }
                j++;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
