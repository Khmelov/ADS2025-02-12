package by.it.group410902.harkavy.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();

        // список смежности прямого графа
        Map<String, List<String>> g = new HashMap<>();
        // список смежности обратного графа
        Map<String, List<String>> rg = new HashMap<>();
        // все вершины
        Set<String> vertices = new HashSet<>();

        if (!line.isEmpty()) {
            String[] parts = line.split(",");
            for (String part : parts) {
                String edge = part.trim();
                if (edge.isEmpty()) continue;

                String[] uv = edge.split("->");
                if (uv.length != 2) continue;

                String u = uv[0].trim();
                String v = uv[1].trim();

                vertices.add(u);
                vertices.add(v);

                // добавляем ребро u -> v
                g.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
                // в обратном графе добавляем ребро v -> u
                rg.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
            }
        }

        // шаг 1: порядок обхода вершин (первый DFS)
        List<String> order = new ArrayList<>();
        Set<String> used = new HashSet<>();

        // чтобы порядок был детерминированным, обходим вершины по алфавиту
        List<String> all = new ArrayList<>(vertices);
        Collections.sort(all);

        for (String v : all) {
            if (!used.contains(v)) {
                dfs1(v, g, used, order);
            }
        }

        // шаг 2: поиск КСС на обратном графе в порядке убывания времени выхода
        Collections.reverse(order);
        Map<String, Integer> compId = new HashMap<>();
        List<List<String>> components = new ArrayList<>();
        used.clear();

        for (String v : order) {
            if (!used.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, rg, used, comp, compId, components.size());
                // сортируем вершины компоненты по алфавиту
                Collections.sort(comp);
                components.add(comp);
            }
        }

        int compCount = components.size();

        // строим конденсацию графа: граф КСС
        boolean[][] compEdges = new boolean[compCount][compCount];
        int[] indeg = new int[compCount];

        for (Map.Entry<String, List<String>> e : g.entrySet()) {
            String u = e.getKey();
            int cu = compId.get(u);
            for (String v : e.getValue()) {
                int cv = compId.get(v);
                if (cu != cv && !compEdges[cu][cv]) {
                    compEdges[cu][cv] = true;
                    indeg[cv]++;
                }
            }
        }

        // ключ компоненты для сортировки (строка вершин без пробелов)
        String[] compKey = new String[compCount];
        for (int i = 0; i < compCount; i++) {
            StringBuilder sb = new StringBuilder();
            for (String v : components.get(i)) sb.append(v);
            compKey[i] = sb.toString();
        }

        // топологическая сортировка КСС по их связям
        PriorityQueue<Integer> q = new PriorityQueue<>(
                (i, j) -> compKey[i].compareTo(compKey[j])
        );

        for (int i = 0; i < compCount; i++) {
            if (indeg[i] == 0) {
                q.add(i);
            }
        }

        List<Integer> topoComps = new ArrayList<>();
        while (!q.isEmpty()) {
            int c = q.poll();
            topoComps.add(c);
            for (int to = 0; to < compCount; to++) {
                if (compEdges[c][to]) {
                    indeg[to]--;
                    if (indeg[to] == 0) {
                        q.add(to);
                    }
                }
            }
        }

        // вывод: каждая компонента с новой строки, вершины подряд
        for (int idx = 0; idx < topoComps.size(); idx++) {
            int c = topoComps.get(idx);
            List<String> comp = components.get(c);
            StringBuilder sb = new StringBuilder();
            for (String v : comp) sb.append(v);
            System.out.println(sb.toString());
        }
    }

    // первый DFS: считаем порядок выхода вершин
    private static void dfs1(String v,
                             Map<String, List<String>> g,
                             Set<String> used,
                             List<String> order) {
        if (used.contains(v)) return;
        used.add(v);
        List<String> next = g.get(v);
        if (next != null) {
            for (String u : next) {
                dfs1(u, g, used, order);
            }
        }
        order.add(v);
    }

    // второй DFS по обратному графу: собираем вершины компоненты
    private static void dfs2(String v,
                             Map<String, List<String>> rg,
                             Set<String> used,
                             List<String> comp,
                             Map<String, Integer> compId,
                             int currentComp) {
        if (used.contains(v)) return;
        used.add(v);
        comp.add(v);
        compId.put(v, currentComp);
        List<String> next = rg.get(v);
        if (next != null) {
            for (String u : next) {
                dfs2(u, rg, used, comp, compId, currentComp);
            }
        }
    }
}
