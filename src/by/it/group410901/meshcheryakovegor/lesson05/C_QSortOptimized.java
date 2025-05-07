package by.it.group410901.meshcheryakovegor.lesson05;

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
    // Быстрая сортировка отрезков с трёхпутевым разбиением и устранением хвостовой рекурсии
    void quickSort(Segment[] a) {
        class StackFrame {
            int left, right;

            StackFrame(int l, int r) {
                left = l;
                right = r;
            }
        }

        java.util.Stack<StackFrame> stack = new java.util.Stack<>();
        stack.push(new StackFrame(0, a.length - 1)); // начальная область

        while (!stack.isEmpty()) {
            StackFrame frame = stack.pop();
            int l = frame.left, r = frame.right;
            if (l >= r) continue;

            Segment pivot = a[l]; // опорный элемент
            int lt = l, gt = r, i = l + 1;

            // Трёхпутевое разбиение: < pivot, == pivot, > pivot
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else i++;
            }

            // Сохраняем левые и правые части для дальнейшей обработки
            stack.push(new StackFrame(l, lt - 1));
            stack.push(new StackFrame(gt + 1, r));
        }
    }

    void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
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
        // Сортируем отрезки
        quickSort(segments);

        // Для каждой точки считаем количество отрезков, которые её покрывают
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Используем бинарный поиск, чтобы найти последний отрезок, чей start <= point
            int left = 0;
            int right = segments.length - 1;
            int found = -1;

            while (left <= right) {
                int mid = (left + right) / 2;
                if (segments[mid].start <= point) {
                    found = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // Теперь от начала до найденной позиции проверяем, входит ли точка в отрезок
            int count = 0;
            for (int j = 0; j <= found; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            // Записываем результат
            result[i] = count;
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
            Segment other = (Segment) o;
            if (this.start != other.start)
                return Integer.compare(this.start, other.start);
            return Integer.compare(this.stop, other.stop);
        }

    }

}
