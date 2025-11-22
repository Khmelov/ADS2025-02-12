package by.it.group451003.kharkevich.lesson13;

import java.util.*;

public class GraphA {

    // Рекурсивная функция для обхода графа и заполнения стека в порядке завершения обработки вершин
    static void topologicalSortUnit(String node, Map<String, ArrayList<String>> graph, Set<String> visited, Stack<String> stack) {
        visited.add(node); // Помечаем текущую вершину как посещенную

        if (graph.get(node) != null) { // Если у вершины есть соседи
            for (String nextNode: graph.get(node)) { // Для каждого соседа
                if (!visited.contains(nextNode)) { // Если сосед не посещен
                    topologicalSortUnit(nextNode, graph, visited, stack); // Рекурсивно обрабатываем соседа
                }
            }
        }
        stack.push(node); // После обработки всех соседей добавляем вершину в стек
    }

    // Основная функция топологической сортировки
    static void topologicalSort(Map<String, ArrayList<String>> graph) {
        Stack<String> stack = new Stack<>(); // Стек для хранения вершин в обратном порядке
        Set<String> visited = new HashSet<>(); // Множество для отслеживания посещенных вершин

        // Сортируем списки соседей в обратном лексикографическом порядке
        for (ArrayList<String> array : graph.values()) {
            array.sort(Comparator.reverseOrder());
        }

        // Для каждой вершины в графе
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) { // Если вершина не посещена
                topologicalSortUnit(node, graph, visited, stack); // Запускаем обход из этой вершины
            }
        }

        // Выводим вершины из стека (в обратном порядке)
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " "); // Извлекаем и выводим вершины
        }
    }

    // Функция для чтения и построения графа из входной строки
    private static void getGraph(Map<String, ArrayList<String>> graph) {
        Scanner in = new Scanner(System.in);

        boolean isEnd = false;
        while (!isEnd) {
            String vertexOut = in.next(); // Читаем исходную вершину
            if (!graph.containsKey(vertexOut)) { // Если вершины нет в графе
                graph.put(vertexOut, new ArrayList<>()); // Добавляем ее
            }
            String edge = in.next(); // Читаем стрелку "->"
            String vertexIn = in.next(); // Читаем целевую вершину
            if (vertexIn.charAt(vertexIn.length() - 1) == ',') { // Если есть запятая
                vertexIn = vertexIn.substring(0, vertexIn.length() - 1); // Убираем запятую
            } else {
                isEnd = true; // Иначе это последнее ребро
            }
            graph.get(vertexOut).add(vertexIn); // Добавляем ребро в граф
        }
    }

    public static void main(String[] args) {
        Map<String, ArrayList<String>> graph = new HashMap<>(); // Создаем граф
        getGraph(graph); // Читаем граф из входных данных
        topologicalSort(graph); // Выполняем топологическую сортировку
    }
}