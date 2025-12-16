package by.it.group451002.Osadchy.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int[] counts;
    private static int[] heights;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        counts = new int[N + 1];
        heights = new int[3];
        heights[0] = N;
        heights[1] = 0;
        heights[2] = 0;

        hanoi(N, 0, 1, 2);

        int nonZeroCount = 0;
        for (int i = 1; i <= N; i++) {
            if (counts[i] > 0) {
                nonZeroCount++;
            }
        }

        int[] result = new int[nonZeroCount];
        int index = 0;
        for (int i = 1; i <= N; i++) {
            if (counts[i] > 0) {
                result[index++] = counts[i];
            }
        }

        Arrays.sort(result);

        for (int i = 0; i < result.length; i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(result[i]);
        }
    }

    private static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }
        hanoi(n - 1, from, aux, to);
        heights[from]--;
        heights[to]++;
        int maxHeight = Math.max(heights[0], Math.max(heights[1], heights[2]));
        counts[maxHeight]++;
        hanoi(n - 1, aux, to, from);
    }
}
