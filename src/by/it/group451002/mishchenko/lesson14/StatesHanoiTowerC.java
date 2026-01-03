package by.it.group451002.mishchenko.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    // Система непересекающихся множеств
    static class DSU {
        private int[] parent; // Родительские элементы
        private int[] size;   // Размеры множеств

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];

            // Инициализация каждого элемента
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // Найти корень с эвристикой сжатия пути
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // Объединение множеств по размеру
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            // Объединение по размеру для балансировки
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        // Получить размер множества
        public int getSize(int x) {
            return size[find(x)];
        }
    }

    // Класс для представления состояния Ханойской башни
    static class HanoiState {
        int[] heights = new int[3]; // Высоты башен (количество дисков)

        HanoiState(int a, int b, int c) {
            heights[0] = a;
            heights[1] = b;
            heights[2] = c;
        }

        // Получить максимальную высоту среди всех башен
        int getMaxHeight() {
            return Math.max(heights[0], Math.max(heights[1], heights[2]));
        }
    }

    static HanoiState[] states; // Массив всех состояний
    static int stateCount = 0;  // Счетчик состояний

    // Рекурсивное решение Ханойской башни с записью состояний
    public static void solveHanoi(int n, int from, int to, int aux, int[] heights) {
        if (n == 0) return;

        // Перемещение n-1 диска на вспомогательный стержень
        solveHanoi(n - 1, from, aux, to, heights);

        // Перемещение самого большого диска на целевой стержень
        heights[from]--;
        heights[to]++;
        // Сохранение текущего состояния
        states[stateCount++] = new HanoiState(heights[0], heights[1], heights[2]);

        // Перемещение n-1 диска с вспомогательного на целевой стержень
        solveHanoi(n - 1, aux, to, from, heights);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt(); // Количество дисков
        scanner.close();

        int totalSteps = (1 << N) - 1; // Формула для общего числа шагов
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

        // Инициализация массива индексов
        for (int i = 0; i <= maxPossibleHeight; i++) {
            maxHeightToFirstIndex[i] = -1;
        }

        // Проход: Для каждой максимальной высоты запоминаем первое состояние с такой высотой, а все остальные объединяем с ним
        for (int i = 0; i < stateCount; i++) {
            int maxHeight = states[i].getMaxHeight();
            if (!hasMaxHeight[maxHeight]) {
                maxHeightToFirstIndex[maxHeight] = i;
                hasMaxHeight[maxHeight] = true;
            } else {
                // Объединяем с первым состоянием с такой же максимальной высотой
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

        // Сортировка пузырьком по возрастанию
        for (int i = 0; i < groupCount - 1; i++) {
            for (int j = 0; j < groupCount - i - 1; j++) {
                if (groupSizes[j] > groupSizes[j + 1]) {
                    int temp = groupSizes[j];
                    groupSizes[j] = groupSizes[j + 1];
                    groupSizes[j + 1] = temp;
                }
            }
        }

        // Вывод результатов
        for (int i = 0; i < groupCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(groupSizes[i]);
        }
        System.out.println();
    }
}