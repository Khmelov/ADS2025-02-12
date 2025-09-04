package by.it.group451003.alekseyuk.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;

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
        //число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //сортируем отрезки на месте с помощью оптимизированной быстрой сортировки
        quickSort(segments);

        //для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            //бинарный поиск первого отрезка с start <= point
            int firstIndex = binarySearchFirst(segments, point);
            int count = 0;
            //проверяем все отрезки начиная с firstIndex
            for (int j = firstIndex; j < n && segments[j].start <= point; j++) {
                if (point <= segments[j].stop) {
                    count++;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        long start;
        long stop;

        Segment(long start, long stop) {
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
            if (this.start != o.start) {
                return Long.compare(this.start, o.start);
            }
            return Long.compare(this.stop, o.stop);
        }
    }

    //итеративная быстрая сортировка с 3-сторонним разбиением
    private void quickSort(Segment[] array) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{0, array.length - 1});

        while (!stack.isEmpty()) {
            int[] range = stack.pop();
            int left = range[0];
            int right = range[1];

            if (left >= right) {
                continue;
            }

            //3-стороннее разбиение
            int[] bounds = partition(array, left, right);
            int lt = bounds[0]; //меньше пивота
            int gt = bounds[1]; //больше пивота

            //добавляем подмассивы в стек, больший первым
            if (gt - lt - 1 > right - gt) {
                stack.push(new int[]{lt + 1, gt - 1}); //равные пивоту
                stack.push(new int[]{gt, right});       //больше пивота
                stack.push(new int[]{left, lt});        //меньше пивота
            } else {
                stack.push(new int[]{left, lt});        //меньше пивота
                stack.push(new int[]{lt + 1, gt - 1});  //равные пивоту
                stack.push(new int[]{gt, right});       //больше пивота
            }
        }
    }

    //3-стороннее разбиение
    private int[] partition(Segment[] array, int left, int right) {
        Segment pivot = array[left];
        int lt = left;     //граница элементов < пивота
        int gt = right;    //граница элементов > пивота
        int i = left;      //текущий элемент

        while (i <= gt) {
            int cmp = array[i].compareTo(pivot);
            if (cmp < 0) {
                swap(array, lt++, i++);
            } else if (cmp > 0) {
                swap(array, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt - 1, gt + 1};
    }

    //обмен элементов
    private void swap(Segment[] array, int i, int j) {
        Segment temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    //бинарный поиск первого отрезка с start <= point
    private int binarySearchFirst(Segment[] segments, long point) {
        int left = 0;
        int right = segments.length - 1;
        int result = segments.length; //если не найдено, возвращаем n

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid; //возможный кандидат
                right = mid - 1; //ищем левее
            } else {
                left = mid + 1;
            }
        }
        return result;
    }
}