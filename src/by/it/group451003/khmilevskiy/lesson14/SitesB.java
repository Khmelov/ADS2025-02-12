package by.it.group451003.khmilevskiy.lesson14;

import java.util.*;

public class SitesB {
    static class DSU {
        int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx != ry) {
                if (rank[rx] < rank[ry]) {
                    parent[rx] = ry;
                } else {
                    parent[ry] = rx;
                    if (rank[rx] == rank[ry]) {
                        rank[rx]++;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToId = new HashMap<>();
        List<String> unique = new ArrayList<>();
        List<String[]> pairs = new ArrayList<>();
        int id = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }
            String[] parts = line.split("\\+");
            String a = parts[0];
            String b = parts[1];
            pairs.add(parts);
            if (!siteToId.containsKey(a)) {
                siteToId.put(a, id++);
                unique.add(a);
            }
            if (!siteToId.containsKey(b)) {
                siteToId.put(b, id++);
                unique.add(b);
            }
        }
        int n = id;
        DSU dsu = new DSU(n);
        for (String[] p : pairs) {
            int ia = siteToId.get(p[0]);
            int ib = siteToId.get(p[1]);
            dsu.union(ia, ib);
        }
        Map<Integer, Integer> compSize = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int r = dsu.find(i);
            compSize.put(r, compSize.getOrDefault(r, 0) + 1);
        }
        List<Integer> sizes = new ArrayList<>(compSize.values());
        sizes.sort(Collections.reverseOrder());
        StringBuilder sb = new StringBuilder();
        for (int s : sizes) {
            sb.append(s).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}