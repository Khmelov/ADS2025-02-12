package by.it.group410901.getmanchuk.lesson14;

import java.util.*;

// Группировка сайтов по общим ссылкам с помощью DSU

public class SitesB {

    // DSU для объединения и поиска кластеров сайтов
    static class DSU {

        // Родитель каждого элемента
        private Map<String, String> parent;
        // Ранг дерева
        private Map<String, Integer> rank;
        // Размер множества
        private Map<String, Integer> size;

        // Инициализация пустых структур
        public DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
            size = new HashMap<>();
        }

        // Создание нового множества для сайта
        public void makeSet(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
            }
        }

        // Поиск представителя множества с сжатием пути
        public String find(String x) {
            if (!parent.get(x).equals(x))
                parent.put(x, find(parent.get(x)));
            return parent.get(x);
        }

        // Объединение двух множеств
        public void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);
            if (rootX.equals(rootY)) return;

            if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
                size.put(rootY, size.get(rootY) + size.get(rootX));
            } else if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            } else {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }

        // Получение размера множества, к которому принадлежит сайт
        public int getSize(String x) {
            return size.get(find(x));
        }
    }

    public static void main(String[] args) {

        // Чтение входных данных
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();
        Set<String> allSites = new HashSet<>();

        // Обработка строк до слова "end"
        while (true) {
            String line = scanner.nextLine();
            if ("end".equals(line)) break;

            String[] sites = line.split("\\+");
            String site1 = sites[0].trim();
            String site2 = sites[1].trim();

            allSites.add(site1);
            allSites.add(site2);

            dsu.makeSet(site1);
            dsu.makeSet(site2);
            dsu.union(site1, site2);
        }

        // Подсчёт размеров всех кластеров
        Map<String, Integer> clusterSizes = new HashMap<>();
        for (String site : allSites) {
            String root = dsu.find(site);
            clusterSizes.put(root, dsu.getSize(root));
        }

        // Сбор всех размеров кластеров
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());

        // Сортировка по убыванию
        Collections.sort(sizes, Collections.reverseOrder());

        // Вывод размеров кластеров
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }
}