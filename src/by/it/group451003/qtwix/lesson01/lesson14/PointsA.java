package by.it.group451003.qtwix.lesson01.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PointsA {

    static class DSU {
        int[] parent;
        int[] rank;

        DSU(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a != b) {
                if (rank[a] < rank[b]) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                parent[b] = a;
                if (rank[a] == rank[b])
                    rank[a]++;
            }
        }

        int find(int i) {
            return i == parent[i] ? i : find(parent[i]);
        }

        ArrayList<Integer> getSizes() {
            var componentSizes = new ArrayList<Integer>();
            for (int i = 0; i < parent.length; i++) {
                var root = find(i);
                while (componentSizes.size() <= root)
                    componentSizes.add(0);
                componentSizes.set(root, componentSizes.get(root) + 1);
            }
            return componentSizes;
        }
    }

    private static class Point {
        final int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        double distanceTo(Point other) {
            return Math.hypot(Math.hypot(this.x - other.x, this.y - other.y), this.z - other.z);
        }
    }

    private static ArrayList<Integer> calculateClusterSizes(int distance, int size, ArrayList<Point> points) {
        var dsu = new DSU(size);

        for (int i = 0; i < size; i++)
            for (int j = i + 1; j < size; j++)
                if (points.get(i).distanceTo(points.get(j)) < distance)
                    dsu.union(i, j);

        var dsuSizes = dsu.getSizes();

        dsuSizes.removeIf(s -> s == 0);
        dsuSizes.sort(Collections.reverseOrder());

        return dsuSizes;
    }

    public static void main(String[] args) {
        int distance, length;
        var points = new ArrayList<Point>();

        try (var scanner = new Scanner(System.in)) {
            distance = scanner.nextInt();
            length = scanner.nextInt();

            for (int i = 0; i < length; i++)
                points.add(new Point(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
        }

        var dsuSizes = calculateClusterSizes(distance, length, points);

        var result = String.join(" ", dsuSizes.stream().map(String::valueOf).toArray(String[]::new));
        System.out.println(result);
    }
}