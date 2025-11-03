package by.it.group451001.serganovskij.lesson14;

import java.util.*;

/**
 * Кластеризация связанных сайтов через гиперссылки с использованием DSU.
 * Каждая пара "A+B" означает, что сайты A и B взаимосвязаны и должны быть в одном кластере.
 * Ввод продолжается до строки "end".
 * Выводятся размеры всех кластеров в порядке возрастания.
 */
public class SitesB {

    /** DSU (Union-Find) с эвристикой по размеру и сжатием пути */
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        /** Находим корень с сжатием пути */
        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        /** Объединяем множества по размеру */
        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a != b) {
                if (size[a] < size[b]) {
                    int t = a;
                    a = b;
                    b = t;
                }
                parent[b] = a;
                size[a] += size[b];
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Сопоставляем названия сайтов с индексами
        Map<String, Integer> idMap = new HashMap<>();
        List<String[]> pairs = new ArrayList<>();

        String line;
        int idx = 0;

        // Чтение входа до слова "end"
        while (!(line = sc.nextLine()).equals("end")) {
            String[] parts = line.split("\\+");
            String a = parts[0];
            String b = parts[1];

            if (!idMap.containsKey(a)) idMap.put(a, idx++);
            if (!idMap.containsKey(b)) idMap.put(b, idx++);

            pairs.add(new String[]{a, b});
        }

        DSU dsu = new DSU(idx);

        // Объединяем связанные сайты
        for (String[] p : pairs) {
            int u = idMap.get(p[0]);
            int v = idMap.get(p[1]);
            dsu.union(u, v);
        }

        // Подсчитываем размеры кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < idx; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Сортируем размеры по возрастанию
        List<Integer> result = new ArrayList<>(clusterSizes.values());
        result.sort(Comparator.reverseOrder());

        // Формируем вывод без лишнего пробела
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            sb.append(result.get(i));
            if (i + 1 < result.size()) sb.append(" ");
        }
        System.out.println(sb);
    }
}
