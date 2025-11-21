package by.it.group410902.plekhova.lesson14;

import java.util.*;

public class SitesB {

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
            if (a == b) return;


            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }

            parent[b] = a;
            size[a] += size[b];
        }

        // вычисляем размеры кластеров
        List<Integer> clusterSizes(int n) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < n; i++) {
                int root = find(i);
                map.put(root, map.getOrDefault(root, 0) + 1);
            }
            return new ArrayList<>(map.values());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Integer> id = new HashMap<>();
        List<String[]> pairs = new ArrayList<>();

        String line;
        while (!(line = sc.nextLine().trim()).equals("end")) {
            String[] p = line.split("\\+");
            pairs.add(p);

            // регистрируем сайты в map
            if (!id.containsKey(p[0])) id.put(p[0], id.size());
            if (!id.containsKey(p[1])) id.put(p[1], id.size());
        }

        int n = id.size();
        DSU dsu = new DSU(n);

        // объединение согласно парам
        for (String[] p : pairs) {
            int a = id.get(p[0]);
            int b = id.get(p[1]);
            dsu.union(a, b);
        }

        // получаем размеры кластеров
        List<Integer> sizes = dsu.clusterSizes(n);

        // сортировка по убыванию (как ожидает тест)
        sizes.sort((a, b) -> b - a);

        // вывод
        for (int s : sizes) {
            System.out.print(s + " ");
        }
    }
}
