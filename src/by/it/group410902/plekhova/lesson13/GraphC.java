package by.it.group410902.plekhova.lesson13;

import java.util.*;
// найти компоненты сильной связности
public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) { sc.close(); return; }
        String line = sc.nextLine().trim();
        sc.close();
        if (line.isEmpty()) return;

        // 1) Парсинг входа и сбор всех вершин
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverse = new HashMap<>();
        LinkedHashSet<String> vertexOrder = new LinkedHashSet<>(); // сохраняет порядок появления вершины в строке

        String[] edges = line.split(",");
        for (String e : edges) {
            String[] parts = e.trim().split("\\s*->\\s*");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();
            if (from.isEmpty() || to.isEmpty()) continue;

            vertexOrder.add(from);
            vertexOrder.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>());

            reverse.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            reverse.putIfAbsent(from, new ArrayList<>());
        }

        // Сортировка списков смежности для детерминизма
        for (List<String> lst : graph.values()) Collections.sort(lst);
        for (List<String> lst : reverse.values()) Collections.sort(lst);

        // 2) Используем алгоритм Косарайю: порядок завершения (DFS1)
        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();

        // используем deterministic order: сначала вершины в порядке появления в input,
        // затем любые недостающие
        List<String> allVertices = new ArrayList<>(vertexOrder);
        Collections.sort(allVertices); // для стабилизации, но основной порядок — appearance

        Set<String> seen = new HashSet<>(vertexOrder);
        for (String v : vertexOrder) {
            if (!visited.contains(v)) dfs1(v, graph, visited, order);
        }
        // на случай, если в graph есть вершины не встречающиеся в vertexOrder
        for (String v : graph.keySet()) {
            if (!visited.contains(v)) dfs1(v, graph, visited, order);
        }

        // 3) извлечения компонент на обратном графе
        visited.clear();
        List<List<String>> comps = new ArrayList<>();
        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, reverse, visited, comp);
                Collections.sort(comp); // внутри компоненты — лексикографический порядок
                comps.add(comp);
            }
        }

        // 4) Строим отображение вершина -> id компоненты
        Map<String, Integer> compId = new HashMap<>();
        for (int i = 0; i < comps.size(); i++) {
            for (String node : comps.get(i)) compId.put(node, i);
        }

        // 5) убираем петли и дубликаты.
        int k = comps.size();
        List<Set<Integer>> sccAdj = new ArrayList<>(k);
        for (int i = 0; i < k; i++) sccAdj.add(new HashSet<>());

        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String u = entry.getKey();
            int cu = compId.get(u);
            for (String w : entry.getValue()) {
                int cv = compId.get(w);
                if (cu != cv) sccAdj.get(cu).add(cv);
            }
        }

        // 6) Вычислим входящие степени для компонент
        int[] indeg = new int[k];
        for (int i = 0; i < k; i++) {
            for (int to : sccAdj.get(i)) indeg[to]++;
        }

        // 7) задаём для каждой компоненты её минимальную вершину (лексикографически)
        String[] minVertex = new String[k];
        for (int i = 0; i < k; i++) {
            List<String> comp = comps.get(i);
            if (comp.isEmpty()) minVertex[i] = ""; else {
                // comp уже отсортирован лексикографически, поэтому первый — минимальный
                minVertex[i] = comp.get(0);
            }
        }

        // 8) Kahn с приоритетом: когда несколько компонент indeg==0, обрабатывать ту, у которой minVertex меньше лексикографически.
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(a -> 0)); // placeholder

        pq = new PriorityQueue<>((a, b) -> minVertex[a].compareTo(minVertex[b]));

        for (int i = 0; i < k; i++) if (indeg[i] == 0) pq.add(i);

        List<Integer> topo = new ArrayList<>();
        while (!pq.isEmpty()) {
            int u = pq.poll();
            topo.add(u);
            for (int v : sccAdj.get(u)) {
                indeg[v]--;
                if (indeg[v] == 0) pq.add(v);
            }
        }

        // 9) Вывод: в порядке topo, каждая компонента — свёрнутая строка из её вершин
        for (int id : topo) {
            StringBuilder sb = new StringBuilder();
            for (String node : comps.get(id)) sb.append(node);
            System.out.println(sb.toString());
        }
    }

    // DFS 1 — собираем порядок завершения
    private static void dfs1(String v, Map<String, List<String>> g, Set<String> visited, Deque<String> order) {
        if (visited.contains(v)) return;
        visited.add(v);
        for (String to : g.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(to)) dfs1(to, g, visited, order);
        }
        order.push(v);
    }

    // DFS 2 — собираем component на обратном графе
    private static void dfs2(String v, Map<String, List<String>> g, Set<String> visited, List<String> comp) {
        if (visited.contains(v)) return;
        visited.add(v);
        comp.add(v);
        for (String to : g.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(to)) dfs2(to, g, visited, comp);
        }
    }
}

