package by.it.group410901.volkov.lesson13;

import java.util.*;

/**
 * Класс для топологической сортировки ориентированного графа
 * Топологическая сортировка - это линейное упорядочивание вершин графа,
 * такое что для каждого ребра (u,v) вершина u идет перед v в упорядочивании
 */
public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        Map<String, List<String>> graph = parseGraph(input);
        List<String> result = topologicalSort(graph);
        
        System.out.println(String.join(" ", result));
    }
    
    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();
        
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();
            
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allVertices.add(from);
            allVertices.add(to);
        }
        
        // Добавляем все вершины в граф, даже если они только как назначения
        for (String vertex : allVertices) {
            graph.computeIfAbsent(vertex, k -> new ArrayList<>());
        }
        
        return graph;
    }
    
    /**
     * Выполняет топологическую сортировку графа алгоритмом Кана
     * Алгоритм: находит вершины без входящих ребер (inDegree = 0),
     * добавляет их в результат и уменьшает inDegree их соседей
     * Время выполнения: O(V + E), где V - вершины, E - ребра
     * @param graph граф в виде списка смежности
     * @return список вершин в топологическом порядке
     */
    private static List<String> topologicalSort(Map<String, List<String>> graph) {
        // inDegree[v] - количество входящих ребер в вершину v
        Map<String, Integer> inDegree = new HashMap<>();
        // Очередь с приоритетом для лексикографически минимального порядка
        Queue<String> queue = new PriorityQueue<>();
        List<String> result = new ArrayList<>();
        
        // Инициализируем все вершины с inDegree = 0
        for (String vertex : graph.keySet()) {
            inDegree.put(vertex, 0);
        }
        
        // Вычисляем inDegree для всех вершин: для каждого ребра (from -> to)
        // увеличиваем inDegree[to] на 1
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }
        
        // Добавляем все вершины с inDegree = 0 в очередь
        // Это вершины без входящих ребер, которые можно обработать первыми
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        // Обрабатываем вершины по очереди
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);
            
            // Обновляем inDegree для соседей: уменьшаем на 1 для каждой исходящей дуги
            // Если inDegree соседа стал 0, добавляем его в очередь
            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return result;
    }
}
