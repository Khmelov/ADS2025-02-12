package by.it.group410902.jalilova.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        // создаем сканер для чтения ввода
        Scanner scanner = new Scanner(System.in);
        // читаем всю строку ввода
        String input = scanner.nextLine();
        // закрываем сканер
        scanner.close();

        // создаем граф в виде словаря: вершина -> список соседних вершин
        Map<String, List<String>> graph = new HashMap<>();
        // разбиваем входную строку на отдельные ребра
        String[] edges = input.split(", ");

        // обрабатываем каждое ребро
        for (String edge : edges) {
            // разделяем ребро на начальную и конечную вершину
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();  // начальная вершина
            String to = parts[1].trim();    // конечная вершина

            // добавляем вершины в граф, если их еще нет
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            // добавляем связь от начальной вершины к конечной
            graph.get(from).add(to);
        }

        // проверяем есть ли цикл в графе и выводим результат
        System.out.println(hasCycle(graph) ? "yes" : "no");
    }

    // метод для проверки наличия циклов в графе
    private static boolean hasCycle(Map<String, List<String>> graph) {
        // множество для отслеживания полностью обработанных вершин
        Set<String> visited = new HashSet<>();
        // множество для отслеживания вершин в текущем пути обхода (стек рекурсии)
        Set<String> stack = new HashSet<>();

        // проверяем каждую вершину графа
        for (String node : graph.keySet()) {
            // если вершина еще не посещена и при обходе найден цикл
            if (!visited.contains(node) && dfs(node, graph, visited, stack)) {
                return true;  // цикл найден
            }
        }
        return false;  // циклов не найдено
    }

    // рекурсивный метод обхода в глубину (DFS) для поиска циклов
    private static boolean dfs(String node, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> stack) {
        // если вершина уже в текущем пути обхода - найден цикл
        if (stack.contains(node)) return true;
        // если вершина уже полностью обработана - пропускаем
        if (visited.contains(node)) return false;

        // помечаем вершину как посещенную
        visited.add(node);
        // добавляем вершину в текущий путь обхода
        stack.add(node);

        // рекурсивно обходим всех соседей текущей вершины
        for (String neighbor : graph.get(node)) {
            // если при обходе соседа найден цикл
            if (dfs(neighbor, graph, visited, stack)) {
                return true;  // возвращаем true - цикл найден
            }
        }

        // убираем вершину из текущего пути обхода (backtracking)
        stack.remove(node);
        return false;  // цикл не найден в этой ветви
    }
}