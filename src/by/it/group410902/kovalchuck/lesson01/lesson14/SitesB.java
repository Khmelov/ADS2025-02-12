package by.it.group410902.kovalchuck.lesson01.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Словарь с имени сайта
        Map<String, Integer> siteToIndex = new HashMap<>();

        List<String> sites = new ArrayList<>();
        // DSU для управления множествами сайтов
        DSU dsu = new DSU();

        // Читаем входные данные
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // Проверяем условие завершения ввода
            if ("end".equals(line)) {
                break;
            }

            // Разделяем строку
            String[] parts = line.split("\\+");

            if (parts.length != 2) {
                continue;
            }

            String site1 = parts[0];
            String site2 = parts[1];

            // Если сайт встречается впервые, добавляем его в словарь и DSU
            if (!siteToIndex.containsKey(site1)) {
                siteToIndex.put(site1, sites.size());
                sites.add(site1);
                dsu.makeSet(sites.size() - 1);
            }

            // Обрабатываем второй сайт аналогично
            if (!siteToIndex.containsKey(site2)) {
                siteToIndex.put(site2, sites.size());
                sites.add(site2);
                dsu.makeSet(sites.size() - 1);
            }

            // Получаем индексы сайтов
            int index1 = siteToIndex.get(site1);
            int index2 = siteToIndex.get(site2);

            // Объединяем множества, содержащие эти сайты
            dsu.union(index1, index2);
        }

        // Собираем статистику по размерам кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < sites.size(); i++) {
            // Находим корневой элемент для текущего сайта
            int root = dsu.find(i);
            // Увеличиваем счетчик размера кластера
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Создаем список размеров кластеров
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        // Сортируем размеры кластеров в порядке убывания
        Collections.sort(sizes, Collections.reverseOrder());

        // Выводим результат
        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            // Добавляем пробел между числами, кроме последнего
            if (i < sizes.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    static class DSU {
        private List<Integer> parent;
        private List<Integer> rank;

        public DSU() {
            parent = new ArrayList<>();
            rank = new ArrayList<>();
        }

        //Создает новое множество для элемента x
        public void makeSet(int x) {
            // Увеличиваем размер списков, если необходимо
            while (parent.size() <= x) {
                // Каждый новый элемент становится корнем своего множества
                parent.add(parent.size());
                rank.add(0);
            }
        }

        public int find(int x) {
            // Если x не является корнем своего множества
            if (parent.get(x) != x) {
                // Рекурсивно находим корень и обновляем родителя
                parent.set(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(int x, int y) {
            // Находим корни множеств для x и y
            int rootX = find(x);
            int rootY = find(y);

            // Если элементы уже в одном множестве, ничего не делаем
            if (rootX != rootY) {
                // Сравниваем ранги корней
                if (rank.get(rootX) < rank.get(rootY)) {
                    // Присоединяем множество с меньшим рангом к множеству с большим рангом
                    parent.set(rootX, rootY);
                } else if (rank.get(rootX) > rank.get(rootY)) {
                    // Присоединяем множество с меньшим рангом к множеству с большим рангом
                    parent.set(rootY, rootX);
                } else {
                    // Если ранги равны, присоединяем одно множество к другому
                    // и увеличиваем ранг корня
                    parent.set(rootY, rootX);
                    rank.set(rootX, rank.get(rootX) + 1);
                }
            }
        }
    }
}