package by.it.group410902.kozincev.lesson14;

import java.util.*;

public class PointsA {

    private static class DSU {
        private final int[] parent;
        private final int[] rank; // или size
        private int count; // Количество множеств

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            count = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Изначально каждый элемент - корень своего множества
                rank[i] = 1;   // Изначально ранг (или размер) каждого множества равен 1
            }
        }

        // Операция Find с сжатием путей
        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]); // Сжатие пути
            }
            return parent[i];
        }

        // Операция Union по рангу (или размеру)
        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                // Объединение по рангу (или размеру)
                if (rank[rootI] < rank[rootJ]) {
                    parent[rootI] = rootJ;
                } else if (rank[rootI] > rank[rootJ]) {
                    parent[rootJ] = rootI;
                } else {
                    parent[rootJ] = rootI;
                    rank[rootI]++; // Увеличение ранга, если ранги одинаковы
                }
                count--; // Уменьшаем количество множеств
                return true;
            }
            return false;
        }

        // Возвращает количество непересекающихся множеств
        public int getCount() {
            return count;
        }
    }


    // Класс для представления точки в 3D-пространстве
    private static class Point {
        final int x, y, z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }


        public double distanceTo(Point other) {
            long dx = (long) this.x - other.x;
            long dy = (long) this.y - other.y;
            long dz = (long) this.z - other.z;
            return Math.hypot(Math.hypot(dx, dy), dz);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Чтение входных данных
        // D - допустимое расстояние
        double d = scanner.nextDouble();
        // N - количество точек
        int n = scanner.nextInt();

        List<Point> points = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points.add(new Point(x, y, z));
        }
        scanner.close();

        // 2. Инициализация DSU
        DSU dsu = new DSU(n);

        // 3. Объединение близких точек
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);

                // Точки объединяются, если расстояние между ними < D
                if (p1.distanceTo(p2) < d) {
                    dsu.union(i, j); // Объединение по их индексам
                }
            }
        }

        // 4. Сбор и вывод размеров кластеров
        // Создаем карту для подсчета размера каждого кластера
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i); // Находим корень кластера
            // Увеличиваем счетчик для этого корня (кластера)
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // 5. Вывод: размеры кластеров в порядке убывания
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());

        // Сортировка по убыванию
        sizes.sort(Comparator.reverseOrder());

        // Вывод через пробел
        StringJoiner sj = new StringJoiner(" ");
        for (int size : sizes) {
            sj.add(String.valueOf(size));
        }
        System.out.println(sj.toString());
    }
}