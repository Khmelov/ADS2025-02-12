package by.it.group410901.korneew.lesson05;

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
...
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

        // Сортируем отрезки с помощью быстрой сортировки с трёхразбиением
        quickSort(segments, 0, n - 1);

        // Для каждой точки ищем количество отрезков, содержащих её
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsForPoint(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка с трёхразбиением
    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) { // Элиминация хвостовой рекурсии
            // Если подмассив небольшой, используем вставочную сортировку
            if (high - low < 10) {
                insertionSort(segments, low, high);
                break;
            } else {
                // Выполняем трёхразбиение
                int[] pivotRange = partition(segments, low, high);
                int lt = pivotRange[0];
                int gt = pivotRange[1];

                // Рекурсивно сортируем меньшую часть, итеративно продолжаем с большей
                if (lt - low < high - gt) {
                    quickSort(segments, low, lt - 1);
                    low = gt + 1; // Итерация вместо рекурсии для большей части
                } else {
                    quickSort(segments, gt + 1, high);
                    high = lt - 1; // Итерация вместо рекурсии для большей части
                }
            }
        }
    }

    // Трёхразбиение (Dutch National Flag partitioning)
    private int[] partition(Segment[] segments, int low, int high) {
        Segment pivot = segments[low]; // Выбираем первый элемент как опорный
        int lt = low; // Граница элементов, меньших опорного
        int gt = high; // Граница элементов, больших опорного
        int i = low; // Текущий индекс

        while (i <= gt) {
            int cmp = segments[i].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, lt++, i++);
            } else if (cmp > 0) {
                swap(segments, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    // Вставочная сортировка для небольших подмассивов
    private void insertionSort(Segment[] segments, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            Segment key = segments[i];
            int j = i - 1;
            while (j >= low && segments[j].compareTo(key) > 0) {
                segments[j + 1] = segments[j];
                j--;
            }
            segments[j + 1] = key;
        }
    }

    // Обмен элементов
    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    // Подсчёт отрезков, содержащих точку, с использованием бинарного поиска
    private int countSegmentsForPoint(Segment[] segments, int point) {
        // Бинарный поиск для нахождения первого отрезка, который может содержать точку
        int left = 0;
        int right = segments.length - 1;
        int firstValid = segments.length; // Индекс первого подходящего отрезка

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                firstValid = mid; // Этот отрезок может быть подходящим
                right = mid - 1; // Ищем ещё раньше
            } else {
                left = mid + 1;
            }
        }

        // Считаем все отрезки, начиная с firstValid, которые содержат точку
        int count = 0;
        for (int i = firstValid; i < segments.length && segments[i].start <= point; i++) {
            if (point <= segments[i].stop) {
                count++;
            }
        }
        return count;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Обрабатываем случай, если концы отрезка пришли в обратном порядке
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
            // Сравниваем по начальной точке, при равенстве — по конечной
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}