package by.it.group410902.derzhavskaya_ludmila.lesson13;
import java.util.*;
//Затем в консоль выводится его топологическая сортировка вида:
//0 1 2 3
public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Разбиваем входную строку на отдельные ребра по запятой
        String[] edges = input.split(", ");

        Set<String> vertices = new HashSet<>();  // для хранения всех вершин
        List<String[]> edgeList = new ArrayList<>();  // для хранения всех ребер

        // Обрабатываем каждое ребро
        for (String edge : edges) {
            // Разбиваем ребро на начальную и конечную вершину
            String[] parts = edge.split(" -> ");
            String from = parts[0];
            String to = parts[1];

            // Добавляем вершины в множество
            vertices.add(from);
            vertices.add(to);

            // Сохраняем ребро в список
            edgeList.add(new String[]{from, to});
        }

        // Преобразуем множество вершин в список и сортируем лексикографически
        List<String> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);

        // Создаем список смежности
        Map<String, List<String>> graph = new HashMap<>();
        for (String vertex : sortedVertices) {
            graph.put(vertex, new ArrayList<>());
        }

        // Заполняем список смежности ребрами
        for (String[] edge : edgeList) {
            String from = edge[0];
            String to = edge[1];
            graph.get(from).add(to);
        }

        // Вычисляем количество входящих ребер для каждой вершины
        Map<String, Integer> inDegree = new HashMap<>();
        for (String vertex : sortedVertices) {
            inDegree.put(vertex, 0);
        }

        // Для каждого ребра увеличиваем счетчик входящих ребер конечной вершины
        for (String[] edge : edgeList) {
            String to = edge[1];
            inDegree.put(to, inDegree.get(to) + 1);
        }

        // Очередь для вершин без входящих ребер
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String vertex : sortedVertices) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }

        // Список для хранения результата топологической сортировки
        List<String> result = new ArrayList<>();

        // Алгоритм Кана для топологической сортировки
        while (!queue.isEmpty()) {
            // Извлекаем вершину с наименьшим лексикографическим порядком
            String current = queue.poll();
            result.add(current);

            // Для всех соседей текущей вершины уменьшаем счетчик входящих ребер
            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                // Если у соседа больше нет входящих ребер, добавляем его в очередь
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Проверяем, что все вершины были обработаны (граф не содержит циклов)
        if (result.size() != sortedVertices.size()) {
            System.out.println("Граф содержит циклы, топологическая сортировка невозможна");
            return;
        }

        // Выводим результат топологической сортировки
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}