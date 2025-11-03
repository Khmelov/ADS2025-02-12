package by.it.group451001.serganovskij.lesson14;

import java.util.*;

/**
 * Кластеризация точек в 3D с использованием DSU (Union-Find).
 * Точки объединяются, если расстояние между ними строго < D.
 * В конце печатаются размеры кластеров по убыванию.
 */
public class PointsA {

    /** Структура данных DSU (Union-Find) c эвристикой по размеру поддерева */
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a != b) {
                if (size[a] < size[b]) {
                    int t = a;
                    a = b;
                    b = t;
                }
                parent[b] = a;
                size[a] += size[b];
            }
        }
    }

    /** Класс для 3D-точки */
    static class Point {
        int x, y, z;
        Point(int x, int y, int z) {
            this.x = x; this.y = y; this.z = z;
        }

        /** Евклидово расстояние до другой точки */
        double distanceTo(Point p) {
            return Math.sqrt(
                    (x - p.x) * (x - p.x) +
                            (y - p.y) * (y - p.y) +
                            (z - p.z) * (z - p.z)
            );
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();  // допустимое расстояние
        int N = sc.nextInt();        // количество точек

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            points.add(new Point(sc.nextInt(), sc.nextInt(), sc.nextInt()));
        }

        DSU dsu = new DSU(N);

        // Перебор всех пар точек (O(N^2), достаточно для N ≤ 2000 в олимпиадах)
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (points.get(i).distanceTo(points.get(j)) < D) {
                    dsu.union(i, j);
                }
            }
        }

        // Подсчёт размера каждого кластера
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Список размеров, сортировка по убыванию
        List<Integer> result = new ArrayList<>(clusterSizes.values());
        result.sort(Comparator.reverseOrder());

        // Формируем вывод без лишнего пробела
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            sb.append(result.get(i));
            if (i + 1 < result.size()) sb.append(" ");
        }
        System.out.println(sb);
    }
}
