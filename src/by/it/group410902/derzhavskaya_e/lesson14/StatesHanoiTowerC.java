package by.it.group410902.derzhavskaya_e.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        if (N <= 0) return;

        int totalSteps = (1 << N) - 1;
        int[] parent = new int[totalSteps];
        int[] size = new int[totalSteps];
        int[] maxHeights = new int[totalSteps];

        // Инициализация DSU
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Генерируем последовательность перемещений
        int[][] moves = new int[totalSteps][2];
        generateMoves(N, 'A', 'B', 'C', moves, new int[]{0});

        // Вычисляем состояния после каждого хода
        int[] stateA = new int[N];
        int[] stateB = new int[N];
        int[] stateC = new int[N];

        // Начальное состояние: все диски на A
        for (int i = 0; i < N; i++) {
            stateA[i] = N - i;
        }
        int heightA = N, heightB = 0, heightC = 0;

        // Обрабатываем каждый ход
        for (int step = 0; step < totalSteps; step++) {
            int from = moves[step][0];
            int to = moves[step][1];

            // Находим диск для перемещения (верхний диск на from)
            int disk = 0;
            if (from == 'A') disk = stateA[heightA - 1];
            else if (from == 'B') disk = stateB[heightB - 1];
            else disk = stateC[heightC - 1];

            // Убираем диск с from
            if (from == 'A') heightA--;
            else if (from == 'B') heightB--;
            else heightC--;

            // Кладем диск на to
            if (to == 'A') stateA[heightA++] = disk;
            else if (to == 'B') stateB[heightB++] = disk;
            else stateC[heightC++] = disk;

            // Сохраняем максимальную высоту
            maxHeights[step] = Math.max(heightA, Math.max(heightB, heightC));
        }

        // Оптимизированное объединение: используем массив для хранения представителей каждой высоты
        int[] representative = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            representative[i] = -1;
        }

        for (int i = 0; i < totalSteps; i++) {
            int maxH = maxHeights[i];
            if (representative[maxH] == -1) {
                // Первое состояние с этой высотой
                representative[maxH] = i;
            } else {
                // Объединяем с представителем этой высоты
                union(representative[maxH], i, parent, size);
            }
        }

        // Собираем и сортируем размеры
        int count = 0;
        int[] result = new int[totalSteps];
        for (int i = 0; i <= N; i++) {
            if (representative[i] != -1) {
                int root = find(representative[i], parent);
                if (root == representative[i]) { // Убеждаемся, что это корень
                    result[count++] = size[root];
                }
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
            if (i < count - 1) System.out.print(" ");
        }
    }

    private static void generateMoves(int n, char from, char to, char aux, int[][] moves, int[] index) {
        if (n == 1) {
            moves[index[0]][0] = from;
            moves[index[0]][1] = to;
            index[0]++;
            return;
        }
        generateMoves(n - 1, from, aux, to, moves, index);
        moves[index[0]][0] = from;
        moves[index[0]][1] = to;
        index[0]++;
        generateMoves(n - 1, aux, to, from, moves, index);
    }

    private static int find(int x, int[] parent) {
        if (parent[x] != x) {
            parent[x] = find(parent[x], parent);
        }
        return parent[x];
    }

    private static void union(int x, int y, int[] parent, int[] size) {
        int rootX = find(x, parent);
        int rootY = find(y, parent);
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