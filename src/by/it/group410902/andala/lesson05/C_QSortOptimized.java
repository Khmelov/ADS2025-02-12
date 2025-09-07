package by.it.group410902.andala.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;

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
        Scanner scanner = new Scanner(stream);

        // Читаем количество отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // Читаем количество точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m]; // сюда запишем ответы

        // Считываем сами отрезки (каждый с началом и концом)
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        // Считываем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по их началу с помощью оптимизированной быстрой сортировки с 3-разбиением
        quickSort3WayIterative(segments);

        // Для каждой точки ищем, сколько отрезков её покрывают
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int left = 0;
            int right = segments.length - 1;
            int index = -1;

            // Ищем бинарным поиском первый отрезок, который содержит точку
            while (left <= right) {
                int mid = (left + right) / 2;
                if (segments[mid].start <= point && segments[mid].stop >= point) {
                    // Нашли подходящий отрезок — пытаемся найти самый левый такой
                    index = mid;
                    right = mid - 1;
                } else if (segments[mid].start > point) {
                    // Если отрезок начинается позже точки — ищем левее
                    right = mid - 1;
                } else {
                    // Иначе ищем правее
                    left = mid + 1;
                }
            }

            // Если такой отрезок найден — считаем, сколько подряд отрезков содержат точку
            if (index != -1) {
                int count = 0;
                for (int j = index; j < segments.length; j++) {
                    // Проверяем покрывает ли текущий отрезок точку
                    if (segments[j].start <= point && segments[j].stop >= point) {
                        count++;
                    } else if (segments[j].start > point) {
                        // Если отрезок начинается правее точки, можно остановиться
                        break;
                    }
                }
                result[i] = count;
            }
        }
        return result; // Возвращаем массив с результатами
    }

    // Итеративная быстрая сортировка с 3-разбиением (Dutch National Flag)
    private void quickSort3WayIterative(Segment[] arr) {
        // Вспомогательный класс для хранения границ сортируемого участка
        class StackFrame {
            int low, high;
            StackFrame(int low, int high) {
                this.low = low;
                this.high = high;
            }
        }

        Stack<StackFrame> stack = new Stack<>();
        // Сначала кладём в стек весь массив
        stack.push(new StackFrame(0, arr.length - 1));

        while (!stack.isEmpty()) {
            StackFrame frame = stack.pop();
            int low = frame.low;
            int high = frame.high;

            if (low >= high) continue; // Если участок пустой или один элемент — пропускаем

            Segment pivot = arr[low]; // Опорный элемент — первый в участке
            int lt = low, gt = high, i = low + 1;

            // Разделяем массив на три части:
            // элементы меньше pivot, равные pivot, больше pivot
            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(arr, lt++, i++);
                } else if (cmp > 0) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            // Помещаем получившиеся части обратно в стек для сортировки
            stack.push(new StackFrame(low, lt - 1));
            stack.push(new StackFrame(gt + 1, high));
        }
    }

    // Вспомогательный метод для обмена элементов массива
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Класс для хранения отрезка с возможностью сравнения по началу и при равенстве по концу
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
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            return Integer.compare(this.stop, other.stop);
        }
    }
}
