package by.it.group410902.sinyutin.lesson05;
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

        for (int i = 0; i < n; i++)
            points[i] = scanner.nextInt();

        int maxVal = 10;
        int minVal = 1;

        int[] count = new int[maxVal - minVal + 1];
        for (int i = 0; i < n; i++)
            count[points[i] - minVal]++;

        int[] sortedPoints = new int[n];
        int currentSortedIndex = 0;

        for (int num = minVal; num <= maxVal; num++) {

            while (count[num - minVal]-- > 0)
                sortedPoints[currentSortedIndex++] = num;
        }

        return sortedPoints;
    }

}