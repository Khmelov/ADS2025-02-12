package by.it.group451004.zarivniak.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int[] result = instance.getNotUpSeqSize(stream);

        // Вывод результата
        System.out.println(result.length);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] m = new int[n];

        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] tail = new int[n];
        int[] prev = new int[n];
        int[] positions = new int[n];
        int size = 0;
        for (int i = 0; i < n; i++) {
            prev[i] = -1;
        }

        for (int i = 0; i < n; i++) {
            int left = 0, right = size;
            while (left < right) {
                int mid = (left + right) / 2;
                if (tail[mid] < m[i]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            tail[left] = m[i];
            positions[left] = i;

            if (left > 0) {
                prev[i] = positions[left - 1];
            }

            if (left == size) {
                size++;
            }
        }

        ArrayList<Integer> indices = new ArrayList<>();
        int currentIndex = positions[size - 1];
        while (currentIndex != -1) {
            indices.add(currentIndex + 1);
            currentIndex = prev[currentIndex];
        }

        int[] result = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            result[i] = indices.get(indices.size() - 1 - i);
        }

        return result;
    }
}