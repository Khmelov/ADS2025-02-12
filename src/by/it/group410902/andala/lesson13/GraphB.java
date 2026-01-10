package by.it.group410902.andala.lesson13;

import java.util.*;

public class GraphB {

    // Список смежности: каждая вершина хранит список вершин, в которые из неё есть ребро
    private final Map<String, List<String>> graph = new HashMap<>();

    // Флаг: найден ли цикл в графе
    private boolean cyclic = false;

    public boolean hasCycle() { return cyclic; }

    public GraphB(String input) {

        // Разбираем вход вида:  A -> B, C -> D, B -> E
        for (String part : input.split(", ")) {
            String[] pair = part.split(" -> ");
            String start = pair[0]; // откуда идёт ребро
            String end   = pair[1]; // куда идёт ребро

            // Добавляем ребро start → end
            graph.computeIfAbsent(start, k -> new ArrayList<>())
                    .add(end);
        }

        // Проверяем граф на наличие циклов
        findCycle();
    }

    // ------- Поиск циклов (обход в глубину) -------
    private void findCycle() {
        Set<String> visited = new HashSet<>(); // вершины, полностью обработанные
        Set<String> active  = new HashSet<>(); // вершины в текущем рекурсивном стеке

        // Запускаем DFS для всех вершин графа
        for (String v : graph.keySet()) {
            if (!visited.contains(v)) {
                dfs(v, visited, active);
                if (cyclic) return; // если цикл найден — можно не продолжать
            }
        }
    }

    // DFS для поиска цикла
    private void dfs(String v, Set<String> visited, Set<String> active) {
        visited.add(v); // помечаем вершину как посещённую
        active.add(v);  // добавляем в текущий путь (стек)

        List<String> next = graph.get(v);
        if (next != null) {
            for (String u : next) {

                // Если цикл уже нашли выше — выходим
                if (cyclic) return;

                if (!visited.contains(u)) {
                    // Продолжаем DFS в соседнюю вершину
                    dfs(u, visited, active);
                }
                // Если сосед уже находится в active — значит cycle
                else if (active.contains(u)) {
                    cyclic = true;
                    return;
                }
            }
        }

        // Выходим из вершины — убираем её из текущего стека
        active.remove(v);
    }

    // ------- main -------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GraphB g = new GraphB(sc.nextLine());
        System.out.print(g.hasCycle() ? "yes" : "no");
    }
}
