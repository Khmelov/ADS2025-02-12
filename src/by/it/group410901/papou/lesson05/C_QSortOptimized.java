package by.it.group410901.papou.lesson05;

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

        // Read input
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Read segments
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }

        // Read points
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Sort segments using optimized QuickSort
        quickSort(segments, 0, n - 1);

        // Process each point
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsForPoint(segments, points[i]);
        }

        return result;
    }

    // Optimized QuickSort with 3-way partitioning and tail recursion elimination
    private void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            // Use insertion sort for small arrays (threshold = 10)
            if (high - low < 10) {
                insertionSort(arr, low, high);
                break;
            }

            // 3-way partitioning
            int[] pivotIndices = partition(arr, low, high);
            int lt = pivotIndices[0];
            int gt = pivotIndices[1];

            // Recurse on smaller partition, iterate on larger to eliminate tail recursion
            if (lt - low < high - gt) {
                quickSort(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    // 3-way partitioning
    private int[] partition(Segment[] arr, int low, int high) {
        Segment pivot = arr[low];
        int i = low;
        int lt = low;
        int gt = high;

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
        return new int[]{lt, gt};
    }

    // Insertion sort for small subarrays
    private void insertionSort(Segment[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            Segment key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j].compareTo(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Swap elements in array
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Count segments covering a point using binary search
    private int countSegmentsForPoint(Segment[] segments, int point) {
        // Binary search to find the first segment that could cover the point
        int left = 0;
        int right = segments.length - 1;
        int firstPossible = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                firstPossible = mid;
                left = mid + 1; // Look for later segments
            } else {
                right = mid - 1;
            }
        }

        // Count segments starting from firstPossible
        int count = 0;
        if (firstPossible != -1) {
            for (int i = firstPossible; i < segments.length; i++) {
                if (segments[i].start > point) {
                    break; // No further segments can cover
                }
                if (point <= segments[i].stop) {
                    count++; // Point is within segment
                }
            }
        }

        return count;
    }

    // Segment class
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Ensure start <= stop
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Sort by start, then by stop
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}