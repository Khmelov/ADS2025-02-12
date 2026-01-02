package by.it.group451003.plyushchevich.lesson14;

import java.util.*;

public class SitesB {

    private static class DSU {
        int[] parent;
        int[] rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Integer> idBySite = new HashMap<>();
        List<int[]> edges = new ArrayList<>();
        int idCounter = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\+");
            if (parts.length != 2) continue;

            String s1 = parts[0].trim();
            String s2 = parts[1].trim();
            if (s1.isEmpty() || s2.isEmpty()) continue;

            int id1 = idBySite.getOrDefault(s1, -1);
            if (id1 == -1) {
                id1 = idCounter++;
                idBySite.put(s1, id1);
            }

            int id2 = idBySite.getOrDefault(s2, -1);
            if (id2 == -1) {
                id2 = idCounter++;
                idBySite.put(s2, id2);
            }

            edges.add(new int[]{id1, id2});
        }

        int n = idCounter;
        if (n == 0) return;

        DSU dsu = new DSU(n);

        for (int[] e : edges) {
            dsu.union(e[0], e[1]);
        }

        Map<Integer, Integer> compSize = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            compSize.put(root, compSize.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(compSize.values());
        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }
}