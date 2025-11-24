package by.it.group410901.kalach.lesson14;

import java.util.*;

public class SitesB {

    // Класс для системы непересекающихся множеств (Disjoint Set Union)
    static class DSU {
        private Map<String, String> parent; // Хранит родительский элемент для каждого сайта
        private Map<String, Integer> rank;   // Хранит ранг (глубину) для каждого корневого элемента

        public DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
        }

        // Находит корневой элемент для сайта с применением сжатия пути
        public String find(String site) {
            // Если сайт встречается впервые, инициализируем его
            if (!parent.containsKey(site)) {
                parent.put(site, site);  // Сайт становится корнем своего множества
                rank.put(site, 0);       // Начальный ранг = 0
                return site;
            }

            // Сжатие пути: перенаправляем узел напрямую к корню
            if (!parent.get(site).equals(site)) {
                parent.put(site, find(parent.get(site)));
            }

            return parent.get(site);
        }

        // Объединяет два множества, содержащие site1 и site2
        public void union(String site1, String site2) {
            String root1 = find(site1);
            String root2 = find(site2);

            if (root1.equals(root2)) {
                return; // Сайты уже находятся в одном множестве
            }

            // Объединение по рангу: присоединяем дерево с меньшим рангом к дереву с большим рангом
            int rank1 = rank.get(root1);
            int rank2 = rank.get(root2);

            if (rank1 < rank2) {
                parent.put(root1, root2); // root1 присоединяется к root2
            } else if (rank1 > rank2) {
                parent.put(root2, root1); // root2 присоединяется к root1
            } else {
                // При равных рангах выбираем произвольно и увеличиваем ранг
                parent.put(root2, root1);
                rank.put(root1, rank1 + 1);
            }
        }

        // Возвращает размеры всех кластеров (компонент связности)
        public List<Integer> getClusterSizes() {
            Map<String, Integer> clusterSizes = new HashMap<>();

            // Подсчитываем размер каждого кластера
            for (String site : parent.keySet()) {
                String root = find(site); // Находим корень для каждого сайта
                clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
            }

            // Преобразуем в список и сортируем в порядке убывания
            List<Integer> sizes = new ArrayList<>(clusterSizes.values());
            Collections.sort(sizes, Collections.reverseOrder());

            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU(); // Создаем экземпляр DSU для управления связями между сайтами

        // Чтение входных данных до ввода "end"
        while (true) {
            String line = scanner.nextLine().trim();

            if (line.equals("end")) {
                break; // Завершаем ввод данных
            }

            // Разбираем строку на пару сайтов, разделенных знаком '+'
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();
                dsu.union(site1, site2); // Объединяем сайты в один кластер
            }
        }

        // Получаем размеры кластеров
        List<Integer> clusterSizes = dsu.getClusterSizes();

        // Выводим размеры кластеров в порядке убывания
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) {
                System.out.print(" "); // Разделитель между числами
            }
        }
        System.out.println();

        scanner.close();
    }
}