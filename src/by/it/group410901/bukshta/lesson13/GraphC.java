package by.it.group410901.bukshta.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // граф и его обратный граф
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();

        // строим графы
        String[] parts = input.split(", ");
        for (String part : parts) {
            if (part.contains("->")) {
                String[] edge = part.split("->");
                String from = edge[0].trim();
                String to = edge[1].trim();
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
                // добавляем изолированные вершины
                graph.putIfAbsent(to, new ArrayList<>());
                reverseGraph.putIfAbsent(from, new ArrayList<>());
            } else {
                String vertex = part.trim();
                graph.putIfAbsent(vertex, new ArrayList<>());
                reverseGraph.putIfAbsent(vertex, new ArrayList<>());
            }
        }

        // находим сильно связанные компоненты
        List<List<String>> scc = kosaraju(graph, reverseGraph);

        // проверка, есть ли цикл: если все компоненты размером 1 — граф ацикличен
        boolean isAcyclic = scc.stream().allMatch(component -> component.size() == 1);

        // сортируем компоненты по первой вершине, если граф ацикличен
        if (isAcyclic) {
            scc.sort((c1, c2) -> c1.get(0).compareTo(c2.get(0)));
        }

        // выводим компоненты
        for (List<String> component : scc) {
            Collections.sort(component); // сортировка вершин внутри компоненты
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }
    }

    // метод Косарайю: возвращает список SCC
    private static List<List<String>> kosaraju(Map<String, List<String>> graph,
                                               Map<String, List<String>> reverseGraph) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        // первый проход DFS по исходному графу, заполняем стек
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                dfsFirstPass(node, graph, visited, stack);
            }
        }

        visited.clear();
        List<List<String>> scc = new ArrayList<>();

        // второй проход DFS по обратному графу
        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(node, reverseGraph, visited, component);
                scc.add(component); // добавляем найденную SCC
            }
        }

        return scc;
    }

    // первый DFS проход: заполняем стек по завершении обхода вершины
    private static void dfsFirstPass(String current, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(current);
        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    dfsFirstPass(neighbor, graph, visited, stack);
                }
            }
        }
        stack.push(current); // вершина обработана, добавляем в стек
    }

    // второй DFS проход по обратному графу: формируем SCC
    private static void dfsSecondPass(String current, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(current);
        component.add(current); // добавляем вершину в текущую компоненту
        if (reverseGraph.containsKey(current)) {
            for (String neighbor : reverseGraph.get(current)) {
                if (!visited.contains(neighbor)) {
                    dfsSecondPass(neighbor, reverseGraph, visited, component);
                }
            }
        }
    }
}
