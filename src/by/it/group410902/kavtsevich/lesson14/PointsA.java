package by.it.group410902.kavtsevich.lesson14;

import java.util.*;

public class PointsA {

    // Класс для реализации системы непересекающихся множеств
    static class DSU {
        int[] parent;  // массив родительских вершин
        int[] size;    // массив размеров компонент

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;  // изначально каждый элемент - корень сам себе
                size[i] = 1;    // изначально размер каждой компоненты = 1
            }
        }

        // Метод поиска корня элемента (с эвристикой сжатия пути)
        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]); // рекурсивно находим корень и сокращаем путь
            return parent[x];
        }

        // Объединение двух множеств (с эвристикой по размеру)
        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) return; // уже в одном множестве

            // Присоединяем меньшее дерево к большему (эвристика по размеру)
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            } else {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }

        // Получение размера компоненты, содержащей элемент x
        int getSize(int x) {
            return size[find(x)];
        }
    }

    // Класс для представления точки в 3D-пространстве
    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x; this.y = y; this.z = z;
        }

        // Вычисление евклидова расстояния до другой точки
        double distanceTo(Point other) {
            int dx = x - other.x;
            int dy = y - other.y;
            int dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();  // пороговое расстояние
        int N = sc.nextInt();        // количество точек
        Point[] points = new Point[N];

        // Чтение координат всех точек
        for (int i = 0; i < N; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int z = sc.nextInt();
            points[i] = new Point(x, y, z);
        }

        // Создание DSU для N точек
        DSU dsu = new DSU(N);

        // Попарная проверка всех точек на связность
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // Если расстояние между точками меньше D - объединяем их множества
                if (points[i].distanceTo(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        // Сбор статистики по размерам кластеров
        boolean[] counted = new boolean[N];  // массив для отметки уже посчитанных корней
        List<Integer> clusterSizes = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);  // находим корень текущей точки
            if (!counted[root]) {
                // Если этот корень еще не учитывался, добавляем размер его компоненты
                clusterSizes.add(dsu.getSize(root));
                counted[root] = true;  // помечаем как учтенный
            }
        }

        // Сортировка размеров кластеров по убыванию
        clusterSizes.sort(Collections.reverseOrder());

        // Вывод результатов
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) System.out.print(" ");
        }
        System.out.println();
    }
}