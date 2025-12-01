package by.it.group410902.harkavy.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        // найти корень множества
        String find(String x) {
            String p = parent.get(x);
            if (p.equals(x)) return x;
            String root = find(p);
            parent.put(x, root);          // path compression
            return root;
        }

        // объединить множества
        void union(String a, String b) {
            a = find(a);
            b = find(b);
            if (a.equals(b)) return;

            // union by size
            int sa = size.get(a);
            int sb = size.get(b);

            if (sa < sb) {   // меняем местами, чтобы a всегда был крупнейшим
                String t = a; a = b; b = t;
            }

            parent.put(b, a);
            size.put(a, sa + sb);
        }

        // если сайт встречается впервые — создаём для него множество
        void add(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DSU dsu = new DSU();

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;

            String[] parts = line.split("\\+");
            if (parts.length != 2) continue;

            String a = parts[0].trim();
            String b = parts[1].trim();

            dsu.add(a);
            dsu.add(b);
            dsu.union(a, b);
        }

        // собираем размеры кластеров
        Map<String, Integer> compSizes = new HashMap<>();

        for (String s : dsu.parent.keySet()) {
            String root = dsu.find(s);
            compSizes.put(root, compSizes.getOrDefault(root, 0) + 1);
        }

        // сортируем strictly descending — как требуют тесты
        List<Integer> res = new ArrayList<>(compSizes.values());
        res.sort(Collections.reverseOrder());

        // вывод
        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i));
            if (i + 1 < res.size()) System.out.print(" ");
        }
    }
}
