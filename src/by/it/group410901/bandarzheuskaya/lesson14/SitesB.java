package by.it.group410901.bandarzheuskaya.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private final int[] parent; //родитель сайта
        private final int[] rank;   //высота дерева
        private final int[] size;   //размер кластера

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        // Сокращение пути
        public int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];  //каждый узел -> дедушка
                x = parent[x];
            }
            return x;
        }

        // Union by rank
        public void union(int x, int y) {
            int px = find(x);
            int py = find(y);
            if (px == py) return;   //если в одном кластере выходим

            if (rank[px] < rank[py]) {  //Приклеиваем меньшее дерево к большему
                parent[px] = py;
                size[py] += size[px];
            } else if (rank[px] > rank[py]) {   //Приклеиваем меньшее к большему
                parent[py] = px;
                size[px] += size[py];
            } else {    //Приклеиваем py к px и увеличиваем ранг px
                parent[py] = px;
                size[px] += size[py];
                rank[px]++;
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Integer> siteToId = new HashMap<>();
        List<String[]> edges = new ArrayList<>();

        int nextId = 0; //каждому сайту присваеваем уникальный айди
        while (true) {  //читаем пары сайтов
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;

            String[] parts = line.split("\\+", 2);
            if (parts.length != 2) continue;

            String a = parts[0];
            String b = parts[1];

            siteToId.putIfAbsent(a, nextId++);
            siteToId.putIfAbsent(b, nextId++);

            edges.add(new String[]{a, b});  //сохраняем как ребро
        }

        int n = nextId; //колво уникальных сайтов
        if (n == 0) {
            System.out.println();
            return;
        }

        DSU dsu = new DSU(n);

        for (String[] e : edges) {  //сайты, связанные цепочкой ссылок, попадают в один кластер
            int idA = siteToId.get(e[0]);
            int idB = siteToId.get(e[1]);
            dsu.union(idA, idB);
        }

        // Собираем размеры кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.getSize(i));
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());

        // Сортировка по убыванию
        sizes.sort((a, b) -> Integer.compare(b, a));

        // Вывод
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
}