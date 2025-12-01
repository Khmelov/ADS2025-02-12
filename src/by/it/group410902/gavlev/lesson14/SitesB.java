package by.it.group410902.gavlev.lesson14;

import java.util.*;

public class SitesB {
    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        String find(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        void union(String a, String b) {
            a = find(a);
            b = find(b);
            if (a.equals(b)) return;
            if (size.get(a) < size.get(b)) {
                String t = a; a = b; b = t;
            }
            parent.put(b, a);
            size.put(a, size.get(a) + size.get(b));
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DSU dsu = new DSU();

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            dsu.union(parts[0], parts[1]);
        }

        Map<String, Integer> clusters = new HashMap<>();
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            clusters.put(root, clusters.getOrDefault(root, 0) + 1);
        }

        int[] result = clusters.values().stream().mapToInt(Integer::intValue).toArray();
        Arrays.sort(result);
        StringBuilder sb = new StringBuilder();
        for (int i = result.length - 1; i >= 0; i--) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(result[i]);
        }
        System.out.print(sb.toString());
    }
}
