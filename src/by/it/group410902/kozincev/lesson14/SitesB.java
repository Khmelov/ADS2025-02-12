package by.it.group410902.kozincev.lesson14;

import java.util.*;

public class SitesB {


    private static class DSU {
        private final int[] parent;
        private final int[] rank; // или size
        private int count; // Количество множеств

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            count = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Изначально каждый элемент - корень своего множества
                rank[i] = 1;   // Изначально ранг (или размер) каждого множества равен 1
            }
        }

        // Операция Find с сжатием путей
        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]); // Сжатие пути
            }
            return parent[i];
        }

        // Операция Union по рангу (или размеру)
        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                // Объединение по рангу (или размеру)
                if (rank[rootI] < rank[rootJ]) {
                    parent[rootI] = rootJ;
                } else if (rank[rootI] > rank[rootJ]) {
                    parent[rootJ] = rootI;
                } else {
                    parent[rootJ] = rootI;
                    rank[rootI]++; // Увеличение ранга, если ранги одинаковы
                }
                count--; // Уменьшаем количество множеств
                return true;
            }
            return false;
        }

        // Возвращает количество непересекающихся множеств
        public int getCount() {
            return count;
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Сбор и индексация всех уникальных сайтов
        // Используем Map для сопоставления имени сайта его индексу в DSU
        Map<String, Integer> siteToIndex = new HashMap<>();
        // Список пар для последующего Union
        List<String[]> pairs = new ArrayList<>();
        int nextIndex = 0;

        // Чтение ввода до строки "end"
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("end")) {
                break;
            }

            // Разделение пары (например, "java.mobile+science.org")
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                // Добавляем пару в список для обработки
                pairs.add(sites);

                // Индексация сайтов
                for (String site : sites) {
                    if (!siteToIndex.containsKey(site)) {
                        siteToIndex.put(site, nextIndex++);
                    }
                }
            }
        }
        scanner.close();

        // N - общее количество уникальных сайтов
        int N = siteToIndex.size();
        if (N == 0) {
            System.out.println();
            return;
        }

        // 2. Инициализация DSU
        DSU dsu = new DSU(N);

        // 3. Объединение связанных сайтов
        for (String[] pair : pairs) {
            String site1 = pair[0];
            String site2 = pair[1];

            // Получаем индексы
            Integer index1 = siteToIndex.get(site1);
            Integer index2 = siteToIndex.get(site2);

            // Выполняем объединение
            if (index1 != null && index2 != null) {
                dsu.union(index1, index2);
            }
        }

        // 4. Сбор и вывод размеров кластеров
        // Создаем карту для подсчета размера каждого кластера
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i); // Находим корень кластера
            // Увеличиваем счетчик для этого корня (кластера)
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // 5. Вывод: размеры кластеров в порядке убывания (для прохождения теста)
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());

        // Сортировка по убыванию
        sizes.sort(Comparator.reverseOrder());

        // Вывод через пробел
        StringJoiner sj = new StringJoiner(" ");
        for (int size : sizes) {
            sj.add(String.valueOf(size));
        }
        System.out.println(sj.toString());
    }
}