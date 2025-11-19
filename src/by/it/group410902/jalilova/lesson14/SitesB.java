package by.it.group410902.jalilova.lesson14;
import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteIndexMap = new HashMap<>();
        List<String> sitesList = new ArrayList<>();
        List<int[]> connections = new ArrayList<>();

        // чтение входных данных до команды "end"
        while (true) {
            String inputLine = scanner.nextLine();
            if (inputLine.equals("end")) break;

            String[] connectedSites = inputLine.split("\\+");
            String firstSite = connectedSites[0];
            String secondSite = connectedSites[1];

            // добавляем сайты в словарь, если их еще нет
            if (!siteIndexMap.containsKey(firstSite)) {
                siteIndexMap.put(firstSite, sitesList.size());
                sitesList.add(firstSite);
            }
            if (!siteIndexMap.containsKey(secondSite)) {
                siteIndexMap.put(secondSite, sitesList.size());
                sitesList.add(secondSite);
            }

            // сохраняем связь между сайтами
            int firstId = siteIndexMap.get(firstSite);
            int secondId = siteIndexMap.get(secondSite);
            connections.add(new int[]{firstId, secondId});
        }

        int totalSites = sitesList.size();
        DisjointSet dsu = new DisjointSet(totalSites);

        // объединяем связанные сайты в кластеры
        for (int[] connection : connections) {
            dsu.unite(connection[0], connection[1]);
        }

        // собираем размеры кластеров
        PriorityQueue<Integer> clusterSizes = new PriorityQueue<>(Collections.reverseOrder());
        boolean[] processedRoots = new boolean[totalSites];
        for (int i = 0; i < totalSites; i++) {
            int root = dsu.findRoot(i);
            if (!processedRoots[root]) {
                processedRoots[root] = true;
                clusterSizes.add(dsu.getClusterSize(root));
            }
        }

        // формируем выходную строку
        StringBuilder result = new StringBuilder();
        while (!clusterSizes.isEmpty()) {
            if (result.length() > 0) result.append(" ");
            result.append(clusterSizes.poll());
        }
        System.out.print(result);
    }

    // реализация системы непересекающихся множеств
    static class DisjointSet {
        private final int[] parent;
        private final int[] clusterSize;

        public DisjointSet(int n) {
            parent = new int[n];
            clusterSize = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                clusterSize[i] = 1;
            }
        }

        // находим корневой элемент с оптимизацией пути
        public int findRoot(int x) {
            if (parent[x] != x) {
                parent[x] = findRoot(parent[x]);
            }
            return parent[x];
        }

        // объединяем два множества по размеру
        public void unite(int x, int y) {
            int rootX = findRoot(x);
            int rootY = findRoot(y);

            if (rootX != rootY) {
                // присоединяем меньшее дерево к большему
                if (clusterSize[rootX] < clusterSize[rootY]) {
                    parent[rootX] = rootY;
                    clusterSize[rootY] += clusterSize[rootX];
                } else {
                    parent[rootY] = rootX;
                    clusterSize[rootX] += clusterSize[rootY];
                }
            }
        }

        // получаем размер кластера
        public int getClusterSize(int x) {
            return clusterSize[findRoot(x)];
        }
    }
}