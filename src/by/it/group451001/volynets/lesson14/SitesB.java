package by.it.group451001.volynets.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SitesB {

    static class DSU {
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
            if (parent[x] != x) parent[x] = find(parent[x]);
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

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> id = new HashMap<>();
        List<int[]> edges = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            int plus = line.indexOf('+');
            if (plus < 0) continue;
            String a = line.substring(0, plus);
            String b = line.substring(plus + 1);
            Integer ia = id.get(a);
            if (ia == null) {
                ia = id.size();
                id.put(a, ia);
            }
            Integer ib = id.get(b);
            if (ib == null) {
                ib = id.size();
                id.put(b, ib);
            }
            edges.add(new int[]{ia, ib});
        }

        int n = id.size();
        DSU dsu = new DSU(n);

        for (int[] e : edges) {
            dsu.union(e[0], e[1]);
        }

        Map<Integer, Integer> compSize = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int r = dsu.find(i);
            compSize.put(r, compSize.getOrDefault(r, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(compSize.values());
        Collections.sort(sizes);
        Collections.reverse(sizes);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) out.append(' ');
            out.append(sizes.get(i));
        }
        System.out.println(out.toString());
    }
}