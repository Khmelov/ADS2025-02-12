package by.it.group410901.korneew.lesson14;

import java.util.*;

public class SitesB {

    // DSU с эвристикой по размеру и сжатием путей
    static class DSU {
        private int[] parent;  // массив родителей
        private int[] size;    // массив размеров компонент

        // Конструктор - создает DSU для n элементов
        public DSU(int n) {
            parent = new int[n];
            size = new int[n];

            // Инициализация: каждый элемент - корень своего дерева
            for (int i = 0; i < n; i++) {
                parent[i] = i;  // родитель - сам элемент
                size[i] = 1;    // начальный размер = 1
            }
        }

        // Поиск корня элемента со сжатием путей
        public int find(int x) {
            if (parent[x] != x) {
                // Рекурсивно ищем корень и "спрямляем" путь
                parent[x] = find(parent[x]);  // СЖАТИЕ ПУТИ
            }
            return parent[x];
        }

        // Объединение двух элементов с эвристикой по размеру
        public void union(int x, int y) {
            int rootX = find(x); // находим корень первого элемента
            int rootY = find(y); // находим корень второго элемента

            // Если корни одинаковые - элементы уже в одном множестве
            if (rootX == rootY) return;

            // ЭВРИСТИКА ПО РАЗМЕРУ: меньшее дерево присоединяем к большему
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;           // корень X теперь указывает на корень Y
                size[rootY] += size[rootX];      // увеличиваем размер Y
            } else {
                parent[rootY] = rootX;           // корень Y теперь указывает на корень X
                size[rootX] += size[rootY];      // увеличиваем размер X
            }
        }

        // Получение размера компоненты, содержащей элемент x
        public int getSize(int x) {
            return size[find(x)]; // размер корня = размер всей компоненты
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Map для связи имени сайта с индексом
        Map<String, Integer> siteToIndex = new HashMap<>();
        List<String> indexToSite = new ArrayList<>();
        List<String[]> connections = new ArrayList<>();

        System.out.println("Введите связи между сайтами (формат: site1+site2):");
        System.out.println("Для завершения ввода введите 'end'");

        // Чтение входных данных до строки "end"
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }

            // Разделение строки на два сайта по знаку '+'
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();
                connections.add(new String[]{site1, site2});

                // Добавление сайтов в mapping, если их еще нет
                if (!siteToIndex.containsKey(site1)) {
                    siteToIndex.put(site1, indexToSite.size());
                    indexToSite.add(site1);
                }
                if (!siteToIndex.containsKey(site2)) {
                    siteToIndex.put(site2, indexToSite.size());
                    indexToSite.add(site2);
                }
            }
        }

        // Общее количество уникальных сайтов
        int totalSites = indexToSite.size();

        // Инициализация DSU
        DSU dsu = new DSU(totalSites);

        System.out.println("\nОбработка связей...");

        // Обработка всех соединений между сайтами
        for (String[] connection : connections) {
            String site1 = connection[0];
            String site2 = connection[1];

            int index1 = siteToIndex.get(site1);
            int index2 = siteToIndex.get(site2);

            System.out.println("Объединяем: " + site1 + " + " + site2);

            // Объединение сайтов в один кластер
            dsu.union(index1, index2);
        }

        // Сбор размеров кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < totalSites; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.getSize(root));
        }

        // Извлечение и сортировка размеров кластеров по убыванию
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Вывод результатов
        System.out.println("\nРезультат - размеры кластеров сайтов:");
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}