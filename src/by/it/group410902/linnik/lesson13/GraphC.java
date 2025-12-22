package by.it.group410902.linnik.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>(); //вершины в которые можно попасть
        Map<String, List<String>> reverseGraph = new HashMap<>(); //вершины из которых можно попасть
        Set<String> allVertices = new HashSet<>(); //все вершины

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            reverseGraph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.get(to).add(from);

            allVertices.add(from); //сохраняем вершины в общее множество
            allVertices.add(to);
        }

        //обход в глубину на прямом графе
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack); //поиск в глубину если не посещали
            }
        }

        //обход в глубину на обратном графе
        visited.clear();
        List<List<String>> components = new ArrayList<>(); //компоненты сильной связности

        while (!stack.isEmpty()) {
            String vertex = stack.pop(); //извлекаем из стека, начинаем с вершины которую записали последней - LIFO
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>(); //если не посещали создаем новый компонент сильной связности
                dfsSecondPass(vertex, reverseGraph, visited, component); //поиск в глубину
                components.add(component);
            }
        }

        for (List<String> component : components) {
            Collections.sort(component);
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }
    }

    //обходим прямой граф и заполняем стек. идем в глубину пока можно
    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);
        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsFirstPass(neighbor, graph, visited, stack);
                }
            }
        }
        stack.push(vertex);
    }

    //обходим обратный граф: идем пока не дойдем до вершины все соседи которой уже есть в компоненте
    private static void dfsSecondPass(String vertex, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);
        if (reverseGraph.containsKey(vertex)) { //если есть вершины из которых можно попасть
            for (String neighbor : reverseGraph.get(vertex)) { //то обходим их
                if (!visited.contains(neighbor)) {
                    dfsSecondPass(neighbor, reverseGraph, visited, component);
                }
            }
        }
    }
}