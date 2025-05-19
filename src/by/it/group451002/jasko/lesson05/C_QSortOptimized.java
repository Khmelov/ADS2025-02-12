package by.it.group451002.jasko.lesson05;

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
        // Загрузка данных из файла
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        // Получение и вывод результатов
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Чтение количества отрезков (периодов работы камер)
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // Чтение количества точек (моментов событий)
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение и создание отрезков
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        // Чтение моментов времени событий
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Оптимизированная сортировка отрезков по времени начала
        quickSort(segments, 0, n - 1);

        // Для каждой точки находим количество содержащих её отрезков
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsContainingPoint(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Оптимизированная быстрая сортировка с 3-разбиением
    private void quickSort(Segment[] segments, int low, int high) {
        // Используем цикл вместо рекурсии для правой части
        while (low < high) {
            // Выполняем 3-разбиение массива
            int[] partitionIndices = partition(segments, low, high);
            int lt = partitionIndices[0]; // Граница элементов < опорного
            int gt = partitionIndices[1]; // Граница элементов > опорного

            // Рекурсивно сортируем меньшую часть
            if (lt - low < high - gt) {
                quickSort(segments, low, lt - 1);
                low = gt + 1; // Переходим к сортировке правой части
            } else {
                quickSort(segments, gt + 1, high);
                high = lt - 1; // Переходим к сортировке левой части
            }
        }
    }

    // 3-разбиение массива
    private int[] partition(Segment[] segments, int low, int high) {
        // Опорный элемент (выбираем последний)
        Segment pivot = segments[high];
        int lt = low;  // Указатель для элементов < опорного
        int gt = high; // Указатель для элементов > опорного
        int i = low;   // Текущий элемент

        while (i <= gt) {
            int cmp = segments[i].compareTo(pivot);
            if (cmp < 0) {
                // Элемент меньше опорного - перемещаем в левую часть
                swap(segments, i++, lt++);
            } else if (cmp > 0) {
                // Элемент больше опорного - перемещаем в правую часть
                swap(segments, i, gt--);
            } else {
                // Элемент равен опорному - оставляем на месте
                i++;
            }
        }

        // Возвращаем границы равных элементов
        return new int[]{lt, gt};
    }

    // Обмен элементов массива местами
    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    // Подсчет отрезков, содержащих точку, с использованием бинарного поиска
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        int count = 0;
        int left = 0, right = segments.length - 1;
        int lastPossible = -1; // Индекс последнего отрезка, который может содержать точку

        // Бинарный поиск правой границы отрезков, где start <= point
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                lastPossible = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Проверяем все подходящие отрезки (от lastPossible вниз)
        for (int i = lastPossible; i >= 0; i--) {
            if (point <= segments[i].stop) {
                count++;
            }
            // Оптимизация: если точка выходит за начало, дальше можно не проверять
            if (point < segments[i].start) {
                break;
            }
        }

        return count;
    }

    // Класс отрезка с реализацией сравнения по начальной точке
    private static class Segment implements Comparable<Segment> {
        int start; // Начало отрезка
        int stop;  // Конец отрезка

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment other) {
            // Сравнение только по начальной точке для сортировки
            return Integer.compare(this.start, other.start);
        }
    }
}