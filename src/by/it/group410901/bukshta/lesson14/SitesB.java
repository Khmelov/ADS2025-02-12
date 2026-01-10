package by.it.group410901.bukshta.lesson14;

import java.util.*;

public class SitesB {

    // объединяет связанные сайты в группы
    static class DSU {
        private final int[] parent;
        private final int[] rank;   // высота дерева
        private final int[] size;  //размер множества

        public DSU(int n) { // инициализация множеств
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        // поиск корня множества
        public int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]]; // сокращаем путь к корню
                x = parent[x];
            }
            return x;
        }

        // объединение двух множеств по рангу
        public void union(int x, int y) {
            int px = find(x);
            int py = find(y);
            if (px == py) return;

            if (rank[px] < rank[py]) {
                parent[px] = py;
                size[py] += size[px];
            } else if (rank[px] > rank[py]) {
                parent[py] = px;
                size[px] += size[py];
            } else {
                parent[py] = px;
                size[px] += size[py];
                rank[px]++;
            }
        }

        // возвращает размер множества
        public int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // сопоставление названия сайта с его уникальным ID
        Map<String, Integer> siteToId = new HashMap<>();

        // хранение всех связей между сайтами
        List<String[]> edges = new ArrayList<>();

        int nextId = 0;

        // чтение строк
        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;

            String[] parts = line.split("\\+", 2);
            if (parts.length != 2) continue;

            String a = parts[0];
            String b = parts[1];

            // если сайт встречается впервые — присваиваем ему новый ID
            siteToId.putIfAbsent(a, nextId++);
            siteToId.putIfAbsent(b, nextId++);

            edges.add(new String[]{a, b}); // сохраняем связь
        }

        int n = nextId; // количество уникальных сайтов
        if (n == 0) {
            System.out.println();
            return;
        }

        // создаём структуру DSU для объединения сайтов
        DSU dsu = new DSU(n);

        // объединяем сайты, которые связаны между собой
        for (String[] e : edges) {
            int idA = siteToId.get(e[0]);
            int idB = siteToId.get(e[1]);
            dsu.union(idA, idB);
        }

        // собираем размеры всех кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.getSize(i));
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());

        sizes.sort((a, b) -> Integer.compare(b, a));

        // вывод
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
}
