package by.it.group410902.habrukovich.lesson14;

import java.util.*;

public class PointsA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();
        int N = sc.nextInt();

        int[][] p = new int[N][3];
        for (int i = 0; i < N; i++) {
            p[i][0] = sc.nextInt();
            p[i][1] = sc.nextInt();
            p[i][2] = sc.nextInt();
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {

                double dx = p[i][0] - p[j][0];
                double dy = p[i][1] - p[j][1];
                double dz = p[i][2] - p[j][2];

                double dist = Math.hypot(Math.hypot(dx, dy), dz);

                if (dist < D) {
                    dsu.union(i, j);
                }
            }
        }

        // соберём размеры всех кластеров
        ArrayList<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (dsu.find(i) == i) {
                sizes.add(dsu.size(i));
            }
        }

        // сортировка по убыванию
        sizes.sort((a, b) -> b - a);

        // вывод
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }

    // класс DSU с эвристикой по размеру
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

        int find(int v) {
            if (parent[v] != v)
                parent[v] = find(parent[v]); // сжатие пути
            return parent[v];
        }
        // объединение по размеру (меньший к большему)
        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;

            if (size[a] < size[b]) {
                parent[a] = b;
                size[b] += size[a];
            } else {
                parent[b] = a;
                size[a] += size[b];
            }
        }

        int size(int v) {
            return size[find(v)];
        }
    }
}
