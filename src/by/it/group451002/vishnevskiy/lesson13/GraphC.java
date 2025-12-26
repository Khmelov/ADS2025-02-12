package by.it.group451002.vishnevskiy.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Находим компоненты сильной связности в графе
        List<List<String>> scc = findStronglyConnectedComponents(input);

        // Выводим компоненты сильной связности
        for (List<String> component : scc) {
            // Сортируем вершины в компоненте лексикографически (в алфавитном порядке)
            Collections.sort(component);
            // Выводим вершины компоненты без пробелов между ними
            for (String vertex : component) {
                System.out.print(vertex);
            }
            System.out.println(); // Переход на новую строку после каждой компоненты
        }
    }

    // Метод для поиска компонент сильной связности в ориентированном графе
    public static List<List<String>> findStronglyConnectedComponents(String input) {
        // Граф: вершина -> список её соседей (достижимых вершин)
        Map<String, List<String>> graph = new HashMap<>();
        // Обратный граф: обратные рёбра (для алгоритма Косарайю)
        Map<String, List<String>> reverseGraph = new HashMap<>();
        // Множество всех вершин графа
        Set<String> allVertices = new HashSet<>();

        // Проверяем, не пустая ли входная строка
        if (!input.isEmpty()) {
            // Разбиваем строку по запятым, получая отдельные рёбра
            String[] edges = input.split(",\\s*");

            // Обрабатываем каждое ребро
            for (String edge : edges) {
                // Разделяем ребро по символу "->"
                String[] parts = edge.split("\\s*->\\s*");
                String from = parts[0].trim();  // Начальная вершина ребра
                String to = parts[1].trim();    // Конечная вершина ребра

                // Прямой граф: добавляем ребро from -> to
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

                // Обратный граф: добавляем ребро to -> from (обратное направление)
                reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

                // Добавляем обе вершины в множество всех вершин
                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Алгоритм Косарайю для нахождения компонент сильной связности
        List<String> vertices = new ArrayList<>(allVertices);
        Collections.sort(vertices); // Сортируем вершины для стабильности результата

        // Первый проход DFS: определение порядка завершения обработки вершин
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        // Запускаем DFS из каждой ещё не посещённой вершины
        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        // Второй проход DFS: поиск компонент сильной связности в обратном графе
        visited.clear(); // Сбрасываем отметки о посещении
        List<List<String>> stronglyConnectedComponents = new ArrayList<>();

        // Обрабатываем вершины в порядке убывания времени завершения
        while (!stack.isEmpty()) {
            String vertex = stack.pop(); // Берём вершину с наибольшим временем завершения
            if (!visited.contains(vertex)) {
                // Начинаем новую компоненту сильной связности
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, reverseGraph, visited, component);
                stronglyConnectedComponents.add(component);
            }
        }

        // Сортируем компоненты в топологическом порядке конденсационного графа
        return sortComponentsInTopologicalOrder(stronglyConnectedComponents, graph);
    }

    // Первый проход DFS: определяем порядок завершения обработки вершин
    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        // Помечаем вершину как посещённую
        visited.add(vertex);

        // Рекурсивно обрабатываем всех соседей вершины
        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsFirstPass(neighbor, graph, visited, stack);
                }
            }
        }

        // Когда все потомки обработаны, добавляем вершину в стек
        stack.push(vertex);
    }

    // Второй проход DFS: находим все вершины, достижимые в обратном графе
    private static void dfsSecondPass(String vertex, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        // Помечаем вершину как посещённую
        visited.add(vertex);
        // Добавляем вершину в текущую компоненту сильной связности
        component.add(vertex);

        // Рекурсивно обрабатываем всех соседей в обратном графе
        if (reverseGraph.containsKey(vertex)) {
            for (String neighbor : reverseGraph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsSecondPass(neighbor, reverseGraph, visited, component);
                }
            }
        }
    }

    // Метод для сортировки компонент в топологическом порядке конденсационного графа
    private static List<List<String>> sortComponentsInTopologicalOrder
    (
            List<List<String>> components, Map<String, List<String>> graph) {

        // Создаём отображение: вершина -> номер её компоненты
        Map<String, Integer> vertexToComponent = new HashMap<>();
        // Заполняем отображение для всех вершин
        for (int i = 0; i < components.size(); i++) {
            for (String vertex : components.get(i)) {
                vertexToComponent.put(vertex, i);
            }
        }

        // Строим конденсационный граф (граф компонент)
        Map<Integer, Set<Integer>> condensationGraph = new HashMap<>();
        // Массив полустепеней захода для каждой компоненты
        int[] inDegree = new int[components.size()];

        // Перебираем все рёбра исходного графа
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                // Определяем компоненты начальной и конечной вершин
                int compFrom = vertexToComponent.get(from);
                int compTo = vertexToComponent.get(to);

                // Если рёбро соединяет разные компоненты
                if (compFrom != compTo) {
                    // Добавляем ребро в конденсационный граф
                    condensationGraph.computeIfAbsent(compFrom, k -> new HashSet<>()).add(compTo);
                }
            }
        }

        // Вычисляем полустепени захода для компонент в конденсационном графе
        for (Set<Integer> neighbors : condensationGraph.values()) {
            for (int neighbor : neighbors) {
                inDegree[neighbor]++;
            }
        }

        // Топологическая сортировка компонент (алгоритм Кана)
        Queue<Integer> queue = new LinkedList<>();
        // Добавляем в очередь все компоненты с нулевой полустепенью захода
        for (int i = 0; i < components.size(); i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        // Результат: компоненты в топологическом порядке
        List<List<String>> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll(); // Извлекаем текущую компоненту
            result.add(components.get(current)); // Добавляем её в результат

            // Если у текущей компоненты есть исходящие рёбра
            if (condensationGraph.containsKey(current)) {
                for (int neighbor : condensationGraph.get(current)) {
                    // Уменьшаем полустепень захода соседа
                    inDegree[neighbor]--;
                    // Если у соседа теперь нет входящих рёбер, добавляем его в очередь
                    if (inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // Сортируем вершины внутри каждой компоненты лексикографически
        for (List<String> component : result) {
            Collections.sort(component);
        }

        return result;
    }
}