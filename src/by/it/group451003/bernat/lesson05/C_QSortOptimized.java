package by.it.group451003.bernat.lesson05;

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
        // Загружаем входные данные из файла "dataC.txt"
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        // Вызываем метод для подсчета количества камер, записавших каждое событие
        int[] result = instance.getAccessory2(stream);
        // Выводим результат: количество камер для каждой точки
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        // Создаем объект Scanner для чтения данных из входного потока
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        // Считываем количество отрезков (камер)
        int n = scanner.nextInt();
        // Создаем массив для хранения отрезков
        Segment[] segments = new Segment[n];
        // Считываем количество точек (событий)
        int m = scanner.nextInt();
        // Создаем массив для хранения координат точек
        int[] points = new int[m];
        // Создаем массив для хранения результата (количество камер для каждой точки)
        int[] result = new int[m];

        // Читаем данные об отрезках (время работы камер)
        for (int i = 0; i < n; i++) {
            // Считываем начало и конец отрезка
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // Создаем объект Segment и сохраняем его в массив
            segments[i] = new Segment(start, stop);
        }
        // Читаем координаты точек (время событий)
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем массив отрезков на месте с помощью оптимизированной быстрой сортировки
        quickSort(segments);

        // Для каждой точки находим количество покрывающих ее отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i]; // Текущая точка
            // Используем бинарный поиск, чтобы найти первый отрезок, у которого start <= point
            int firstIndex = binarySearchFirst(segments, point);
            int count = 0; // Счетчик подходящих отрезков
            // Проверяем все отрезки, начиная с firstIndex, у которых start <= point
            for (int j = firstIndex; j < n && segments[j].start <= point; j++) {
                // Если точка находится внутри отрезка (point <= stop), увеличиваем счетчик
                if (point <= segments[j].stop) {
                    count++;
                }
            }
            // Сохраняем количество подходящих отрезков для текущей точки
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Возвращаем массив с количеством камер для каждой точки
        return result;
    }

    // Класс для представления отрезка (время работы камеры)
    private class Segment implements Comparable<Segment> {
        long start; // Начало отрезка
        long stop;  // Конец отрезка

        Segment(long start, long stop) {
            // Если start > stop, меняем их местами для корректности
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        // Метод сравнения для сортировки отрезков (по началу, затем по концу)
        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Long.compare(this.start, o.start);
            }
            return Long.compare(this.stop, o.stop);
        }
    }

    // Итеративная быстрая сортировка с трехсторонним разбиением
    private void quickSort(Segment[] array) {
        // Создаем стек для хранения границ подмассивов, которые нужно отсортировать
        Stack<int[]> stack = new Stack<>();
        // Добавляем в стек весь массив (от 0 до length-1)
        stack.push(new int[]{0, array.length - 1});

        // Пока стек не пуст, обрабатываем подмассивы
        while (!stack.isEmpty()) {
            // Извлекаем границы подмассива
            int[] range = stack.pop();
            int left = range[0]; // Левая граница
            int right = range[1]; // Правая граница

            // Если подмассив содержит меньше двух элементов, пропускаем его
            if (left >= right) {
                continue;
            }

            // Выполняем трехстороннее разбиение подмассива
            int[] bounds = partition(array, left, right);
            int lt = bounds[0]; // Граница элементов, меньших пивота
            int gt = bounds[1]; // Граница элементов, больших пивота

            // Добавляем подмассивы в стек для дальнейшей сортировки
            // Чтобы минимизировать глубину стека, сначала добавляем больший подмассив
            if (gt - lt - 1 > right - gt) {
                stack.push(new int[]{lt + 1, gt - 1}); // Элементы, равные пивоту
                stack.push(new int[]{gt, right});       // Элементы, большие пивота
                stack.push(new int[]{left, lt});        // Элементы, меньшие пивота
            } else {
                stack.push(new int[]{left, lt});        // Элементы, меньшие пивота
                stack.push(new int[]{lt + 1, gt - 1});  // Элементы, равные пивоту
                stack.push(new int[]{gt, right});       // Элементы, большие пивота
            }
        }
    }

    // Трехстороннее разбиение массива
    private int[] partition(Segment[] array, int left, int right) {
        // Выбираем пивот (первый элемент подмассива)
        Segment pivot = array[left];
        int lt = left;     // Граница элементов, меньших пивота
        int gt = right;    // Граница элементов, больших пивота
        int i = left;      // Текущий элемент для обработки

        // Проходим по подмассиву, распределяя элементы относительно пивота
        while (i <= gt) {
            // Сравниваем текущий элемент с пивотом
            int cmp = array[i].compareTo(pivot);
            if (cmp < 0) {
                // Если элемент меньше пивота, меняем его с элементом на границе lt
                swap(array, lt++, i++);
            } else if (cmp > 0) {
                // Если элемент больше пивота, меняем его с элементом на границе gt
                swap(array, i, gt--);
            } else {
                // Если элемент равен пивоту, просто переходим к следующему
                i++;
            }
        }
        // Возвращаем границы: lt-1 (последний элемент < пивота), gt+1 (первый элемент > пивота)
        return new int[]{lt - 1, gt + 1};
    }

    // Метод для обмена двух элементов в массиве
    private void swap(Segment[] array, int i, int j) {
        Segment temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Бинарный поиск первого отрезка, у которого start <= point
    private int binarySearchFirst(Segment[] segments, long point) {
        int left = 0; // Левая граница поиска
        int right = segments.length - 1; // Правая граница поиска
        int result = segments.length; // Если ничего не найдено, возвращаем n

        // Пока левая граница не превышает правую
        while (left <= right) {
            // Находим середину, чтобы разделить область поиска
            int mid = left + (right - left) / 2;
            // Если начало отрезка в середине меньше или равно точке
            if (segments[mid].start <= point) {
                result = mid; // Сохраняем индекс как возможный результат
                right = mid - 1; // Ищем более ранний отрезок (слева)
            } else {
                // Если начало отрезка больше точки, ищем в правой половине
                left = mid + 1;
            }
        }
        // Возвращаем индекс первого подходящего отрезка или n, если не найдено
        return result;
    }
}