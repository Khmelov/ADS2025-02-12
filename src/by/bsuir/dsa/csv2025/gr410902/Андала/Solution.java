package by.bsuir.dsa.csv2025.gr410902.Андала;

import java.util.*;

public class Solution {

    public static List<Integer> possibleK(int[] A, int[] B) {
        int n = A.length;
        int[] current = Arrays.copyOf(A, n);
        int[] minPos = new int[n];
        int minVal = Integer.MAX_VALUE;

        // Вычисляем позиции минимальных элементов с конца
        for (int i = n - 1; i >= 0; i--) {
            if (A[i] <= minVal) {
                minVal = A[i];
                minPos[i] = i;
            } else {
                minPos[i] = (i + 1 < n) ? minPos[i + 1] : i;
            }
        }

        List<Integer> result = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < n; i++) {
            int j = minPos[i];
            if (i != j) {
                // выполняем обмен
                int temp = current[i];
                current[i] = current[j];
                current[j] = temp;
                k++;
                if (Arrays.equals(current, B)) {
                    result.add(k);
                }
            } else {
                if (Arrays.equals(current, B)) {
                    result.add(k);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Чтение массива A
        String[] lineA = sc.nextLine().trim().split("\\s+");
        int n = lineA.length;
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = Integer.parseInt(lineA[i]);
        }

        // Чтение массива B
        String[] lineB = sc.nextLine().trim().split("\\s+");
        int[] B = new int[n];
        for (int i = 0; i < n; i++) {
            B[i] = Integer.parseInt(lineB[i]);
        }

        List<Integer> ks = possibleK(A, B);

        // Вывод результата
        if (ks.isEmpty()) {
            System.out.println();
        } else {
            for (int i = 0; i < ks.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(ks.get(i));
            }
            System.out.println();
        }

        sc.close();
    }
}
