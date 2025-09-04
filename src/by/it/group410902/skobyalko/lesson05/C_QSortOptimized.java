package by.it.group410902.skobyalko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        /// //////////////

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int[] starts = new int[n];
        int[] ends = new int[n];
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            if (a <= b) {
                starts[i] = a;
                ends[i] = b;
            } else {
                starts[i] = b;
                ends[i] = a;
            }
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем начала и концы отрезков
        Arrays.sort(starts);
        Arrays.sort(ends);

        for (int i = 0; i < m; i++) {
            int point = points[i];

            int startsCount = upperBound(starts, point); // сколько отрезков начинается <= point
            int endsCount = lowerBound(ends, point);     // сколько отрезков заканчивается < point

            result[i] = startsCount - endsCount;
        }

        return result;
    }

    // Аналог std::upper_bound: индекс первого элемента > key
    private int upperBound(int[] arr, int key) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] <= key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // Аналог std::lower_bound: индекс первого элемента >= key
    private int lowerBound(int[] arr, int key) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] < key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}

