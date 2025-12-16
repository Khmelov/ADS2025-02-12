package by.it.group410901.skachkova.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        List<List<String>> scc = findStronglyConnectedComponents(input);

        // Выводим компоненты сильной связности
        for (List<String> component : scc) {
            // Сортируем вершины в компоненте лексикографически
            Collections.sort(component);
            // Выводим без пробелов
            for (String vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }
    // Метод для поиска компонент сильной связности в ориентированном графе
    public static List<List<String>> findStronglyConnectedComponents(String input)
    {
        // создаём входную строку и строим граф
        Map<String, List<String>> graph = new HashMap<>();//для представления обратного графа
        Map<String, List<String>> reverseGraph = new HashMap<>();//для хранения вершин
        Set<String> allVertices = new HashSet<>();

        if (!input.isEmpty())
        {
            String[] edges = input.split(",\\s*");
            for (String edge : edges) {
                String[] parts = edge.split("\\s*->\\s*");// Разбиваем ребро по "->" для получения начальной и конечной вершины
                String from = parts[0].trim();//начальная вершина
                String to = parts[1].trim();//конечная вершина

                // Прямой граф
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                // Обратный граф
                reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Алгоритм Косарайю для нахождения компонент сильной связности
        List<String> vertices = new ArrayList<>(allVertices);
        Collections.sort(vertices); // Для стабильности

        //  Первый обход DFS для определения порядка завершения
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        for (String vertex : vertices)
        {
            if (!visited.contains(vertex))
            {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        //  Обход обратного графа в порядке убывания времени завершения
        visited.clear();//помечаем вершину как посещённую
        List<List<String>> stronglyConnectedComponents = new ArrayList<>();

        //проверяем соседей вершины
        while (!stack.isEmpty())
        {
            String vertex = stack.pop();
            if (!visited.contains(vertex))
            {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, reverseGraph, visited, component);
                stronglyConnectedComponents.add(component);
            }
        }

        // Строим конденсационный граф и определяем порядок вывода
        return sortComponentsInTopologicalOrder(stronglyConnectedComponents, graph);
    }
    // первый проход DFS для обратного графа (определение порядка завершения)
    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        //проверка соседей на повтор
        if (graph.containsKey(vertex))
        {
            for (String neighbor : graph.get(vertex))
            {
                if (!visited.contains(neighbor))
                {
                    dfsFirstPass(neighbor, graph, visited, stack);
                }
            }
        }

        stack.push(vertex);
    }

    // Второй проход DFS для обратного графа (поиск компонент сильной связности)
    private static void dfsSecondPass(String vertex, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex); // Добавляем вершину в текущую компоненту сильной связности

        //проверка соседей
        if (reverseGraph.containsKey(vertex))
        {
            for (String neighbor : reverseGraph.get(vertex))
            {
                if (!visited.contains(neighbor))
                {
                    dfsSecondPass(neighbor, reverseGraph, visited, component);
                }
            }
        }
    }

    // Метод для сортировки компонент в топологическом порядке конденсационного графа
    private static List<List<String>> sortComponentsInTopologicalOrder
            (
            List<List<String>> components, Map<String, List<String>> graph) {

        // Создаем отображение вершина -> номер её компонента
        Map<String, Integer> vertexToComponent = new HashMap<>();
        // Заполняем отображение для всех вершин
        for (int i = 0; i < components.size(); i++)
        {
            for (String vertex : components.get(i))
            {
                vertexToComponent.put(vertex, i);
            }
        }

        // Строим конденсационный граф
        Map<Integer, Set<Integer>> condensationGraph = new HashMap<>();
        int[] inDegree = new int[components.size()];

        //перебираем все рёбра исходного графа
        for (String from : graph.keySet())
        {
            for (String to : graph.get(from))
            {//определяем компоненты
                int compFrom = vertexToComponent.get(from);
                int compTo = vertexToComponent.get(to);

                if (compFrom != compTo) {
                    condensationGraph.computeIfAbsent(compFrom, k -> new HashSet<>()).add(compTo);
                }
            }
        }

        // Вычисляем полустепени захода для компонент
        for (Set<Integer> neighbors : condensationGraph.values()) {
            for (int neighbor : neighbors) {
                inDegree[neighbor]++;
            }
        }

        // Топологическая сортировка компонент
        Queue<Integer> queue = new LinkedList<>();
        // Добавляем в очередь все компоненты с нулевой полустепенью захода
        for (int i = 0; i < components.size(); i++)
        {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<List<String>> result = new ArrayList<>();//список с результатом
        while (!queue.isEmpty())
        {
            int current = queue.poll();// Извлекаем текущую компоненту
            result.add(components.get(current));//Добавляем в результта

            if (condensationGraph.containsKey(current))// Если у текущей компоненты есть исходящие рёбра
            {
                for (int neighbor : condensationGraph.get(current))
                {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0)
                    {
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