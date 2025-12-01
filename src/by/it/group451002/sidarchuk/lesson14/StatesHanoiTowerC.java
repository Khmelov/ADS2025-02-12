package by.it.group451002.sidarchuk.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int[] parent;
    private static int[] size;
    private static int stepCount;
    private static int[][] towerHeights;
    private static int[] maxHeights; // Кэшируем максимальные высоты

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        if (N == 0) {
            return;
        }

        int totalSteps = (1 << N) - 1;

        parent = new int[totalSteps];
        size = new int[totalSteps];
        towerHeights = new int[totalSteps + 1][3];
        maxHeights = new int[totalSteps + 1];

        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Начальное состояние
        towerHeights[0][0] = N;
        towerHeights[0][1] = 0;
        towerHeights[0][2] = 0;
        maxHeights[0] = N;

        stepCount = 0;
        solveHanoi(N, 0, 1, 2);

        // Оптимизированная группировка: создаем группы по максимальной высоте
        int[] groupHeads = new int[N + 1]; // groupHeads[h] = первый элемент с максимальной высотой h
        for (int i = 0; i <= N; i++) {
            groupHeads[i] = -1;
        }

        // Первый проход: находим представителей групп
        for (int i = 0; i < totalSteps; i++) {
            int maxH = maxHeights[i + 1];
            if (groupHeads[maxH] == -1) {
                groupHeads[maxH] = i;
            } else {
                union(groupHeads[maxH], i);
            }
        }

        // Сбор и сортировка результатов
        int[] result = new int[totalSteps];
        int count = 0;
        boolean[] visited = new boolean[totalSteps];

        for (int i = 0; i < totalSteps; i++) {
            int root = find(i);
            if (!visited[root]) {
                visited[root] = true;
                result[count++] = size[root];
            }
        }

        // Сортировка пузырьком
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (result[j] > result[j + 1]) {
                    int temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }

        // Вывод
        for (int i = 0; i < count; i++) {
            System.out.print(result[i]);
            if (i < count - 1) {
                System.out.print(" ");
            }
        }
    }

    private static void solveHanoi(int n, int from, int to, int aux) {
        if (n == 1) {
            makeMove(from, to);
            return;
        }

        solveHanoi(n - 1, from, aux, to);
        makeMove(from, to);
        solveHanoi(n - 1, aux, to, from);
    }

    private static void makeMove(int from, int to) {
        // Копируем состояние
        System.arraycopy(towerHeights[stepCount], 0, towerHeights[stepCount + 1], 0, 3);

        // Выполняем ход
        towerHeights[stepCount + 1][from]--;
        towerHeights[stepCount + 1][to]++;

        // Вычисляем и кэшируем максимальную высоту
        int maxH = Math.max(towerHeights[stepCount + 1][0],
                Math.max(towerHeights[stepCount + 1][1], towerHeights[stepCount + 1][2]));
        maxHeights[stepCount + 1] = maxH;

        stepCount++;
    }

    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    private static void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX != rootY) {
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }
}