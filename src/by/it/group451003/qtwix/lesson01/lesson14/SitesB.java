package by.it.group451003.qtwix.lesson01.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SitesB {

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

    public static void main(String[] args) {
        var points = new ArrayList<String>();
        var dsu = new DSU(1000);

        try (var scanner = new Scanner(System.in)) {
            points = new ArrayList<>();
            String line;
            while (!(line = scanner.next()).equals("end")) {
                var pair = line.split("\\+");
                var x1 = points.indexOf(pair[0]);
                if (x1 == -1)
                    points.add(pair[0]);
                x1 = points.indexOf(pair[0]);

                var x2 = points.indexOf(pair[1]);
                if (x2 == -1)
                    points.add(pair[1]);
                x2 = points.indexOf(pair[1]);

                dsu.union(x1, x2);
            }
        }

        var dsuSizes = dsu.getSizes();
        dsuSizes.sort(Collections.reverseOrder());

        System.out.println(String.join(" ", dsuSizes.stream().map(String::valueOf).toArray(String[]::new)));
    }
}