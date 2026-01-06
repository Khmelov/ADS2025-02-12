package by.it.group410901.kliaus.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;
        int count;

        public DSU(int maxStates) {
            parent = new int[maxStates];
            size = new int[maxStates];
            count = 0;
        }

        public int makeSet() {
            int id = count++;
            parent[id] = id;
            size[id] = 1;
            return id;
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                // Union by size
                if (size[rootX] < size[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                }
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    static class HeightGroup {
        int firstId;
        int nextIdx;

        public HeightGroup() {
            this.firstId = -1;
            this.nextIdx = 0;
        }
    }

    static DSU dsu;
    static int[] maxHeights;
    static int[] stateIds;
    static int stateCount;
    static int[] towers;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();

        // Максимальное количество состояний = 2^n - 1
        int maxStates = (1 << n) - 1;
        dsu = new DSU(maxStates);
        maxHeights = new int[maxStates];
        stateIds = new int[maxStates];
        stateCount = 0;

        // Инициализируем три башни
        towers = new int[3];
        towers[0] = n; // A содержит n дисков
        towers[1] = 0; // B пустая
        towers[2] = 0; // C пустая

        // Решаем задачу Ханойских башен
        hanoi(n, 0, 1, 2);

        // Группируем состояния по максимальной высоте эффективно
        // Используем массив для хранения первого элемента каждой группы высоты
        HeightGroup[] heightGroups = new HeightGroup[n + 1];
        for (int i = 0; i <= n; i++) {
            heightGroups[i] = new HeightGroup();
        }

        for (int i = 0; i < stateCount; i++) {
            int height = maxHeights[i];
            if (heightGroups[height].firstId == -1) {
                heightGroups[height].firstId = stateIds[i];
            } else {
                dsu.union(heightGroups[height].firstId, stateIds[i]);
            }
        }

        // Собираем размеры кластеров
        int[] clusterSizes = new int[stateCount];
        int uniqueCount = 0;
        boolean[] visited = new boolean[stateCount];

        for (int i = 0; i < stateCount; i++) {
            int root = dsu.find(stateIds[i]);
            if (!visited[root]) {
                visited[root] = true;
                clusterSizes[uniqueCount++] = dsu.getSize(stateIds[i]);
            }
        }

        // Сортируем размеры кластеров
        for (int i = 0; i < uniqueCount - 1; i++) {
            for (int j = 0; j < uniqueCount - i - 1; j++) {
                if (clusterSizes[j] > clusterSizes[j + 1]) {
                    int temp = clusterSizes[j];
                    clusterSizes[j] = clusterSizes[j + 1];
                    clusterSizes[j + 1] = temp;
                }
            }
        }

        // Выводим результат
        for (int i = 0; i < uniqueCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes[i]);
        }
        System.out.println();
    }

    static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) return;

        hanoi(n - 1, from, aux, to);

        towers[from]--;
        towers[to]++;

        // Сохраняем состояние
        int id = dsu.makeSet();
        int maxHeight = Math.max(towers[0], Math.max(towers[1], towers[2]));
        maxHeights[stateCount] = maxHeight;
        stateIds[stateCount] = id;
        stateCount++;

        hanoi(n - 1, aux, to, from);
    }
}