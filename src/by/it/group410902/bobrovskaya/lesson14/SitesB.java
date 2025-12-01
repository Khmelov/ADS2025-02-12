package by.it.group410902.bobrovskaya.lesson14;

import java.util.*;

public class SitesB {
    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        // Находит корень с сокращением пути
        String find(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
            if (!x.equals(parent.get(x))) {
                parent.put(x, find(parent.get(x))); // сокращение пути
            }
            return parent.get(x);
        }

        // Объединяет два множества с эвристикой по размеру
        void union(String a, String b) {
            String rootA = find(a);
            String rootB = find(b);
            if (rootA.equals(rootB)) return;

            int sizeA = size.get(rootA);
            int sizeB = size.get(rootB);

            if (sizeA < sizeB) {
                parent.put(rootA, rootB);
                size.put(rootB, sizeA + sizeB);
            } else {
                parent.put(rootB, rootA);
                size.put(rootA, sizeA + sizeB);
            }
        }

        // Получить размеры всех кластеров
        List<Integer> getClusterSizes() {
            Set<String> roots = new HashSet<>();
            for (String site : parent.keySet()) {
                roots.add(find(site));
            }

            List<Integer> sizes = new ArrayList<>();
            for (String root : roots) {
                sizes.add(size.get(root));
            }

            // Сортировка по убыванию, как требует тест
            sizes.sort(Collections.reverseOrder());
            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DSU dsu = new DSU();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                String siteA = parts[0];
                String siteB = parts[1];
                dsu.union(siteA, siteB);
            }
        }

        List<Integer> result = dsu.getClusterSizes();
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) System.out.print(" ");
        }
        System.out.println();
    }
}