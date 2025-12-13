package by.it.group451001.apanovich.lesson14;

import java.io.*;
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
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        List<String> pairs = new ArrayList<>();
        LinkedHashSet<String> sitesSet = new LinkedHashSet<>();

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            pairs.add(line);
            int plus = line.indexOf('+');
            if (plus >= 0) {
                String a = line.substring(0, plus);
                String b = line.substring(plus + 1);
                sitesSet.add(a);
                sitesSet.add(b);
            }
        }

        Map<String, Integer> id = new HashMap<>();
        int idx = 0;
        for (String s : sitesSet) id.put(s, idx++);

        if (idx == 0) {
            System.out.println();
            return;
        }

        DSU dsu = new DSU(idx);

        for (String p : pairs) {
            int plus = p.indexOf('+');
            if (plus < 0) continue;
            String a = p.substring(0, plus);
            String b = p.substring(plus + 1);
            int ia = id.get(a);
            int ib = id.get(b);
            dsu.union(ia, ib);
        }

        Map<Integer, Integer> cnt = new HashMap<>();
        for (int i = 0; i < idx; i++) {
            int r = dsu.find(i);
            cnt.put(r, cnt.getOrDefault(r, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(cnt.values());
        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }
}
