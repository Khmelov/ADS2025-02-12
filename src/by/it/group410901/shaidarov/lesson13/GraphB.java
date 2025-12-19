package by.it.group410901.shaidarov.lesson13;

import java.util.*;

public class GraphB {
    private Map<Integer, List<Integer>> adjacencyList;
    private Set<Integer> allVertices;

    public GraphB() {
        adjacencyList = new HashMap<>();
        allVertices = new HashSet<>();
    }

    // Добавление ребра в граф
    public void addEdge(int from, int to) {
        adjacencyList.putIfAbsent(from, new ArrayList<>());
        adjacencyList.get(from).add(to);
        allVertices.add(from);
        allVertices.add(to);
    }

    // Проверка наличия циклов с помощью DFS
    public boolean hasCycle() {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();

        for (int vertex : allVertices) {
            if (!visited.contains(vertex)) {
                if (hasCycleDFS(vertex, visited, recursionStack)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Рекурсивный DFS для поиска цикла
    private boolean hasCycleDFS(int vertex, Set<Integer> visited, Set<Integer> recursionStack) {
        visited.add(vertex);
        recursionStack.add(vertex);

        // Проверяем всех соседей
        if (adjacencyList.containsKey(vertex)) {
            for (int neighbor : adjacencyList.get(vertex)) {
                // Если сосед не посещен, рекурсивно проверяем его
                if (!visited.contains(neighbor)) {
                    if (hasCycleDFS(neighbor, visited, recursionStack)) {
                        return true;
                    }
                }
                // Если сосед уже в стеке рекурсии - найден цикл
                else if (recursionStack.contains(neighbor)) {
                    return true;
                }
            }
        }

        // Убираем вершину из стека рекурсии при возврате
        recursionStack.remove(vertex);
        return false;
    }

    // Парсинг строки и построение графа
    public void parseGraph(String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                int from = Integer.parseInt(parts[0].trim());
                int to = Integer.parseInt(parts[1].trim());
                addEdge(from, to);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Считываем строку структуры орграфа
        String input = scanner.nextLine();

        GraphB graph = new GraphB();
        graph.parseGraph(input);

        // Проверяем наличие циклов
        boolean hasCycle = graph.hasCycle();

        // Выводим результат
        System.out.println(hasCycle ? "yes" : "no");

        scanner.close();
    }
}