package by.it.group410902.jalilova.lesson13;
import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        // создаем сканер для чтения ввода
        Scanner scanner = new Scanner(System.in);
        // читаем всю строку ввода
        String input = scanner.nextLine();
        // закрываем сканер
        scanner.close();

        // создаем исходный граф: вершина -> список соседей
        Map<String, List<String>> graph = new HashMap<>();
        // создаем транспонированный граф (ребра в обратном направлении)
        Map<String, List<String>> reverseGraph = new HashMap<>();

        // разбиваем входную строку на отдельные ребра
        String[] edges = input.split(", ");
        for (String edge : edges) {
            // разделяем ребро на начальную и конечную вершину
            String[] parts = edge.split("->");
            String from = parts[0].trim();  // начальная вершина
            String to = parts[1].trim();    // конечная вершина

            // добавляем вершины в оба графа, если их еще нет
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            // добавляем прямое ребро в исходный граф
            graph.get(from).add(to);

            reverseGraph.putIfAbsent(from, new ArrayList<>());
            reverseGraph.putIfAbsent(to, new ArrayList<>());
            // добавляем обратное ребро в транспонированный граф
            reverseGraph.get(to).add(from);
        }

        // находим компоненты сильной связности алгоритмом Косарайю
        List<Set<String>> components = kosaraju(graph, reverseGraph);

        // строим граф конденсации (компоненты как вершины)
        Map<Set<String>, List<Set<String>>> condensation = buildCondensation(graph, components);

        // выполняем топологическую сортировку компонент
        List<Set<String>> sortedComponents = topologicalSortComponents(condensation);

        // выводим результат: компоненты в топологическом порядке, вершины внутри компонент отсортированы
        for (Set<String> component : sortedComponents) {
            // сортируем вершины внутри компоненты
            List<String> sortedVertices = new ArrayList<>(component);
            Collections.sort(sortedVertices);
            // выводим все вершины компоненты подряд
            for (String vertex : sortedVertices) {
                System.out.print(vertex);
            }
            System.out.println();  // переход на новую строку для следующей компоненты
        }
    }

    // алгоритм Косарайю для нахождения компонент сильной связности
    private static List<Set<String>> kosaraju(Map<String, List<String>> graph,
                                              Map<String, List<String>> reverseGraph) {
        // множество посещенных вершин
        Set<String> visited = new HashSet<>();
        // стек для порядка выхода вершин из DFS
        Stack<String> stack = new Stack<>();

        // первый проход DFS: заполняем стек в порядке времени выхода
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        // очищаем множество посещенных для второго прохода
        visited.clear();
        // список для хранения найденных компонент
        List<Set<String>> components = new ArrayList<>();

        // второй проход DFS: по транспонированному графу в порядке из стека
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                // создаем новую компоненту связности
                Set<String> component = new HashSet<>();
                dfsSecondPass(vertex, reverseGraph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    // первый проход DFS: обход в глубину с заполнением стека
    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);
        // рекурсивно обходим всех соседей
        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }
        // добавляем вершину в стек после обработки всех соседей
        stack.push(vertex);
    }

    // второй проход DFS: обход транспонированного графа для нахождения компонент
    private static void dfsSecondPass(String vertex, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, Set<String> component) {
        visited.add(vertex);
        // добавляем вершину в текущую компоненту
        component.add(vertex);
        // обходим соседей в транспонированном графе
        for (String neighbor : reverseGraph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, reverseGraph, visited, component);
            }
        }
    }

    // построение графа конденсации (компоненты как вершины)
    private static Map<Set<String>, List<Set<String>>> buildCondensation(
            Map<String, List<String>> graph, List<Set<String>> components) {

        // граф конденсации: компонента -> список достижимых компонент
        Map<Set<String>, List<Set<String>>> condensation = new HashMap<>();
        // отображение вершины на ее компоненту
        Map<String, Set<String>> vertexToComponent = new HashMap<>();

        // инициализируем граф конденсации и заполняем отображение вершин
        for (Set<String> component : components) {
            condensation.put(component, new ArrayList<>());
            for (String vertex : component) {
                vertexToComponent.put(vertex, component);
            }
        }

        // добавляем ребра между компонентами
        for (String from : graph.keySet()) {
            Set<String> fromComponent = vertexToComponent.get(from);
            for (String to : graph.get(from)) {
                Set<String> toComponent = vertexToComponent.get(to);
                // если ребро ведет в другую компоненту и такого ребра еще нет
                if (!fromComponent.equals(toComponent) &&
                        !condensation.get(fromComponent).contains(toComponent)) {
                    condensation.get(fromComponent).add(toComponent);
                }
            }
        }

        return condensation;
    }

    // топологическая сортировка компонент графа конденсации
    private static List<Set<String>> topologicalSortComponents(
            Map<Set<String>, List<Set<String>>> condensation) {

        // подсчет полустепеней захода для каждой компоненты
        Map<Set<String>, Integer> inDegree = new HashMap<>();
        // инициализируем счетчики нулями
        for (Set<String> component : condensation.keySet()) {
            inDegree.put(component, 0);
        }

        // вычисляем полустепени захода
        for (Set<String> from : condensation.keySet()) {
            for (Set<String> to : condensation.get(from)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        // результат топологической сортировки
        List<Set<String>> result = new ArrayList<>();
        // очередь для вершин с нулевой полустепенью захода
        Queue<Set<String>> queue = new LinkedList<>();

        // находим начальные компоненты (без входящих ребер)
        for (Set<String> component : condensation.keySet()) {
            if (inDegree.get(component) == 0) {
                queue.add(component);
            }
        }

        // алгоритм Кана для топологической сортировки
        while (!queue.isEmpty()) {
            Set<String> current = queue.poll();
            result.add(current);

            // уменьшаем полустепени захода соседей
            for (Set<String> neighbor : condensation.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                // если полустепень захода стала нулевой, добавляем в очередь
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }
}