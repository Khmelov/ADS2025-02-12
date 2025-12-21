package by.it.group451003.rashchenya.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        result = countInversions(a);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int countInversions(int[] array) {
        return mergeSortAndCount(array, 0, array.length - 1);
    }

    private int mergeSortAndCount(int[] array, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Count inversions in left subarray
            count += mergeSortAndCount(array, left, mid);

            // Count inversions in right subarray
            count += mergeSortAndCount(array, mid + 1, right);

            // Count inversions during merge
            count += mergeAndCount(array, left, mid, right);
        }
        return count;
    }

    private int mergeAndCount(int[] array, int left, int mid, int right) {
        // Create temporary arrays
        int[] leftArray = new int[mid - left + 1];
        int[] rightArray = new int[right - mid];

        // Copy data to temporary arrays
        System.arraycopy(array, left, leftArray, 0, leftArray.length);
        System.arraycopy(array, mid + 1, rightArray, 0, rightArray.length);

        int i = 0, j = 0, k = left;
        int inversionCount = 0;

        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
                // All remaining elements in leftArray will form inversions
                inversionCount += (mid + 1) - (left + i);
            }
        }

        // Copy remaining elements
        while (i < leftArray.length) {
            array[k++] = leftArray[i++];
        }
        while (j < rightArray.length) {
            array[k++] = rightArray[j++];
        }

        return inversionCount;
    }
}