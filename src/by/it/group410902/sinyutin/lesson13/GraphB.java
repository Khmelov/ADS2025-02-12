package by.it.group410902.sinyutin.lesson13;

import java.util.*;

public class GraphB {
    // Состояния: 0 = White (не посещен), 1 = Gray (в процессе), 2 = Black (обработан)
    // Эти поля должны быть статическими, так как DFS — это статический метод,
    // но их необходимо очищать в начале main.
    private static Map<Integer, Integer> visited = new HashMap<>();
    private static Map<Integer, List<Integer>> adj = new HashMap<>();

    public static void main(String[] args) {
        visited.clear();
        adj.clear();

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine();

        // Парсинг графа с использованием Integer
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String cleanEdge = edge.trim();
            String[] parts = cleanEdge.split(" -> ");

            // Проверка на пустые строки после сплита (если вход некорректен)
            if (parts.length != 2) continue;

            // Преобразуем в Integer
            int u = Integer.parseInt(parts[0].trim());
            int v = Integer.parseInt(parts[1].trim());

            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);

            visited.putIfAbsent(u, 0);
            visited.putIfAbsent(v, 0);
        }

        boolean hasCycle = false;

        // Запуск dfs
        List<Integer> nodes = new ArrayList<>(visited.keySet());
        Collections.sort(nodes);

        for (Integer node : nodes) {
            if (visited.get(node) == 0) {
                if (dfs(node)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(int u) {
        visited.put(u, 1); // Gray

        if (adj.containsKey(u)) {
            // Сортируем соседей для детерминизма
            Collections.sort(adj.get(u));

            for (int v : adj.get(u)) {

                // Используем getOrDefault(v, 0) на случай, если какой-то тест
                // имеет вершину-сток, которая не была явно инициализирована,
                // хотя putIfAbsent в main должен это исключать.
                int state = visited.getOrDefault(v, 0);

                if (state == 1) {
                    return true; // Цикл!
                }

                if (state == 0) {
                    if (dfs(v)) return true;
                }
            }
        }

        visited.put(u, 2); // Black
        return false;
    }
}