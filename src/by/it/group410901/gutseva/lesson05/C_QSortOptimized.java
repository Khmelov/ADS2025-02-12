package by.it.group410901.gutseva.lesson05;

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
// Сортируем отрезки
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки считаем сколько отрезков её покрывает
        for (int i = 0; i < m; i++) {
            result[i] = countSegments(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    void quickSort(Segment[] arr, int left, int right) {
        while (left < right) {
            int[] m = partition3(arr, left, right);
            // Оптимизация: сначала рекурсивно обрабатывать меньшую часть
            if (m[0] - left < right - m[1]) {
                quickSort(arr, left, m[0] - 1);
                left = m[1] + 1;
            } else {
                quickSort(arr, m[1] + 1, right);
                right = m[0] - 1;
            }
        }
    }

    // Метод разделения массива на 3 части 
    int[] partition3(Segment[] arr, int left, int right) {
        Segment pivot = arr[left];
        int lt = left, gt = right;
        int i = left;
        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                // Элемент меньше опорного - перемещаем в левую часть
                swap(arr, lt++, i++);
            } else if (cmp > 0) {
                // Элемент больше опорного - перемещаем в правую часть
                swap(arr, i, gt--);
            } else {
                // Элемент равен опорному - оставляем на месте
                i++;
            }
        }
        // Возвращаем границы равных элементов
        return new int[]{lt, gt};
    }

    // Вспомогательный метод для обмена элементов массива
    void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Метод для подсчета количества отрезков, покрывающих заданную точку
    int countSegments(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        int count = 0;

        // Бинарный поиск отрезка, содержащего точку
        while (left <= right) {
            int mid = (left + right) / 2;

            // Если нашли отрезок, содержащий точку
            if (segments[mid].start <= point && segments[mid].stop >= point) {
                count = 1;  // Найденный отрезок

                // Проверяем отрезки слева от найденного
                int l = mid - 1;
                while (l >= 0 && segments[l].start <= point && segments[l].stop >= point) {
                    count++;
                    l--;
                }

                // Проверяем отрезки справа от найденного
                int r = mid + 1;
                while (r < segments.length && segments[r].start <= point && segments[r].stop >= point) {
                    count++;
                    r++;
                }
                break;
            }
            // Если начало отрезка больше точки - ищем в левой половине
            else if (segments[mid].start > point) {
                right = mid - 1;
            }
            // Иначе ищем в правой половине
            else {
                left = mid + 1;
            }
        }
        return count;
    }

    // Класс, представляющий отрезок [start, stop]
    private class Segment implements Comparable<Segment> {
        int start;  // Начальная точка отрезка
        int stop;   // Конечная точка отрезка

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        // Метод сравнения отрезков
        @Override
        public int compareTo(Segment other) {
            // Сначала сравниваем по начальной точке
            if (this.start != other.start)
                return Integer.compare(this.start, other.start);
                // Если начальные точки равны, сравниваем по конечной точке
            else
                return Integer.compare(this.stop, other.stop);
        }
    }
}
