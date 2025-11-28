package by.it.group410902.derzhavskaya_ludmila.lesson13;
import java.util.*;
//Затем в консоль выводятся вершины компонент сильной связности
//каждый компонент с новой строки, первый - исток, последний - сток,
public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        String[] edges = input.split(", ");

        Set<String> vertices = new HashSet<>();
        List<String[]> edgeList = new ArrayList<>();  // для хранения всех ребер графа
        List<String[]> reverseEdgeList = new ArrayList<>();  // и ребер обратного графа

        // Обрабатываем каждое ребро в формате "A -> B"
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0];
            String to = parts[1];

            vertices.add(from);
            vertices.add(to);

            // Сохраняем ребро в список прямого графа
            edgeList.add(new String[]{from, to});
            // Сохраняем обратное ребро в список обратного графа
            reverseEdgeList.add(new String[]{to, from});
        }

        // Преобразуем множество вершин в список и сортируем лексикографически
        List<String> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);

        // Создаем списки смежности для прямого и обратного графов
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();

        // Инициализируем списки смежности для каждой вершины
        for (String vertex : sortedVertices) {
            graph.put(vertex, new ArrayList<>());
            reverseGraph.put(vertex, new ArrayList<>());
        }

        // Заполняем список смежности прямого графа
        for (String[] edge : edgeList) {
            String from = edge[0];
            String to = edge[1];
            graph.get(from).add(to);
        }

        // Заполняем список смежности обратного графа
        for (String[] edge : reverseEdgeList) {
            String from = edge[0];
            String to = edge[1];
            reverseGraph.get(from).add(to);
        }

        Set<String> visited = new HashSet<>(); // Множество для отслеживания посещенных вершин
        Stack<String> stack = new Stack<>(); // Стек для хранения порядка завершения обхода вершин

        // Первый проход DFS по прямому графу для заполнения стека
        for (String vertex : sortedVertices) {
            if (!visited.contains(vertex)) {
                // Запускаем DFS и заполняем стек в порядке завершения обработки вершин
                fillOrderDFS(vertex, visited, stack, graph);
            }
        }

        // Очищаем множество посещенных вершин для второго прохода
        visited.clear();
        // Список для хранения всех компонент сильной связности
        List<List<String>> sccList = new ArrayList<>();

        // Второй проход DFS по обратному графу в порядке из стека
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                // Список для хранения текущей компоненты сильной связности
                List<String> component = new ArrayList<>();
                // Запускаем DFS по обратному графу для нахождения компоненты
                dfsReverse(vertex, visited, component, reverseGraph);
                // Сортируем вершины компоненты лексикографически
                Collections.sort(component);
                // Добавляем компоненту в общий список
                sccList.add(component);
            }
        }

        // Выводим компоненты сильной связности
        for (List<String> component : sccList) {
            // Объединяем вершины компоненты в одну строку без пробелов
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }
    }

    // Рекурсивная функция для первого прохода DFS (заполнение стека)
    private static void fillOrderDFS(String vertex, Set<String> visited,
                                     Stack<String> stack, Map<String, List<String>> graph) {
        // Помечаем вершину как посещенную
        visited.add(vertex);

        // Рекурсивно обходим всех соседей
        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                fillOrderDFS(neighbor, visited, stack, graph);
            }
        }

        // После обработки всех соседей добавляем вершину в стек
        stack.push(vertex);
    }

    // Рекурсивная функция для второго прохода DFS по обратному графу
    private static void dfsReverse(String vertex, Set<String> visited,
                                   List<String> component, Map<String, List<String>> reverseGraph) {
        // Помечаем вершину как посещенную
        visited.add(vertex);
        // Добавляем вершину в текущую компоненту
        component.add(vertex);

        // Рекурсивно обходим всех соседей в обратном графе
        for (String neighbor : reverseGraph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsReverse(neighbor, visited, component, reverseGraph);
            }
        }
    }
}