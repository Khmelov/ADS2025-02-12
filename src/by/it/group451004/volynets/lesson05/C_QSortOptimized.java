package by.it.group451004.volynets.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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
        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Бинарный поиск первого отрезка, который может содержать точку
            int left = 0;
            int right = n - 1;
            int firstIndex = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    if (point <= segments[mid].stop) {
                        firstIndex = mid;
                        break;
                    }
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            if (firstIndex == -1) {
                result[i] = 0;
                continue;
            }

            int count = 0;
            // Проверяем отрезки слева от найденного
            int j = firstIndex;
            while (j >= 0 && segments[j].start <= point) {
                if (point <= segments[j].stop) {
                    count++;
                }
                j--;
            }

            // Проверяем отрезки справа от найденного
            j = firstIndex + 1;
            while (j < n && segments[j].start <= point) {
                if (point <= segments[j].stop) {
                    count++;
                }
                j++;
            }

            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort(Segment[] segments, int left, int right) {
        while (left < right) {
            int[] pivots = partition(segments, left, right);
            if (pivots[0] - left < right - pivots[1]) {
                quickSort(segments, left, pivots[0] - 1);
                left = pivots[1] + 1;
            } else {
                quickSort(segments, pivots[1] + 1, right);
                right = pivots[0] - 1;
            }
        }
    }

    private int[] partition(Segment[] segments, int left, int right) {
        Segment pivot = segments[left + (right - left) / 2];
        int i = left;
        int j = right;
        int k = left;

        while (k <= j) {
            int cmp = segments[k].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, i, k);
                i++;
                k++;
            } else if (cmp > 0) {
                swap(segments, k, j);
                j--;
            } else {
                k++;
            }
        }

        return new int[]{i, j};
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}