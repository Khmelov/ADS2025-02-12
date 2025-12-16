package by.it.group451001.tsurko.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String graphInput = scanner.nextLine();
        scanner.close();

        // adj - список смежности: ключ=вершина, значение=список соседей (куда идут рёбра из ключа)
        Map<String, List<String>> adj = new HashMap<>();
        // inDegree - входящая степень: ключ=вершина, значение=количество входящих рёбер
        Map<String, Integer> inDegree = new HashMap<>();
        // allNodes - множество всех уникальных вершин в графе
        Set<String> allNodes = new HashSet<>();


        String[] edges = graphInput.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            if (parts.length != 2) {
                System.err.println("Некорректный формат ребра: " + edge);
                return;
            }
            String u = parts[0].trim(); // Источник
            String v = parts[1].trim(); // Назначение

            // Добавляем все найденные вершины в список всех вершин
            allNodes.add(u);
            allNodes.add(v);

            // Добавляем ребро u -> v в список смежности
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        }

        // 2. Инициализация входящих степеней всех вершин нулями
        for (String node : allNodes) {
            inDegree.put(node, 0);
        }

        // 3. Вычисление фактических входящих степеней
        for (Map.Entry<String, List<String>> entry : adj.entrySet()) {
            for (String neighbor : entry.getValue()) {
                // Если сосед (neighbor) не был источником ни одного ребра, он мог не попасть в adj.keySet().
                // Но он гарантированно попал в allNodes, и для него уже есть entry в inDegree.
                // Поэтому safe to getOrDefault (хотя в данном случае просто get() тоже сработает)
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // 4. Инициализация PriorityQueue с вершинами, имеющими входящую степень 0
        // PriorityQueue автоматически сортирует строки лексикографически (по алфавиту)
        PriorityQueue<String> readyNodes = new PriorityQueue<>();
        for (String node : allNodes) {
            if (inDegree.get(node) == 0) {
                readyNodes.add(node);
            }
        }

        // 5. Выполнение топологической сортировки
        List<String> topologicalOrder = new ArrayList<>();
        while (!readyNodes.isEmpty()) {
            String u = readyNodes.poll(); // Извлекаем вершину с наименьшим лексикографическим ключом
            topologicalOrder.add(u);

            // Обходим всех соседей вершины u

            for (String v : adj.getOrDefault(u, Collections.emptyList())) {
                inDegree.put(v, inDegree.get(v) - 1); // Уменьшаем входящую степень соседа
                if (inDegree.get(v) == 0) {
                    readyNodes.add(v); // Если степень стала 0, добавляем соседа в очередь
                }
            }
        }

        // 6. Проверка на наличие цикла (если граф содержит цикл, топологическая сортировка невозможна)
        if (topologicalOrder.size() != allNodes.size()) {
            System.out.println("Граф содержит цикл, топологическая сортировка невозможна.");
        } else {
            // 7. Вывод результата
            for (int i = 0; i < topologicalOrder.size(); i++) {
                System.out.print(topologicalOrder.get(i) + (i == topologicalOrder.size() - 1 ? "" : " "));
            }
            System.out.println(); // Переход на новую строку после вывода
        }
    }
}
