package by.it.group410902.plekhova.lesson14;

import java.util.*;

public class PointsA {

    // Структура данных Система непересекающихся множеств
    static class DSU {
        int[] parent; // родитель для каждого элемента
        int[] size;   // размер компоненты (только для корня)

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // изначально каждый — свой собственный корень
                size[i] = 1;
            }
        }

        // Нахождение корня множества с сжатием пути
        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]); // рекурсивно поднимаемся и сжимаем путь
            return parent[x];
        }

        // Объединение двух множеств
        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return; // уже в одном множестве

            // объединяем по размеру: маленькое — к большому
            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }
            parent[b] = a;
            size[a] += size[b];
        }

        // Получение размеров всех кластеров
        List<Integer> getClusterSizes(int n) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < n; i++) {
                int root = find(i); // найти корень точки
                map.put(root, map.getOrDefault(root, 0) + 1);
            }
            return new ArrayList<>(map.values());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int D = sc.nextInt(); // максимальная дистанция для объединения
        int N = sc.nextInt(); // число точек

        int[][] pts = new int[N][3];
        for (int i = 0; i < N; i++) {
            pts[i][0] = sc.nextInt(); // x
            pts[i][1] = sc.nextInt(); // y
            pts[i][2] = sc.nextInt(); // z
        }

        DSU dsu = new DSU(N);

        // Перебор всех пар точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {

                // Вычисляем 3D расстояние между точками
                double dx = pts[i][0] - pts[j][0];
                double dy = pts[i][1] - pts[j][1];
                double dz = pts[i][2] - pts[j][2];
                double dist = Math.hypot(Math.hypot(dx, dy), dz);

                // Если расстояние меньше D — точки в одном кластере
                if (dist < D) {
                    dsu.union(i, j);
                }
            }
        }

        // Получаем размеры кластеров и сортируем по убыванию
        List<Integer> result = dsu.getClusterSizes(N);
        result.sort((a, b) -> b - a);

        // Вывод
        for (int x : result) {
            System.out.print(x + " ");
        }
    }
}
