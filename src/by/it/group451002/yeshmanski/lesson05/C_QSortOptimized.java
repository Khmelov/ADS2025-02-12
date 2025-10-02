
package by.it.group451002.yeshmanski.lesson05;

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

    // Трехстороннее разбиение (3-way partitioning) массива сегментов для QSort
    void Partition(Segment[] Arr,int l,int r,int[] res){
        Segment x = Arr[l]; // опорный элемент
        int j = l-1;        // индекс для элементов меньше опорного
        Segment temp;

        // Проход по массиву для разделения на три части:
        // меньше, равны и больше опорного
        for (int i = l; i <= r; i++){
            // Если элемент меньше опорного
            if (Arr[i].compareTo(x) < 0) {
                j++;
                res[1]++;  // увеличиваем индекс для равных и меньше элементов
                // Меняем местами элементы для группировки
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
                // Если элемент равен опорному — меняем для группировки
                if(Arr[i].compareTo(x)==0){
                    res[1]++;
                    temp=Arr[i];
                    Arr[i]=Arr[res[1]];
                    Arr[res[1]]=temp;
                }
            }
        }
        // Устанавливаем границу для дальнейших рекурсивных вызовов
        res[0]=j+1;
    }

    // Быстрая сортировка с использованием хвостовой рекурсии и 3-разбиения
    void QSort(Segment[] Arr,int l,int r){
        while(l<r){
            int[] res = new int[2];
            res[0]=l-1;
            res[1]=l-1;
            Partition(Arr,l,r,res);

            // Рекурсивный вызов для левой части массива (меньше опорного)
            QSort(Arr,l,res[0]-1);

            // Элиминация хвостовой рекурсии: сдвигаем левую границу
            l=res[1]+1;
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Чтение количества отрезков и точек
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков (времён работы камер)
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // Чтение точек (времён событий)
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по началу для последующего эффективного поиска
        QSort(segments,0,n-1);

        // Для каждой точки нужно найти количество подходящих отрезков
        for(int i=0; i<m; i++){
            // TODO: Здесь можно применить бинарный поиск, чтобы найти
            // первый подходящий отрезок, у которого start <= points[i],
            // а затем двигаться вправо пока отрезок включает точку.

            // Сейчас реализован простой перебор — работает, но медленнее
            int j=0;
            result[i]=0;

            // Перебираем отрезки пока их start <= текущая точка
            while((j<n)&&(segments[j].start <= points[i])){
                // Если точка входит в отрезок — увеличиваем счётчик
                if (points[i]<=segments[j].stop){
                    result[i]++;
                }
                j++;
            }
        }
        return result;
    }

    // Класс сегмента с реализацией Comparable для сортировки по start
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сравниваем по началу отрезков для сортировки
            return Integer.compare(this.start, o.start);
        }
    }
}
