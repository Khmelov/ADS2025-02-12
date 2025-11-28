package by.it.group410902.sinyutin.lesson13;

import java.util.*;

public class GraphC {
    private static Map<String, List<String>> adj = new HashMap<>();
    private static Map<String, List<String>> revAdj = new HashMap<>();
    private static Set<String> visited = new HashSet<>();
    private static Stack<String> stack = new Stack<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // 1. Парсинг и построение обычного и обратного графа
        String[] edges = input.split(", ");
        Set<String> allNodes = new HashSet<>();

        for (String edge : edges) {
            String[] parts = edge.split("->"); // Обратите внимание: без пробелов вокруг -> в примере ввода C
            String u = parts[0];
            String v = parts[1];

            allNodes.add(u);
            allNodes.add(v);

            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            revAdj.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }

        // 2. Первый проход DFS (заполнение стека по времени выхода)
        for (String node : allNodes) {
            if (!visited.contains(node)) {
                fillOrder(node);
            }
        }

        // 3. Второй проход DFS на обратном графе
        visited.clear();

        // Используем список списков для хранения компонент
        // Но алгоритм Косарайю с порядком из стека выдаст их в порядке топологической сортировки (Source -> Sink)
        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsReverse(node, component);

                // Сортируем вершины внутри компонента по алфавиту
                Collections.sort(component);

                // Выводим компонент без пробелов
                for (String s : component) {
                    System.out.print(s);
                }
                System.out.println();
            }
        }
    }

    // Первый dfs заполняем стек
    private static void fillOrder(String u) {
        visited.add(u);
        if (adj.containsKey(u)) {
            // Для стабильности порядка можно отсортировать соседей, но для SCC это не строго обязательно
            List<String> neighbors = adj.get(u);
            Collections.sort(neighbors);
            for (String v : neighbors) {
                if (!visited.contains(v)) {
                    fillOrder(v);
                }
            }
        }
        stack.push(u);
    }

    // Второй dfs собираем компоненту
    private static void dfsReverse(String u, List<String> component) {
        visited.add(u);
        component.add(u);
        if (revAdj.containsKey(u)) {
            for (String v : revAdj.get(u)) {
                if (!visited.contains(v)) {
                    dfsReverse(v, component);
                }
            }
        }
    }
}