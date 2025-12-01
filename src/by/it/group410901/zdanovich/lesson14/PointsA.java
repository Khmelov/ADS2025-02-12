package by.it.group410901.zdanovich.lesson14;

import java.util.*;

public class PointsA {

    // Класс для представления точки в 3D пространстве
    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        // Вычисление евклидова расстояния между точками
        double distanceTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            double dz = this.z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    // DSU с эвристикой по рангу и сжатием путей
    static class DSU {
        private int[] parent;
        private int[] rank;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;  // каждый элемент - корень своего дерева
                size[i] = 1;    // начальный размер компоненты
                rank[i] = 0;    // начальный ранг
            }
        }

        // Поиск корня со сжатием путей
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // сжатие пути
            }
            return parent[x];
        }

        // Объединение с эвристикой по рангу
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            // Объединяем меньшее дерево с большим
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
                rank[rootX]++;
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение допустимого расстояния и количества точек
        double maxDistance = scanner.nextDouble();
        int n = scanner.nextInt();

        // Массив для хранения точек
        Point[] points = new Point[n];

        // Чтение координат точек
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point(x, y, z);
        }

        // Инициализация DSU
        DSU dsu = new DSU(n);

        // Объединение точек, находящихся на допустимом расстоянии
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double distance = points[i].distanceTo(points[j]);
                // Используем строгое неравенство согласно условию [0, D)
                if (distance < maxDistance) {
                    dsu.union(i, j);
                }
            }
        }

        // Сбор размеров кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.getSize(root));
        }

        // Извлечение и сортировка размеров кластеров по убыванию
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Вывод результатов
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}

