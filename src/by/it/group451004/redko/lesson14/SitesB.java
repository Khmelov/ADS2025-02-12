package by.it.group451004.redko.lesson14;

import java.util.*;

public class SitesB {

    private static class DSU {
        private final int[] parent;
        private final int[] size;

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
                int t = ra; ra = rb; rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Map<String, Integer> id = new HashMap<>();
        List<int[]> pairs = new ArrayList<>();
        int idx = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            int plus = line.indexOf('+');
            if (plus <= 0 || plus >= line.length() - 1) continue;
            String a = line.substring(0, plus);
            String b = line.substring(plus + 1);
            Integer ia = id.get(a);
            if (ia == null) { ia = idx; id.put(a, idx++); }
            Integer ib = id.get(b);
            if (ib == null) { ib = idx; id.put(b, idx++); }
            pairs.add(new int[]{ia, ib});
        }

        DSU dsu = new DSU(idx);
        for (int[] p : pairs) dsu.union(p[0], p[1]);

        Map<Integer, Integer> comp = new HashMap<>();
        for (int i = 0; i < idx; i++) {
            int r = dsu.find(i);
            comp.put(r, dsu.size[r]);
        }
        List<Integer> sizes = new ArrayList<>(comp.values());
        sizes.sort(Comparator.reverseOrder());
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) out.append(' ');
            out.append(sizes.get(i));
        }
        System.out.println(out);
    }
}






