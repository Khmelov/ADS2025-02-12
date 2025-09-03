package by.it.group410901.kliaus.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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

        //число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //чтение отрезков
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        //чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //сортировка отрезков по левому краю
        quickSort3Way(segments, 0, segments.length - 1);

        //для каждой точки ищем количество отрезков, включающих её
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            //бинарный поиск первого подходящего отрезка
            int left = 0, right = segments.length - 1;
            int first = -1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (segments[mid].start <= point && point <= segments[mid].stop) {
                    first = mid;
                    right = mid - 1; // ищем ЛЕВУЮ границу
                } else if (segments[mid].start > point) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }

            //если нашли хотя бы один подходящий
            if (first != -1) {
                for (int j = first; j < segments.length; j++) {
                    if (segments[j].start > point) break;
                    if (segments[j].stop >= point) count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    //Хвостовая рекурсия и трёхразбиение
    void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = arr[low];
            int i = low;

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

            // Рекурсивно сортируем меньшую часть и продолжаем с большей
            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}
