package by.it.group410901.shaidarov.lesson13;

import java.util.*;

public class GraphC {
    private Map<String, List<String>> adjacencyList;
    private Map<String, List<String>> reverseAdjacencyList;
    private Set<String> allVertices;

    public GraphC() {
        adjacencyList = new HashMap<>();
        reverseAdjacencyList = new HashMap<>();
        allVertices = new HashSet<>();
    }

    // Добавление ребра в граф
    public void addEdge(String from, String to) {
        adjacencyList.putIfAbsent(from, new ArrayList<>());
        adjacencyList.get(from).add(to);

        reverseAdjacencyList.putIfAbsent(to, new ArrayList<>());
        reverseAdjacencyList.get(to).add(from);

        allVertices.add(from);
        allVertices.add(to);
    }

    // Алгоритм Косарайю для поиска компонент сильной связности
    public List<List<String>> findSCC() {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        // Шаг 1: Заполняем стек в порядке завершения DFS
        List<String> sortedVertices = new ArrayList<>(allVertices);
        Collections.sort(sortedVertices);

        for (String vertex : sortedVertices) {
            if (!visited.contains(vertex)) {
                fillOrder(vertex, visited, stack);
            }
        }

        // Шаг 2: Выполняем DFS на транспонированном графе
        visited.clear();
        List<List<String>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsReverse(vertex, visited, component);
                Collections.sort(component);
                components.add(component);
            }
        }

        return components;
    }

    // DFS для заполнения стека
    private void fillOrder(String vertex, Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        if (adjacencyList.containsKey(vertex)) {
            List<String> neighbors = new ArrayList<>(adjacencyList.get(vertex));
            Collections.sort(neighbors);
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    fillOrder(neighbor, visited, stack);
                }
            }
        }

        stack.push(vertex);
    }

    // DFS на транспонированном графе
    private void dfsReverse(String vertex, Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        if (reverseAdjacencyList.containsKey(vertex)) {
            List<String> neighbors = new ArrayList<>(reverseAdjacencyList.get(vertex));
            Collections.sort(neighbors);
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfsReverse(neighbor, visited, component);
                }
            }
        }
    }

    // Определение истоков и стоков
    public List<List<String>> sortComponentsByTopology(List<List<String>> components) {
        // Создаем граф компонент
        Map<Integer, Set<Integer>> componentGraph = new HashMap<>();
        Map<String, Integer> vertexToComponent = new HashMap<>();

        // Маппинг вершин к компонентам
        for (int i = 0; i < components.size(); i++) {
            for (String vertex : components.get(i)) {
                vertexToComponent.put(vertex, i);
            }
            componentGraph.put(i, new HashSet<>());
        }

        // Строим граф компонент
        for (String from : adjacencyList.keySet()) {
            int fromComp = vertexToComponent.get(from);
            for (String to : adjacencyList.get(from)) {
                int toComp = vertexToComponent.get(to);
                if (fromComp != toComp) {
                    componentGraph.get(fromComp).add(toComp);
                }
            }
        }

        // Топологическая сортировка компонент
        Map<Integer, Integer> inDegree = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            inDegree.put(i, 0);
        }

        for (int comp : componentGraph.keySet()) {
            for (int neighbor : componentGraph.get(comp)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> {
            String minA = components.get(a).get(0);
            String minB = components.get(b).get(0);
            return minA.compareTo(minB);
        });

        for (int i = 0; i < components.size(); i++) {
            if (inDegree.get(i) == 0) {
                queue.offer(i);
            }
        }

        List<List<String>> sorted = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            sorted.add(components.get(current));

            List<Integer> neighbors = new ArrayList<>(componentGraph.get(current));
            for (int neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return sorted;
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
                String from = parts[0].trim();
                String to = parts[1].trim();
                addEdge(from, to);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Считываем строку структуры орграфа
        String input = scanner.nextLine();

        GraphC graph = new GraphC();
        graph.parseGraph(input);

        // Находим компоненты сильной связности
        List<List<String>> components = graph.findSCC();

        // Сортируем компоненты по топологии
        List<List<String>> sortedComponents = graph.sortComponentsByTopology(components);

        // Выводим результат
        for (List<String> component : sortedComponents) {
            for (String vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
        }

        scanner.close();
    }
}