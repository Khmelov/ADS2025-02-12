package by.it.group410902.andala.lesson13;

import java.util.*;

public class GraphA {

    // Список смежности. Сортировка по убыванию
    private final SortedMap<String, SortedSet<String>> graph =
            new TreeMap<>(Comparator.reverseOrder());

    // Итоговый порядок топологической сортировки
    private List<String> topo = null;

    // Флаг наличия цикла в графе
    private boolean cyclic = false;

    public List<String> getTopologicalOrder() { return topo; }

    public boolean hasCycle() { return cyclic; }

    public GraphA(String input) {

        // Разбор входных данных вида "A -> B, C -> D, ..."
        for (String part : input.split(", ")) {
            String[] pair = part.split(" -> ");
            String a = pair[0];
            String b = pair[1];

            // Добавляем ребро a → b
            graph.computeIfAbsent(a, k -> new TreeSet<>(Comparator.reverseOrder()))
                    .add(b);
        }

        // Сначала проверяем на наличие цикла
        detectCycle();

        // Если цикла нет — выполняем топологическую сортировку
        if (!cyclic) {
            topo = new ArrayList<>();
            runTopoSort();
        }
    }

    // --------- Проверка наличия цикла ---------
    private void detectCycle() {
        Set<String> visited = new HashSet<>(); // вершины, которые уже полностью обработаны
        Set<String> active = new HashSet<>();  // вершины текущего рекурсивного стека

        for (String v : graph.keySet()) {
            // Если вершина ещё не посещена — запускаем DFS
            if (!visited.contains(v)) {
                dfsCycle(v, visited, active);
                if (cyclic) return; // если нашли цикл — дальше можно не проверять
            }
        }
    }

    // DFS для обнаружения цикла
    private void dfsCycle(String v, Set<String> visited, Set<String> active) {
        visited.add(v);
        active.add(v); // добавляем в текущий стек вызовов

        Set<String> next = graph.get(v);
        if (next != null) {
            for (String to : next) {
                if (cyclic) return;

                if (!visited.contains(to)) {
                    // продолжаем DFS
                    dfsCycle(to, visited, active);
                } else if (active.contains(to)) {
                    // Обнаружили цикл: вершина уже находится в стеке
                    cyclic = true;
                    return;
                }
            }
        }

        // Выходим из вершины — удаляем её из стека
        active.remove(v);
    }

    // --------- Топологическая сортировка ---------
    private void runTopoSort() {
        Set<String> done = new HashSet<>(); // вершины, для которых DFS выполнен

        // Запускаем DFS для всех вершин графа
        for (String v : graph.keySet()) {
            if (!done.contains(v)) {
                dfsTopo(v, done);
            }
        }

        // Разворачиваем список, т.к. при DFS порядок получается обратный
        Collections.reverse(topo);
    }

    // DFS для топологической сортировки
    private void dfsTopo(String v, Set<String> done) {
        done.add(v);

        Set<String> next = graph.get(v);
        if (next != null) {
            for (String to : next) {
                if (!done.contains(to)) {
                    dfsTopo(to, done);
                }
            }
        }

        // Добавляем вершину после обработки всех зависимых
        topo.add(v);
    }

    // --------- main ---------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Ожидается ввод формата: "A -> B, C -> D, B -> E"
        GraphA g = new GraphA(sc.nextLine());

        for (String v : g.getTopologicalOrder()) {
            System.out.print(v + " ");
        }
    }
}
