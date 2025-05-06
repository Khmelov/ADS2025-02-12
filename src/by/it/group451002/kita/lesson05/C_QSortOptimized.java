package by.it.group451002.kita.lesson05;

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
        //сортируем отрезки по началу
        quickSort(segments, 0, n-1);

        for (int i = 0; i <= m-1;i++) {
            int left = 0;
            int right = segments.length - 1;
            int res = -1;

            //с помощью бинарного поиска ищем последний отрезок по началу, который покрывает это событие
            //(возможно, так как мы не проверяем конец отрезка)
            while (left <= right) {
                int mid = (left+right)/2;
                if (segments[mid].start <= points[i]){
                    res = mid;
                    left = mid + 1;
                }
                else{
                    right = mid - 1;
                }
            }

            int count = 0;
            for (int j = res; j >= 0; j--){
                //теперь точно находим покрывающие отрезки
                if (segments[j].stop >= points[i]){
                    count++;
                }
            }
            //сохраняем количество покрывающих отрезков для данной точки
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //3-разбиение
    private static int[] partition(Segment[] segments, int l, int r){
        //массив из 2 элементов, содержащих индексы тех элементов,
        // с которых начинаются и заканчиваются элементы равные опорному
        int [] parts = new int[2];
        int less = l;
        int greater = r;
        int i = less+1;
        Segment x = segments[i];

        while (i <= greater){
            //если элемент меньше, чем опорный
            if (segments[i].compareTo((Object) x) < 0){
                Segment temp = segments[less];
                segments[less] = segments[i];
                segments[i] = temp;
                less++;
                i++;
            }
            //если элемент больше, чем опорный
            else if (segments[i].compareTo((Object) x) > 0){
                Segment temp = segments[greater];
                segments[greater] = segments[i];
                segments[i] = temp;
                greater--;
            }
            //если элемент равен опорному
            else{
                i++;
            }
        }
        parts[0] = less;
        parts[1] = greater;

        return parts;
    }
    //элиминация хвостовой рекурсии
    private static void quickSort(Segment[] segments, int l, int r){
        if (l < r){
            int[] m = partition(segments, l, r);
            //левая часть сортируется рекурсивно
            quickSort(segments, l, m[0]-1);
            //правая часть сортируется иттеративно
            l = m[1] + 1;
        }
    }


    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            Segment other = (Segment) o;
            if (this.start != other.start){
                return Integer.compare(this.start, other.start);
            }
            return Integer.compare(this.stop, other.stop);
        }
    }

}
