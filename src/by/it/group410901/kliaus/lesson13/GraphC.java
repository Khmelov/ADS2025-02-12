package by.it.group410901.kliaus.lesson13;

import java.util.*;

public class GraphC {

    private static Map<String, List<String>> graph;
    private static Map<String, List<String>> reverseGraph;
    private static Set<String> visited;
    private static Stack<String> stack;
    private static List<List<String>> components;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        graph = new HashMap<>();
        reverseGraph = new HashMap<>();
        visited = new HashSet<>();
        stack = new Stack<>();
        components = new ArrayList<>();

        Set<String> allNodes = new HashSet<>();

        // Парсинг входных данных
        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            allNodes.add(from);
            allNodes.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            reverseGraph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.get(to).add(from);
        }

        // Инициализация пустых списков для вершин без исходящих/входящих рёбер
        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
            reverseGraph.putIfAbsent(node, new ArrayList<>());
        }

        // Алгоритм Косарайю
        // Шаг 1: Первый DFS для заполнения стека
        for (String node : allNodes) {
            if (!visited.contains(node)) {
                dfs1(node);
            }
        }

        // Шаг 2: Второй DFS на транспонированном графе
        visited.clear();
        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfs2(node, component);
                Collections.sort(component);
                components.add(component);
            }
        }

        // Вывод результата (компоненты уже в правильном порядке - обратном топологическом)
        for (int i = 0; i < components.size(); i++) {
            System.out.println(String.join("", components.get(i)));
        }
    }

    // Первый DFS - заполнение стека
    private static void dfs1(String node) {
        visited.add(node);

        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor);
            }
        }

        stack.push(node);
    }

    // Второй DFS - поиск компоненты
    private static void dfs2(String node, List<String> component) {
        visited.add(node);
        component.add(node);

        for (String neighbor : reverseGraph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, component);
            }
        }
    }
}