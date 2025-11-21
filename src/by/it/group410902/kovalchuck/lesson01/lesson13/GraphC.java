package by.it.group410902.kovalchuck.lesson01.lesson13;

import java.util.*;

public class GraphC {

    // Структуры данных для алгоритма Косарайю
    private static Map<String, List<String>> graph = new HashMap<>();
    private static Map<String, List<String>> reversed = new HashMap<>();
    private static Set<String> visited = new HashSet<>();                    // Множество посещённых вершин
    private static Deque<String> order = new ArrayDeque<>();                 // Стек для порядка завершения обхода
    private static List<List<String>> scc = new ArrayList<>();               // Список компонент сильной связности
    private static Map<String, Integer> vertexToComponent = new HashMap<>(); // Соответствие вершины её компоненте

    public static void main(String[] args) {

        graph.clear();
        reversed.clear();
        visited.clear();
        order.clear();
        scc.clear();
        vertexToComponent.clear();

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        buildGraph(input); // Построение графа из входной строки

        // Первый проход DFS - определение порядка обхода
        for (String v : getAllVerticesSorted()) {
            if (!visited.contains(v)) dfs1(v);
        }

        // Второй проход DFS на транспонированном графе - поиск компонент сильной связности
        visited.clear();
        while (!order.isEmpty()) {
            String v = order.pop(); // Берём вершины в порядке убывания времени завершения
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, component);
                Collections.sort(component); // Сортируем вершины внутри компоненты
                scc.add(component);
            }
        }

        //  Создаём: вершина -> номер её компоненты сильной связности
        for (int i = 0; i < scc.size(); i++) {
            for (String v : scc.get(i)) vertexToComponent.put(v, i);
        }

        // Строим граф компоненты как вершины
        int n = scc.size();
        List<Set<Integer>> compGraph = new ArrayList<>();
        for (int i = 0; i < n; i++) compGraph.add(new HashSet<>());

        // Добавляем рёбра между компонентами исключая петли
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                int cf = vertexToComponent.get(from);
                int ct = vertexToComponent.get(to);
                if (cf != ct) compGraph.get(cf).add(ct); // Только рёбра между разными компонентами
            }
        }

        // 5. Топологическая сортировка графа конденсации
        List<Integer> topoOrder = topologicalSort(compGraph);

        // Вывод компонент сильной связности в порядке топологической сортировки
        // от компонент-истоков к компонента-стокам
        for (int i : topoOrder) {
            for (String v : scc.get(i)) System.out.print(v);
            System.out.println();
        }
    }

    //Построение исходного и транспонированного графа из входной строки
    private static void buildGraph(String input) {
        String[] edges = input.split(",");
        for (String e : edges) {
            e = e.trim();
            if (e.isEmpty()) continue;
            String[] parts = e.split("->");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем рёбра в исходный граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // Добавляем обратные рёбра в транспонированный граф
            reversed.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // Гарантируем, что все вершины присутствуют в обоих графах
            graph.putIfAbsent(to, new ArrayList<>());
            reversed.putIfAbsent(from, new ArrayList<>());
        }

        // Сортируем списки смежности для детерминированного порядка обхода
        for (List<String> lst : graph.values()) Collections.sort(lst);
        for (List<String> lst : reversed.values()) Collections.sort(lst);
    }

    //Первый проход DFS - заполнение стека order в порядке времени завершения обработки вершин
    private static void dfs1(String v) {
        visited.add(v);
        for (String to : graph.get(v)) {
            if (!visited.contains(to)) dfs1(to);
        }
        order.push(v); // Добавляем вершину в стек после обработки всех её потомков
    }

    //Второй проход DFS на транспонированном графе - поиск компонент сильной связности
    private static void dfs2(String v, List<String> component) {
        visited.add(v);
        component.add(v); // Добавляем вершину в текущую компоненту
        for (String to : reversed.get(v)) {
            if (!visited.contains(to)) dfs2(to, component);
        }
    }

    //Получение всех вершин графа в отсортированном порядке
    private static List<String> getAllVerticesSorted() {
        List<String> all = new ArrayList<>(graph.keySet());
        Collections.sort(all);
        return all;
    }

    //Топологическая сортировка графа конденсации с помощью DFS
    private static List<Integer> topologicalSort(List<Set<Integer>> compGraph) {
        int n = compGraph.size();
        boolean[] vis = new boolean[n];
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!vis[i]) dfsTopo(i, vis, result, compGraph);
        }
        Collections.reverse(result); // Разворачиваем чтобы получить правильный порядок
        return result;
    }

    //Вспомогательная DFS для топологической сортировки
    private static void dfsTopo(int v, boolean[] vis, List<Integer> result, List<Set<Integer>> compGraph) {
        vis[v] = true;
        for (int to : compGraph.get(v)) {
            if (!vis[to]) dfsTopo(to, vis, result, compGraph);
        }
        result.add(v); // Добавляем вершину после обработки всех её потомков
    }
}