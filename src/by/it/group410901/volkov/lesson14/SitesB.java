package by.it.group410901.volkov.lesson14;

import java.util.*;

/**
 * Класс для кластеризации связанных через гиперссылки сайтов
 * Использует структуру данных DSU с двумя эвристиками:
 * 1. По размеру поддерева (union by size)
 * 2. По сокращению пути (path compression)
 * 
 * Сайты объединяются в кластеры, если между ними есть связь (гиперссылка)
 * Направление ссылок не учитывается (связь двусторонняя)
 */
public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Map для хранения соответствия сайта и его индекса в DSU
        // Это позволяет работать с DSU по индексам, а не по строкам
        Map<String, Integer> siteToIndex = new HashMap<>();
        List<String> sites = new ArrayList<>(); // Список всех уникальных сайтов
        List<int[]> pairs = new ArrayList<>();  // Список пар связанных сайтов (индексы)

        // Читаем пары связанных сайтов до строки "end"
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            
            // Проверяем условие завершения ввода
            if (line.equals("end")) {
                break;
            }

            // Разделяем строку по символу "+"
            String[] parts = line.split("\\+");
            if (parts.length != 2) {
                continue; // Пропускаем некорректные строки
            }

            String site1 = parts[0].trim();
            String site2 = parts[1].trim();

            // Получаем или создаем индекс для первого сайта
            int index1 = getOrCreateIndex(site1, siteToIndex, sites);
            // Получаем или создаем индекс для второго сайта
            int index2 = getOrCreateIndex(site2, siteToIndex, sites);

            // Сохраняем пару связанных сайтов
            pairs.add(new int[]{index1, index2});
        }

        // Создаем DSU для объединения связанных сайтов
        // Изначально каждый сайт - отдельный кластер
        DSU dsu = new DSU(sites.size());

        // Объединяем сайты, которые связаны через гиперссылки
        for (int[] pair : pairs) {
            int site1 = pair[0];
            int site2 = pair[1];
            // Объединяем кластеры связанных сайтов
            dsu.union(site1, site2);
        }

        // Собираем размеры кластеров
        // Для каждого сайта находим корень его кластера и увеличиваем счетчик
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < sites.size(); i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Получаем размеры кластеров и сортируем по убыванию
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Выводим размеры кластеров
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }

    /**
     * Получает индекс сайта в DSU, создавая новый индекс если сайт еще не встречался
     * @param site название сайта
     * @param siteToIndex маппинг сайт -> индекс
     * @param sites список всех сайтов
     * @return индекс сайта в DSU
     */
    private static int getOrCreateIndex(String site, Map<String, Integer> siteToIndex, List<String> sites) {
        if (!siteToIndex.containsKey(site)) {
            // Создаем новый индекс для нового сайта
            int index = sites.size();
            siteToIndex.put(site, index);
            sites.add(site);
            return index;
        }
        return siteToIndex.get(site);
    }

    /**
     * Реализация DSU (Disjoint Set Union) с двумя эвристиками:
     * 1. Union by size: меньшее поддерево присоединяется к большему
     * 2. Path compression: при поиске корня все узлы становятся прямыми детьми корня
     * 
     * Сложность операций: O(α(n)) где α - обратная функция Аккермана (почти константа)
     * Общая сложность: O(N log*(N)) где log* - итерированный логарифм
     */
    static class DSU {
        private int[] parent; // parent[i] - родитель элемента i
        private int[] size;   // size[i] - размер поддерева с корнем в i

        /**
         * Конструктор DSU
         * @param n количество элементов
         */
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            // Инициализация: каждый элемент - корень своего множества
            for (int i = 0; i < n; i++) {
                parent[i] = i;      // Каждый элемент - корень
                size[i] = 1;        // Начальный размер каждого множества - 1
            }
        }

        /**
         * Находит корень множества с использованием path compression
         * Path compression: делает всех предков прямыми детьми корня
         * Это оптимизирует будущие запросы find до почти O(1)
         * @param x элемент
         * @return корень множества
         */
        int find(int x) {
            if (parent[x] != x) {
                // Path compression: рекурсивно находим корень и делаем его родителем
                // Это "сплющивает" дерево, уменьшая высоту до почти константы
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        /**
         * Объединяет два множества с использованием union by size
         * Меньшее поддерево присоединяется к большему
         * Это гарантирует, что высота дерева остается O(log n)
         * @param x первый элемент
         * @param y второй элемент
         */
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            // Если элементы уже в одном множестве, ничего не делаем
            if (rootX == rootY) {
                return;
            }

            // Эвристика по размеру: меньшее поддерево присоединяем к большему
            // Это минимизирует высоту результирующего дерева
            if (size[rootX] < size[rootY]) {
                // rootX меньше, присоединяем его к rootY
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                // rootY меньше или равен, присоединяем rootY к rootX
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }
}

