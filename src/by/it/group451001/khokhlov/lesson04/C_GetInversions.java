package by.it.group451001.khokhlov.lesson04;

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
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        return countInversions(a);
    }

    private int countInversions(int[] array) {
        if (array == null || array.length <= 1) {
            return 0;
        }
        int[] temp = new int[array.length];
        return mergeSortAndCount(array, temp, 0, array.length - 1);
    }

    private int mergeSortAndCount(int[] array, int[] temp, int left, int right) {
        int invCount = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            invCount += mergeSortAndCount(array, temp, left, mid);
            invCount += mergeSortAndCount(array, temp, mid + 1, right);
            invCount += mergeAndCount(array, temp, left, mid, right);
        }
        return invCount;
    }

    private int mergeAndCount(int[] array, int[] temp, int left, int mid, int right) {
        System.arraycopy(array, left, temp, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;
        int invCount = 0;

        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                array[k++] = temp[i++];
            } else {
                array[k++] = temp[j++];
                invCount += (mid - i + 1); // Все оставшиеся в левой части элементы образуют инверсии
            }
        }

        while (i <= mid) {
            array[k++] = temp[i++];
        }

        while (j <= right) {
            array[k++] = temp[j++];
        }

        return invCount;
    }
}