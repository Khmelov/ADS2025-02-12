package by.it.group410902.dziatko.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    static int topA, topB, topC;

    static int[] maxHeights;
    static int stepCounter;

    static void move(int n, char from, char to, char aux) {
        if (n == 0) return;
        move(n - 1, from, aux, to);
        transfer(from, to);
        move(n - 1, aux, to, from);
    }

    static void transfer(char from, char to) {
        if (from == 'A') topA--;
        if (from == 'B') topB--;
        if (from == 'C') topC--;

        if (to == 'A') topA++;
        if (to == 'B') topB++;
        if (to == 'C') topC++;

        int max = Math.max(topA, Math.max(topB, topC));
        maxHeights[stepCounter++] = max;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        topA = N;
        topB = 0;
        topC = 0;

        int totalSteps = (1 << N) - 1; // 2^N - 1
        maxHeights = new int[totalSteps];
        stepCounter = 0;

        move(N, 'A', 'B', 'C');

        int[] counts = new int[N + 1];
        for (int i = 0; i < totalSteps; i++) {
            counts[maxHeights[i]]++;
        }

        int clusterCount = 0;
        int[] clusterSizes = new int[N + 1];
        for (int h = 1; h <= N; h++) {
            if (counts[h] > 0) {
                clusterSizes[clusterCount++] = counts[h];
            }
        }

        for (int i = 0; i < clusterCount - 1; i++) {
            for (int j = i + 1; j < clusterCount; j++) {
                if (clusterSizes[i] > clusterSizes[j]) {
                    int tmp = clusterSizes[i];
                    clusterSizes[i] = clusterSizes[j];
                    clusterSizes[j] = tmp;
                }
            }
        }

        for (int i = 0; i < clusterCount; i++) {
            System.out.print(clusterSizes[i] + " ");
        }
    }
}