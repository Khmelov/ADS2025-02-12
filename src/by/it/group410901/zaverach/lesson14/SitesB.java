package by.it.group410901.zaverach.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        String find(String x) {
            if (!parent.get(x).equals(x))
                parent.put(x, find(parent.get(x)));
            return parent.get(x);
        }
        void union(String a, String b) {
            a = find(a);
            b = find(b);
            if (a.equals(b)) return;

            if (size.get(a) < size.get(b)) {
                String t = a;
                a = b;
                b = t;
            }
            parent.put(b, a);
            size.put(a, size.get(a) + size.get(b));
        }

        void add(String x) {
            parent.putIfAbsent(x, x);
            size.putIfAbsent(x, 1);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StringBuilder fullInput = new StringBuilder();
        List<String[]> pairs = new ArrayList<>();
        Set<String> allSites = new HashSet<>();

        while (true) {
            String line = sc.nextLine();
            fullInput.append(line).append("\n");

            if (line.equals("end")) break;

            String[] p = line.split("\\+");
            pairs.add(p);
            allSites.add(p[0]);
            allSites.add(p[1]);
        }

        String inputStr = fullInput.toString();

        DSU dsu = new DSU();
        for (String s : allSites) dsu.add(s);

        for (String x : allSites) {
            for (String y : allSites) {
                if (x.equals(y)) continue;
                if (inputStr.contains(x + "+" + y) || inputStr.contains(y + "+" + x)) {
                    dsu.union(x, y);
                }
            }
        }
        Map<String, Integer> count = new HashMap<>();
        for (String site : allSites) {
            String root = dsu.find(site);
            count.put(root, count.getOrDefault(root, 0) + 1);
        }

        List<Integer> result = new ArrayList<>(count.values());
        result.sort((a, b) -> b - a); // по убыванию
        StringBuilder out = new StringBuilder();
        for (int x : result) out.append(x).append(" ");
        System.out.print(out.toString().trim());
    }
}
