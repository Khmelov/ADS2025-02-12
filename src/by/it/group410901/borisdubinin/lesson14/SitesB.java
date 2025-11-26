package by.it.group410901.borisdubinin.lesson14;
import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Сначала читаем все строки чтобы определить уникальные сайты
        List<String[]> pairs = new ArrayList<>();
        Set<String> allSites = new HashSet<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }

            String[] sites = line.split("\\+");
            if (sites.length != 2) {
                continue;
            }

            String site1 = sites[0].trim();
            String site2 = sites[1].trim();

            allSites.add(site1);
            allSites.add(site2);
            pairs.add(new String[]{site1, site2});
        }

        // Создаем маппинг сайт -> индекс
        List<String> sitesList = new ArrayList<>(allSites);
        Map<String, Integer> siteToIndex = new HashMap<>();
        for (int i = 0; i < sitesList.size(); i++) {
            siteToIndex.put(sitesList.get(i), i);
        }

        // Инициализация DSU
        int n = allSites.size();
        DSU dsu = new DSU(n);

        // Объединение связанных сайтов в кластеры
        for (String[] pair : pairs) {
            int index1 = siteToIndex.get(pair[0]);
            int index2 = siteToIndex.get(pair[1]);
            dsu.union(index1, index2);
        }

        // Получение и вывод размеров кластеров в порядке убывания
        List<Integer> clusterSizes = dsu.getClusterSizesDescending();
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();

        scanner.close();
    }

    static class DSU {
        private int[] parent;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int leader(int x) {
            if (parent[x] != x) {
                parent[x] = leader(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = leader(x);
            int rootY = leader(y);

            if (rootX == rootY) return;

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        public int getSize(int x) {
            return size[leader(x)];
        }

        public List<Integer> getClusterSizesDescending() {
            Set<Integer> roots = new HashSet<>();
            List<Integer> sizes = new ArrayList<>();

            for (int i = 0; i < parent.length; i++) {
                int root = leader(i);
                if (roots.add(root)) {
                    sizes.add(size[root]);
                }
            }

            Collections.sort(sizes, Collections.reverseOrder());
            return sizes;
        }
    }
}