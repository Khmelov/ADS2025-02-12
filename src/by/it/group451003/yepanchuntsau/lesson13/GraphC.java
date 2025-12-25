package by.it.group451003.yepanchuntsau.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }

        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            return;
        }

        // Граф, обратный граф и множество вершин
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverse = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        // Парсим строку вида: C->B, C->I, I->A, ...
        String[] parts = line.split(",");
        for (String rawPart : parts) {
            String part = rawPart.trim();
            if (part.isEmpty()) continue;

            // убираем все пробелы: "C -> B" -> "C->B"
            part = part.replaceAll("\\s+", "");

            String[] uv = part.split("->");
            if (uv.length != 2) continue;

            String from = uv[0];
            String to = uv[1];

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());

            reverse.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            reverse.computeIfAbsent(from, k -> new ArrayList<>());
        }

        // На всякий случай убеждаемся, что все вершины есть в обоих графах
        for (String v : vertices) {
            graph.putIfAbsent(v, new ArrayList<>());
            reverse.putIfAbsent(v, new ArrayList<>());
        }

        // ---------- 1-й проход DFS: считаем порядок выхода ----------
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        List<String> vertsSorted = new ArrayList<>(vertices);
        Collections.sort(vertsSorted); // чтобы результат был детерминированным

        for (String v : vertsSorted) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }

        // ---------- 2-й проход DFS по обратному графу ----------
        Collections.reverse(order);
        visited.clear();
        List<List<String>> components = new ArrayList<>();

        for (String v : order) {
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, reverse, visited, comp);
                Collections.sort(comp); // вершины в компоненте по алфавиту
                components.add(comp);
            }
        }

        int compCount = components.size();

        // ---------- сопоставляем вершину номеру компоненты ----------
        Map<String, Integer> compIndex = new HashMap<>();
        for (int i = 0; i < compCount; i++) {
            for (String v : components.get(i)) {
                compIndex.put(v, i);
            }
        }

        // ---------- конденсация графа (граф компонент) ----------
        List<List<Integer>> compAdj = new ArrayList<>();
        int[] indeg = new int[compCount];

        for (int i = 0; i < compCount; i++) {
            compAdj.add(new ArrayList<>());
        }

        @SuppressWarnings("unchecked")
        Set<Integer>[] edgeSet = new HashSet[compCount];
        for (int i = 0; i < compCount; i++) {
            edgeSet[i] = new HashSet<>();
        }

        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            int cFrom = compIndex.get(from);
            for (String to : entry.getValue()) {
                int cTo = compIndex.get(to);
                if (cFrom != cTo && edgeSet[cFrom].add(cTo)) {
                    compAdj.get(cFrom).add(cTo);
                    indeg[cTo]++; // считаем входы в компоненты
                }
            }
        }

        // ---------- топологическая сортировка компонент ----------
        // берём компоненты с indeg==0, при равенстве — по алфавиту (по строке компоненты)
        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> {
            String sa = String.join("", components.get(a));
            String sb = String.join("", components.get(b));
            return sa.compareTo(sb);
        });

        int[] indegCopy = Arrays.copyOf(indeg, compCount);
        for (int i = 0; i < compCount; i++) {
            if (indegCopy[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> compOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int c = queue.poll();
            compOrder.add(c);
            for (int to : compAdj.get(c)) {
                indegCopy[to]--;
                if (indegCopy[to] == 0) {
                    queue.add(to);
                }
            }
        }

        // ---------- вывод ----------
        for (int i = 0; i < compOrder.size(); i++) {
            int idx = compOrder.get(i);
            String lineOut = String.join("", components.get(idx));
            System.out.print(lineOut);
            if (i + 1 < compOrder.size()) {
                System.out.println();
            }
        }
    }

    private static void dfs1(String v,
                             Map<String, List<String>> g,
                             Set<String> visited,
                             List<String> order) {
        visited.add(v);
        for (String to : g.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(to)) {
                dfs1(to, g, visited, order);
            }
        }
        order.add(v);
    }

    private static void dfs2(String v,
                             Map<String, List<String>> gRev,
                             Set<String> visited,
                             List<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String to : gRev.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(to)) {
                dfs2(to, gRev, visited, comp);
            }
        }
    }
}
