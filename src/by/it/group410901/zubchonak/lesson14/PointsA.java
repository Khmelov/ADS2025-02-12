package by.it.group410901.zubchonak.lesson14;

import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double D = scanner.nextDouble();
        double D2 = D * D; // используем квадрат расстояния для точного и быстрого сравнения
        int N = scanner.nextInt();
        scanner.nextLine(); // пропускаем остаток строки после чтения N

        List<int[]> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            String[] coords = scanner.nextLine().trim().split("\\s+");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            int z = Integer.parseInt(coords[2]);
            points.add(new int[]{x, y, z});
        }

        DSU dsu = new DSU(N);

        // Проверяем все пары точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                long dx = (long) points.get(i)[0] - points.get(j)[0];
                long dy = (long) points.get(i)[1] - points.get(j)[1];
                long dz = (long) points.get(i)[2] - points.get(j)[2];
                long dist2 = dx * dx + dy * dy + dz * dz;

                if (dist2 < D2) {
                    dsu.union(i, j);
                }
            }
        }

        // Считаем размеры компонент связности
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Получаем размеры и сортируем
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Выводим через пробел, без лишнего пробела в конце
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sizes.get(i));
        }
        System.out.println(); // обязательный перевод строки
    }

    // Вспомогательный класс DSU с эвристикой по рангу и сжатием путей
    static class DSU {
        private final int[] parent;
        private final int[] rank;

        public DSU(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // сжатие путей
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}