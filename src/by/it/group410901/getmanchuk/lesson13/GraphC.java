package by.it.group410901.getmanchuk.lesson13;

import java.util.*;

// Нахождение сильно связанных компонент графа (из любой вершины можно добраться до любой)

public class GraphC {

    public static void main(String[] args) {

        // Чтение входной строки с рёбрами
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Списки смежности для прямого и обратного графов
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();

        // Множество всех вершин
        Set<String> vertices = new HashSet<>();

        // Разделение входных данных на отдельные рёбра
        String[] edges = input.split(", ");

        // Построение прямого и обратного графа
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        }

        // Получение списка сильносвязанных компонент с помощью алгоритма Косараджу
        List<Set<String>> scc = kosaraju(graph, reverseGraph, vertices);

        // Сортировка вершин внутри каждой компоненты по алфавиту
        List<List<String>> sortedComponents = new ArrayList<>();
        for (Set<String> component : scc) {
            List<String> sortedComponent = new ArrayList<>(component);
            Collections.sort(sortedComponent);
            sortedComponents.add(sortedComponent);
        }

        // Подготовка структур для подсчёта входящих и исходящих рёбер между компонентами
        Map<List<String>, Integer> componentInDegree = new HashMap<>();
        Map<List<String>, Integer> componentOutDegree = new HashMap<>();

        // Инициализация степеней компонент
        for (List<String> comp : sortedComponents) {
            componentInDegree.put(comp, 0);
            componentOutDegree.put(comp, 0);
        }

        // Подсчёт рёбер между различными компонентами
        for (List<String> compFrom : sortedComponents) {
            for (String vertexFrom : compFrom) {
                if (graph.containsKey(vertexFrom)) {
                    for (String neighbor : graph.get(vertexFrom)) {
                        for (List<String> compTo : sortedComponents) {
                            if (compTo.contains(neighbor) && compFrom != compTo) {
                                componentOutDegree.put(compFrom, componentOutDegree.get(compFrom) + 1);
                                componentInDegree.put(compTo, componentInDegree.get(compTo) + 1);
                            }
                        }
                    }
                }
            }
        }

        // Сортировка компонент: сначала по входящей степени, затем лексикографически
        sortedComponents.sort((a, b) -> {
            int inA = componentInDegree.get(a);
            int inB = componentInDegree.get(b);
            if (inA != inB)
                return Integer.compare(inA, inB);
            return a.get(0).compareTo(b.get(0));
        });

        // Вывод компонент — каждая компонента в отдельной строке
        for (List<String> component : sortedComponents) {
            for (String vertex : component)
                System.out.print(vertex);
            System.out.println();
        }
    }

    // Алгоритм Косараджу для поиска сильносвязанных компонент графа
    private static List<Set<String>> kosaraju(Map<String, List<String>> graph,
                                              Map<String, List<String>> reverseGraph,
                                              Set<String> vertices) {

        // Список найденных компонент
        List<Set<String>> scc = new ArrayList<>();

        // Множество посещённых вершин
        Set<String> visited = new HashSet<>();

        // Стек для запоминания порядка обхода вершин
        Stack<String> stack = new Stack<>();

        // Первый проход DFS по прямому графу — для вычисления порядка завершения вершин
        for (String vertex : vertices) {
            if (!visited.contains(vertex))
                dfs1(vertex, graph, visited, stack);
        }

        // Очистка множества посещённых вершин перед вторым проходом
        visited.clear();

        // Второй проход DFS по обратному графу — для выделения компонент
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                Set<String> component = new HashSet<>();
                dfs2(vertex, reverseGraph, visited, component);
                scc.add(component);
            }
        }

        // Возврат списка сильносвязанных компонент
        return scc;
    }

    // Первый DFS — обход прямого графа для вычисления порядка завершения вершин
    private static void dfs1(String vertex, Map<String, List<String>> graph,
                             Set<String> visited, Stack<String> stack) {

        visited.add(vertex);

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor))
                    dfs1(neighbor, graph, visited, stack);
            }
        }

        stack.push(vertex);
    }

    // Второй DFS — обход обратного графа для формирования компоненты
    private static void dfs2(String vertex, Map<String, List<String>> reverseGraph,
                             Set<String> visited, Set<String> component) {

        visited.add(vertex);
        component.add(vertex);

        if (reverseGraph.containsKey(vertex)) {
            for (String neighbor : reverseGraph.get(vertex)) {
                if (!visited.contains(neighbor))
                    dfs2(neighbor, reverseGraph, visited, component);
            }
        }
    }
}