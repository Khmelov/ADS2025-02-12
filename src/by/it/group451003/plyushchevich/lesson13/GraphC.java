package by.it.group451003.plyushchevich.lesson13;

import java.util.*;

/**
 * 1) читает строку с описанием орграфа вида "A->B, C->D, ..." (пробелы допустимы),
 * 2) строит oriented graph (adjacency list),
 * 3) находит компоненты сильной связности (SCC),
 * 4) выводит каждую SCC в отдельной строке, вершины в SCC — в лексикографическом порядке,
 *    а сами SCC упорядочены по порядку первой встречи их вершин во входной строке.
 */
public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            scanner.close();
            return;
        }
        String line = scanner.nextLine();
        scanner.close();

        GraphC graph = new GraphC();

        // Находим компоненты сильной связности
        List<List<String>> scc = graph.findStronglyConnectedComponents(line);

        for (List<String> comp : scc) {
            for (String v : comp) {
                System.out.print(v);
            }
            System.out.println();
        }
    }


    public List<List<String>> findStronglyConnectedComponents(String input) {
        Map<String, Set<String>> adj = new LinkedHashMap<>();
        Map<String, Integer> firstAppearance = new LinkedHashMap<>();
        parseInput(input, adj, firstAppearance);

        // Если граф пустой — вернём пустой список
        if (adj.isEmpty()) return new ArrayList<>();


        Set<String> visited = new HashSet<>();
        // finishOrder — стек (deque), в который кладём вершины в порядке post-order (после рекурсивного обхода).
        Deque<String> finishOrder = new ArrayDeque<>();
        for (String node : adj.keySet()) {
            if (!visited.contains(node)) {
                dfs1(node, adj, visited, finishOrder);
            }
        }

        Map<String, Set<String>> revAdj = reverseGraph(adj);

        // обход в порядке убывания finish time (берём finishOrder с конца)
        visited.clear();
        List<List<String>> components = new ArrayList<>();
        while (!finishOrder.isEmpty()) {
            String node = finishOrder.pollLast(); // вершина с наибольшим finish-time
            if (!visited.contains(node)) {
                List<String> comp = new ArrayList<>();
                dfs2(node, revAdj, visited, comp);
                comp.sort(Comparator.naturalOrder());
                components.add(comp);
            }
        }

        components.sort(Comparator.comparingInt(c -> firstAppearance.get(c.get(0))));

        return components;
    }


    private void parseInput(String input, Map<String, Set<String>> adj, Map<String, Integer> firstAppearance) {
        String[] parts = input.split(",");
        int index = 0;

        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;

            String[] edge = part.split("->");
            if (edge.length != 2) continue;

            String from = edge[0].trim();
            String to = edge[1].trim();
            if (from.isEmpty() || to.isEmpty()) continue;

            adj.computeIfAbsent(from, k -> new LinkedHashSet<>()).add(to);
            // Также добавляем ключ для 'to' (если 'to' встречается только как цель, у него будет пустой set)
            adj.putIfAbsent(to, new LinkedHashSet<>());

            if (!firstAppearance.containsKey(from)) firstAppearance.put(from, index++);
            if (!firstAppearance.containsKey(to)) firstAppearance.put(to, index++);
        }
    }

    private void dfs1(String node, Map<String, Set<String>> adj, Set<String> visited, Deque<String> finishOrder) {
        visited.add(node);
        // Проходим по всем смежным вершинам (исходящие рёбра)
        for (String to : adj.getOrDefault(node, Collections.emptySet())) {
            if (!visited.contains(to)) {
                dfs1(to, adj, visited, finishOrder);
            }
        }
        finishOrder.addLast(node);
    }

    private void dfs2(String node, Map<String, Set<String>> revAdj, Set<String> visited, List<String> comp) {
        visited.add(node);
        comp.add(node);
        for (String to : revAdj.getOrDefault(node, Collections.emptySet())) {
            if (!visited.contains(to)) {
                dfs2(to, revAdj, visited, comp);
            }
        }
    }


    private Map<String, Set<String>> reverseGraph(Map<String, Set<String>> adj) {
        Map<String, Set<String>> rev = new LinkedHashMap<>();
        // Инициализируем ключи, чтобы у каждой вершины был набор соседей (возможно пустой)
        for (String node : adj.keySet()) {
            rev.putIfAbsent(node, new LinkedHashSet<>());
        }
        // Пройдём по всем рёбрам и инвертируем их
        for (Map.Entry<String, Set<String>> entry : adj.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                // Добавляем обратное ребро: to -> from
                rev.get(to).add(from);
            }
        }
        return rev;
    }
}
