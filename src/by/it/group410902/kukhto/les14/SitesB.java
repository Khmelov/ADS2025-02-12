package by.it.group410902.kukhto.les14;
import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToId = new HashMap<>();
        List<String> idToSite = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();//список связей

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) break;

            String[] sites = line.split("\\+");
            String site1 = sites[0];
            String site2 = sites[1];

            if (!siteToId.containsKey(site1)) {
                siteToId.put(site1, idToSite.size());
                idToSite.add(site1);
            }
            if (!siteToId.containsKey(site2)) {
                siteToId.put(site2, idToSite.size());
                idToSite.add(site2);
            }

            int id1 = siteToId.get(site1);
            int id2 = siteToId.get(site2);
            edges.add(new int[]{id1, id2});
        }

        int n = idToSite.size();
        DSU dsu = new DSU(n);

        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        // сортировка по убыванию
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                pq.add(dsu.getSize(root));
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(pq.poll());
        }
        System.out.print(sb);
    }

    static class DSU {
        private final int[] parent;
        private final int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        //если корни разные
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                if (size[rootX] < size[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                }
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }
}