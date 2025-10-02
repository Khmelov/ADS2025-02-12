package by.it.group410901.evtuhovskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] points = new int[n];

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        //сортировка подсчётом
        int maxNumber = 10; //по условию числа не превышают 10
        int[] count = new int[maxNumber + 1]; //массив подсчёта

        for (int num : points) {
            count[num]++;
        }

        //восстановдение отсортированного массива
        int index = 0;
        for (int num = 0; num <= maxNumber; num++) {
            while (count[num] > 0) {
                points[index++] = num;
                count[num]--;
            }
        }

        return points;
    }
}