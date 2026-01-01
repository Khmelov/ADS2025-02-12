package by.it.group451002.mishchenko.lesson05;

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
        // Получаем входной поток данных из файла dataC.txt. Файл должен находиться в том же пакете.
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        // Создаем экземпляр класса для вызова нестатичного метода getAccessory2.
        C_QSortOptimized instance = new C_QSortOptimized();
        // Вызываем основной метод, который возвращает массив с количеством отрезков,
        // покрывающих каждую точку.
        int[] result = instance.getAccessory2(stream);
        // Выводим результат в консоль.
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    /**
     * Основной метод решения задачи.
     * Читает данные, сортирует отрезки, подготавливает массивы начальных и конечных точек,
     * а затем для каждой точки ищет, сколько отрезков её покрывают.
     *
     * @param stream Входной поток из файла с данными.
     * @return Массив, где каждый элемент – число отрезков, покрывающих соответствующую точку.
     * @throws FileNotFoundException, если файл не найден.
     */
    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        // Создаем Scanner для чтения входных данных.
        Scanner scanner = new Scanner(stream);

        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        // Читаем количество отрезков.
        int n = scanner.nextInt();
        // Создаем массив объектов Segment для хранения отрезков.
        Segment[] segments = new Segment[n];

        // Читаем количество точек, для которых нужно определить покрытие.
        int m = scanner.nextInt();
        // Массив для точек.
        int[] points = new int[m];
        // Массив для хранения результата (количество отрезков, покрывающих каждую точку).
        int[] result = new int[m];

        // Считываем отрезки: для каждого отрезка вводятся две координаты.
        for (int i = 0; i < n; i++) {
            // Создаем отрезок с заданными координатами начала и конца.
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // Считываем точки.
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем массив отрезков по возрастанию их начала, а при равенстве – по возрастанию конца.
        // Для этого используется оптимизированная (in-place) быстрая сортировка с 3-разбиением и хвостовой рекурсией.
        quickSortSegments(segments, 0, n - 1);

        // Формируем массивы начальных и конечных точек.
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            // Массив starts будет отсортирован, так как segments отсортирован по start.
            starts[i] = segments[i].start;
            // Массив ends — изначально не гарантированно отсортирован, поэтому его отдельно сортируем.
            ends[i] = segments[i].stop;
        }

        // Сортируем массив ends с использованием оптимизированной быстрой сортировки для int.
        quickSortInts(ends, 0, n - 1);

        /*
         Для каждой точки x:
         - Считаем количество отрезков, у которых начало (start) меньше или равно x.
           Это делается с помощью метода upperBound, который возвращает индекс первого элемента, большего x.
         - Считаем количество отрезков, у которых конец (stop) меньше x.
           Для этого используется метод lowerBound.
         Разница между этими значениями и есть число отрезков, покрывающих точку x.
         */
        for (int i = 0; i < m; i++) {
            int x = points[i];
            int countStarts = upperBound(starts, x);    // Число отрезков с start <= x.
            int countEnds = lowerBound(ends, x);          // Число отрезков с stop < x.
            result[i] = countStarts - countEnds;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        return result;
    }

    /**
     * Оптимизированная быстрая сортировка для массива отрезков с использованием 3-разбиения и хвостовой рекурсии.
     * Отрезки сортируются по возрастанию их начала (start), а при равенстве — по возрастанию конца (stop).
     *
     * @param segments Массив отрезков, который сортируется на месте.
     * @param low      Нижний индекс сортируемой части.
     * @param high     Верхний индекс сортируемой части.
     */
    private void quickSortSegments(Segment[] segments, int low, int high) {
        while (low < high) { // Используем цикл для устранения хвостовой рекурсии.
            // Выбираем опорный элемент: в данном случае — первый элемент в диапазоне.
            Segment pivot = segments[low];
            int lt = low;    // Все элементы слева от lt будут меньше pivot.
            int gt = high;   // Все элементы справа от gt будут больше pivot.
            int i = low;
            // 3-разбиение: группируем элементы на три части: меньше, равные и больше опорного.
            while (i <= gt) {
                int cmp = segments[i].compareTo(pivot);
                if (cmp < 0) {
                    // Текущий элемент меньше опорного, меняем его с элементом на позиции lt.
                    swap(segments, lt, i);
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    // Текущий элемент больше опорного — меняем его с элементом на позиции gt.
                    swap(segments, i, gt);
                    gt--;
                } else {
                    // Текущий элемент равен опорному, просто передвигаемся дальше.
                    i++;
                }
            }
            // Рекурсивно сортируем меньшую из двух частей, а затем переходим к сортировке большей части
            // посредством хвостовой рекурсии.
            if ((lt - low) < (high - gt)) {
                quickSortSegments(segments, low, lt - 1);
                low = gt + 1; // Хвостовая рекурсия для правой части.
            } else {
                quickSortSegments(segments, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    /**
     * Оптимизированная быстрая сортировка для массива целых чисел с использованием 3-разбиения и хвостовой рекурсии.
     *
     * @param arr  Массив целых чисел для сортировки.
     * @param low  Нижний индекс сортируемого диапазона.
     * @param high Верхний индекс сортируемого диапазона.
     */
    private void quickSortInts(int[] arr, int low, int high) {
        while (low < high) {
            // Выбираем опорный элемент (pivot) как первый элемент диапазона.
            int pivot = arr[low];
            int lt = low;
            int gt = high;
            int i = low;
            // 3-разбиение: разделяем массив на три части: меньшие, равные и большие опорного элемента.
            while (i <= gt) {
                if (arr[i] < pivot) {
                    swap(arr, lt, i);
                    lt++;
                    i++;
                } else if (arr[i] > pivot) {
                    swap(arr, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }
            // Сначала рекурсивно сортируем меньшую часть, затем с помощью хвостовой рекурсии продолжаем сортировку большей части.
            if ((lt - low) < (high - gt)) {
                quickSortInts(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSortInts(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    /**
     * Бинарный поиск для определения первой позиции в отсортированном массиве,
     * где элемент строго больше заданного ключа.
     * Фактически, этот метод возвращает количество элементов,
     * которые меньше или равны ключу.
     *
     * @param arr Отсортированный массив целых чисел.
     * @param key Искомый ключ.
     * @return Индекс первого элемента, который больше key.
     */
    private int upperBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] <= key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    /**
     * Бинарный поиск для определения первой позиции в отсортированном массиве,
     * где элемент не меньше заданного ключа (то есть, первый элемент, который >= key).
     *
     * @param arr Отсортированный массив целых чисел.
     * @param key Искомый ключ.
     * @return Индекс первого элемента, не меньшего key.
     */
    private int lowerBound(int[] arr, int key) {
        int low = 0;
        int high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] < key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    /**
     * Метод для обмена двух элементов в массиве объектов Segment.
     *
     * @param arr Массив объектов Segment.
     * @param i   Индекс первого элемента.
     * @param j   Индекс второго элемента.
     */
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Метод для обмена двух элементов в массиве целых чисел.
     *
     * @param arr Массив целых чисел.
     * @param i   Индекс первого элемента.
     * @param j   Индекс второго элемента.
     */
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Класс Segment представляет отрезок с начальной и конечной точками.
     * Реализация интерфейса Comparable позволяет сортировать отрезки.
     * Сортировка производится по возрастанию поля start, а при равенстве — по возрастанию поля stop.
     */
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        /**
         * Конструктор отрезка.
         *
         * @param start Начальная точка отрезка.
         * @param stop  Конечная точка отрезка.
         */
        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        /**
         * Метод compareTo выполняет сравнение двух отрезков.
         * Сначала сравниваются значения start; если они различны – возвращается разница.
         * Если значения start равны, сравниваются значения stop.
         *
         * @param other Другой отрезок для сравнения.
         * @return Отрицательное число, если this меньше other, положительное если больше, или 0 при равенстве.
         */
        @Override
        public int compareTo(Segment other) {
            if (this.start != other.start) {
                return this.start - other.start;
            } else {
                return this.stop - other.stop;
            }
        }
    }
}
