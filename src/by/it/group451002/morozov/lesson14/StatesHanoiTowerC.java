package by.it.group451002.morozov.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
	
    // Класс для представления состояния Ханойской башни
    static class HanoiState {
        int[] heights = new int[3];

        HanoiState(int a, int b, int c) {
            heights[0] = a;
            heights[1] = b;
            heights[2] = c;
        }

        int getMaxHeight() {
            return Math.max(heights[0], Math.max(heights[1], heights[2]));
        }
    }

    static HanoiState[] states;
    static int stateCount = 0;  // Счетчик состояний

    // Рекурсивное решение Ханойской башни с записью состояний
    public static void solveHanoi(int n, int from, int to, int aux, int[] heights) {
        if (n == 0) return;

        // Перемещение n-1 диска на вспомогательный стержень
        solveHanoi(n - 1, from, aux, to, heights);

        // Перемещение самого большого диска на целевой стержень
        heights[from]--;
        heights[to]++;
        states[stateCount++] = new HanoiState(heights[0], heights[1], heights[2]);

        // Перемещение n-1 диска с вспомогательного на целевой стержень
        solveHanoi(n - 1, aux, to, from, heights);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        // Формула для общего числа шагов
        int totalSteps = (1 << N) - 1;
        states = new HanoiState[totalSteps];
        stateCount = 0;

        // Начальное состояние: все диски на первом стержне
        int[] heights = {N, 0, 0};
        solveHanoi(N, 0, 1, 2, heights);

        // Создание системы непересекающихся множеств для состояний
        DSU dsu = new DSU(stateCount);

        // Группируем состояния по максимальной высоте башни
        int maxPossibleHeight = N;
        int[] maxHeightToFirstIndex = new int[maxPossibleHeight + 1];
        boolean[] hasMaxHeight = new boolean[maxPossibleHeight + 1];

        for (int i = 0; i <= maxPossibleHeight; i++) {
            maxHeightToFirstIndex[i] = -1;
        }

        for (int i = 0; i < stateCount; i++) {
            int maxHeight = states[i].getMaxHeight();
            if (!hasMaxHeight[maxHeight]) {
                maxHeightToFirstIndex[maxHeight] = i;
                hasMaxHeight[maxHeight] = true;
            } else {
                dsu.union(maxHeightToFirstIndex[maxHeight], i);
            }
        }

        // Подсчитываем размеры групп
        int[] groupSizes = new int[stateCount];
        int groupCount = 0;
        boolean[] visited = new boolean[stateCount];

        for (int i = 0; i < stateCount; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                groupSizes[groupCount++] = dsu.getSize(root);
            }
        }

        for (int i = 0; i < groupCount - 1; i++) {
            for (int j = 0; j < groupCount - i - 1; j++) {
                if (groupSizes[j] > groupSizes[j + 1]) {
                    int temp = groupSizes[j];
                    groupSizes[j] = groupSizes[j + 1];
                    groupSizes[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < groupCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(groupSizes[i]);
        }
        System.out.println();
    }
}