package by.it.group410902.shahov.lesson14;

import java.util.*;

public class PointsA {
    // Класс для системы непересекающихся множеств (Disjoint Set Union)
    static class DSU {
        int[] parent; // массив родительских вершин
        int[] size;   // массив размеров компонент

        // Конструктор: инициализация DSU для n элементов
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // изначально каждый элемент - корень своего множества
                size[i] = 1;   // размер каждого множества = 1
            }
        }

        // Находит корень элемента x с применением сжатия путей
        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]); // рекурсивное сжатие пути
            return parent[x];
        }

        // Объединяет два множества, содержащие элементы a и b
        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) return; // уже в одном множестве

            // Эвристика объединения по размеру: меньшее дерево присоединяется к большему
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            } else {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }

        // Возвращает размер компоненты, содержащей элемент x
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

        // Вычисляет евклидово расстояние до другой точки
        double distanceTo(Point other) {
            int dx = x - other.x;
            int dy = y - other.y;
            int dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble(); // пороговое расстояние для объединения в кластер
        int N = sc.nextInt();       // количество точек
        Point[] points = new Point[N]; // массив точек

        // Чтение координат точек
        for (int i = 0; i < N; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int z = sc.nextInt();
            points[i] = new Point(x, y, z);
        }

        // Инициализация DSU для N точек
        DSU dsu = new DSU(N);

        // Попарная проверка всех точек на близость
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // Если расстояние между точками меньше порога D, объединяем их кластеры
                if (points[i].distanceTo(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        // Сбор информации о размерах кластеров
        boolean[] counted = new boolean[N]; // флаги для учета корней кластеров
        List<Integer> clusterSizes = new ArrayList<>();

        // Проход по всем точкам для сбора уникальных кластеров
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i); // находим корень кластера
            if (!counted[root]) {
                clusterSizes.add(dsu.getSize(root)); // добавляем размер кластера
                counted[root] = true; // помечаем кластер как учтенный
            }
        }

        // Сортировка кластеров по убыванию размера
        clusterSizes.sort(Collections.reverseOrder());

        // Вывод результатов
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) System.out.print(" ");
        }
        System.out.println();
    }
}
