package by.it.group410902.andala.lesson13;

import java.util.*;

public class GraphC {

    // Исходный граф: вершина -> множество вершин, в которые есть ребро
    private final Map<String, Set<String>> graph = new HashMap<>();

    // Итоговый список компонент сильной связности (КСС)
    private final List<List<String>> components = new ArrayList<>();

    public List<List<String>> getStrongComponents() {
        return components;
    }

    public GraphC(String input) {

        // Разбор входа вида "A->B, C->D, B->A"
        for (String edge : input.split(", ")) {
            String[] p = edge.split("->");
            String a = p[0]; // откуда ребро
            String b = p[1]; // куда ребро

            graph.computeIfAbsent(a, k -> new HashSet<>())
                    .add(b);
        }

        // Запуск алгоритма Косарайю
        computeSCC();
    }

    // ----------- Структура для построения обратного графа и порядка обхода -----------

    private static class Reverse {

        // Обратный граф: каждое ребро развёрнуто
        private final Map<String, Set<String>> reversed = new HashMap<>();

        // Порядок обхода (результат первого прохода Косарайю)
        private final List<String> order = new ArrayList<>();

        public List<String> getOrder() { return order; }

        public Reverse(Map<String, Set<String>> src) {
            buildReverse(src);  // строим обратный граф
            topoOrder();        // запускаем DFS по обратному графу для получения порядка вершин
        }

        // Формирование обратного графа: v -> u, если было u -> v
        private void buildReverse(Map<String, Set<String>> src) {
            for (String v : src.keySet()) {
                for (String to : src.get(v)) {
                    reversed.computeIfAbsent(to, k -> new HashSet<>())
                            .add(v);
                }
            }
        }

        // DFS по обратному графу для определения порядка завершения
        private void topoOrder() {
            Set<String> seen = new HashSet<>();

            // Запускаем DFS для всех вершин обратного графа
            for (String v : reversed.keySet()) {
                if (!seen.contains(v)) {
                    dfs(v, seen);
                }
            }

            // Разворачиваем список, чтобы получить порядок убывания времени выхода
            Collections.reverse(order);
        }

        // Стандартный DFS: записываем вершину после обработки всех потомков
        private void dfs(String v, Set<String> seen) {
            seen.add(v);

            Set<String> nxt = reversed.get(v);
            if (nxt != null) {
                for (String u : nxt) {
                    if (!seen.contains(u)) {
                        dfs(u, seen);
                    }
                }
            }

            // Добавляем после рекурсивного спуска — это "время выхода"
            order.add(v);
        }
    }

    // ----------- Алгоритм Косарайю (2 прохода DFS) -----------

    private void computeSCC() {

        // 1) Строим обратный граф и получаем порядок обхода
        Reverse r = new Reverse(graph);
        List<String> sequence = r.getOrder();

        Set<String> visited = new HashSet<>();

        // 2) Обходим исходный граф в порядке, полученном из обратного
        for (String v : sequence) {
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfsOriginal(v, visited, comp); // собираем компоненту сильной связности
                components.add(comp);
            }
        }

        // Разворот списка КСС (классический результат Косарайю)
        Collections.reverse(components);

        // Сортируем вершины внутри каждой компоненты по алфавиту
        for (List<String> c : components) {
            c.sort(String::compareTo);
        }
    }

    // DFS по исходному графу — собираем одну компоненту сильной связности
    private void dfsOriginal(String v, Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);

        Set<String> next = graph.get(v);
        if (next != null) {
            for (String u : next) {
                if (!visited.contains(u)) {
                    dfsOriginal(u, visited, comp);
                }
            }
        }
    }

    // ----------- main -----------

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GraphC g = new GraphC(sc.nextLine());

        // Вывод компонент — каждая строка отдельная компонента
        for (List<String> comp : g.getStrongComponents()) {
            for (String v : comp) {
                System.out.print(v);
            }
            System.out.println();
        }
    }
}
