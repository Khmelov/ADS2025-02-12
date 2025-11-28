package by.it.group410902.bobrovskaya.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;
                size[i] = 0;
            }
        }

        int find(int v) {
            if (parent[v] != v)
                parent[v] = find(parent[v]);
            return parent[v];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;
            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }
            parent[b] = a;
            size[a] += size[b];
        }

        void addToSet(int x) {
            size[x] += 1;
        }
    }

    static DSU dsu;
    static int maxN;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        maxN = N;
        dsu = new DSU(N);

        hanoi(N, 'A', 'B', 'C', new int[]{N, 0, 0});

        // Собираем размеры поддеревьев
        int[] clusterSize = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            int root = dsu.find(i);
            clusterSize[root] = dsu.size[root];
        }

        // Выводим только непустые размеры в порядке возрастания
        int[] result = new int[N + 1];
        int count = 0;
        for (int i = 1; i <= N; i++) {
            if (clusterSize[i] > 0) {
                result[count++] = clusterSize[i];
            }
        }

        // Сортировка вставками, т.к. коллекции нельзя
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (result[i] > result[j]) {
                    int t = result[i];
                    result[i] = result[j];
                    result[j] = t;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result[i]);
        }
        System.out.println();
    }

    // Рекурсивное решение задачи Ханойской башни
    static void hanoi(int n, char from, char to, char aux, int[] heights) {
        if (n == 0) return;

        // Переместить n-1 дисков с from → aux
        hanoi(n - 1, from, aux, to, heights);

        // Переместить диск n с from → to
        moveDisk(n, from, to, heights);

        // Переместить n-1 дисков с aux → to
        hanoi(n - 1, aux, to, from, heights);
    }

    static void moveDisk(int disk, char from, char to, int[] heights) {
        // уменьшить высоту на стержне "from"
        if (from == 'A') heights[0]--;
        if (from == 'B') heights[1]--;
        if (from == 'C') heights[2]--;

        // увеличить на "to"
        if (to == 'A') heights[0]++;
        if (to == 'B') heights[1]++;
        if (to == 'C') heights[2]++;

        // вычисляем наибольшую высоту пирамиды
        int maxHeight = heights[0];
        if (heights[1] > maxHeight) maxHeight = heights[1];
        if (heights[2] > maxHeight) maxHeight = heights[2];

        // добавляем шаг в DSU-группу
        dsu.addToSet(maxHeight);
    }
}
