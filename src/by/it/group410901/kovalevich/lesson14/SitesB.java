package by.it.group410901.kovalevich.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        int[] parent, size;
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
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String[]> pairs = new ArrayList<>();
        LinkedHashSet<String> all = new LinkedHashSet<>(); //собираем порядок

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\+");
            if (parts.length != 2) continue;
            String a = parts[0].trim();
            String b = parts[1].trim();
            pairs.add(new String[]{a, b});
            all.add(a);
            all.add(b);
        }

        //присваиваем id сайтам
        Map<String, Integer> id = new HashMap<>();
        int idx = 0;
        for (String s : all) id.put(s, idx++);

        DSU dsu = new DSU(idx);
        for (String[] p : pairs) {
            int u = id.get(p[0]);
            int v = id.get(p[1]);
            dsu.union(u, v);
        }

        Map<Integer, Integer> cnt = new HashMap<>();
        for (int i = 0; i < idx; i++) {
            int r = dsu.find(i);
            cnt.put(r, cnt.getOrDefault(r, 0) + 1);
        }

        int m = cnt.size();
        int[] sizes = new int[m];
        int k = 0;
        for (int v : cnt.values()) sizes[k++] = v;
        Arrays.sort(sizes);

        for (int i = m - 1; i >= 0; i--) {
            System.out.print(sizes[i]);
            if (i > 0) System.out.print(" ");
        }
    }
}