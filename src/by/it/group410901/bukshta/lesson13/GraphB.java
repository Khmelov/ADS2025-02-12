package by.it.group410901.bukshta.lesson13;

import java.util.*;

public class GraphB {

    private static final int WHITE = 0; // вершина не посещена
    private static final int GRAY = 1;  // вершина в процессе обработки (в стеке рекурсии)
    private static final int BLACK = 2; // вершина полностью обработана

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        // создаём граф в виде списка смежности
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                if (edge.contains("->")) {
                    String[] parts = edge.split("->", 2);
                    String from = parts[0].trim(); // начало ребра
                    String to = parts[1].trim();   // конец ребра
                    graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to); // добавляем ребро
                    allNodes.add(from);
                    allNodes.add(to);
                }
            }
        }

        // добавляем изолированные вершины (без исходящих рёбер)
        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
        }

        // инициализация цветов всех вершин (для отслеживания состояния DFS)
        Map<String, Integer> color = new HashMap<>();
        for (String node : allNodes) {
            color.put(node, WHITE);
        }

        boolean hasCycle = false;
        // проходим по всем вершинам
        for (String node : new TreeSet<>(allNodes)) { // TreeSet для упорядочивания вершин
            if (color.get(node) == WHITE) { // если вершина ещё не посещена
                if (dfsHasCycle(node, graph, color)) { // запускаем DFS
                    hasCycle = true; // если найден цикл, помечаем
                    break;
                }
            }
        }

        // вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    // DFS для проверки цикла
    private static boolean dfsHasCycle(String node, Map<String, List<String>> graph, Map<String, Integer> color) {
        color.put(node, GRAY); // помечаем вершину как "в процессе"

        for (String neighbor : graph.get(node)) {
            if (color.get(neighbor) == GRAY) {
                return true; // нашли цикл (ребро ведёт в вершину в стеке рекурсии)
            }
            if (color.get(neighbor) == WHITE) { // если сосед не посещён
                if (dfsHasCycle(neighbor, graph, color)) { // рекурсивно проверяем его
                    return true;
                }
            }
        }

        color.put(node, BLACK); // вершина полностью обработана
        return false;
    }
}
