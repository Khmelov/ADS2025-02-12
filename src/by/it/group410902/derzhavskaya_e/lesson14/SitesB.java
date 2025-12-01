package by.it.group410902.derzhavskaya_e.lesson14;

import java.util.*;

/**
 * Кластеризация связанных сайтов с использованием DSU
 */
public class SitesB {

    static class DSU {
        private final Map<String, String> parent = new HashMap<>();
        private final Map<String, Integer> rank = new HashMap<>();

        // Создаем новое множество
        void makeSet(String x) {
            parent.putIfAbsent(x, x);
            rank.putIfAbsent(x, 0);
        }

        // Поиск с path compression
        String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // path compression
            }
            return parent.get(x);
        }

        // Union by rank
        void union(String a, String b) {
            String ra = find(a);
            String rb = find(b);
            if (ra.equals(rb)) return;

            int r1 = rank.get(ra);
            int r2 = rank.get(rb);

            if (r1 < r2) {
                parent.put(ra, rb);
            } else if (r1 > r2) {
                parent.put(rb, ra);
            } else {
                parent.put(rb, ra);
                rank.put(ra, r1 + 1);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        DSU dsu = new DSU();
        List<String[]> pairs = new ArrayList<>();

        // Ввод пар
        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            if (!line.contains("+")) continue;

            String[] p = line.split("\\+");
            String a = p[0];
            String b = p[1];

            dsu.makeSet(a);
            dsu.makeSet(b);

            pairs.add(new String[]{a, b});
        }

        // Объединение
        for (String[] pair : pairs) {
            dsu.union(pair[0], pair[1]);
        }

        // Подсчёт кластеров
        Map<String, Integer> clusterSizes = new HashMap<>();

        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Сортировка размеров
        List<Integer> result = new ArrayList<>(clusterSizes.values());
        result.sort(Collections.reverseOrder());


        // Вывод
        for (int s : result) {
            System.out.print(s + " ");
        }
    }
}
