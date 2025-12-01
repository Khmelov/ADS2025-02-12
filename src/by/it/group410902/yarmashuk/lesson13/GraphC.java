package by.it.group410902.yarmashuk.lesson13;
import java.util.*;

public class GraphC {

    // Для первого прохода DFS (на прямом графе)
    private static Stack<String> finishOrderStack;
    private static Set<String> visited;

    // Для второго прохода DFS (на транспонированном графе)
    private static List<String> currentSCC;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String graphInput = scanner.nextLine();
        scanner.close();

        // adj: прямой граф
        // revAdj: транспонированный граф
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> revAdj = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        String[] edges = graphInput.isEmpty() ? new String[0] : graphInput.split(", ");

        boolean malformedInputDetected = false;
        if (graphInput.trim().isEmpty()) {
            // Пустой ввод, нет узлов, нет SCCs. Выводим ничего и завершаем.
            return; // Добавлено return, чтобы не пытаться обрабатывать пустой граф дальше
        } else {
            for (String edge : edges) {
                String[] parts = edge.split("->"); // Обратите внимание: без пробелов вокруг "->"
                if (parts.length != 2) {
                    System.err.println("Некорректный формат ребра: " + edge);
                    malformedInputDetected = true;
                    break;
                }
                String u = parts[0].trim();
                String v = parts[1].trim();

                allNodes.add(u);
                allNodes.add(v);

                // Убедимся, что все узлы, даже те, у которых нет исходящих/входящих ребер,
                // будут инициализированы в списках смежности, чтобы getOrDefault не создавал новые списки в dfs
                adj.computeIfAbsent(u, k -> new ArrayList<>());
                adj.computeIfAbsent(v, k -> new ArrayList<>()); // Для узлов без исходящих
                revAdj.computeIfAbsent(u, k -> new ArrayList<>()); // Для узлов без входящих
                revAdj.computeIfAbsent(v, k -> new ArrayList<>());

                adj.get(u).add(v);
                revAdj.get(v).add(u); // Для транспонированного графа
            }
        }

        if (malformedInputDetected) {
            return;
        }
        // --- Алгоритм Косарайю ---

        // 1. Первый проход DFS на прямом графе, чтобы получить порядок завершения
        visited = new HashSet<>();
        finishOrderStack = new Stack<>();

        // Гарантируем, что обход начинается с узлов, отсортированных лексикографически,
        // чтобы сделать поведение предсказуемым для тестов (хотя для корректности алгоритма не строго необходимо)
        List<String> sortedNodes = new ArrayList<>(allNodes);
        Collections.sort(sortedNodes);

        for (String node : sortedNodes) {
            if (!visited.contains(node)) {
                dfs1(node, adj);
            }
        }

        // 2. Второй проход DFS на транспонированном графе, чтобы найти SCCs
        visited.clear(); // Сбрасываем посещенные узлы
        List<List<String>> sccs = new ArrayList<>();

        while (!finishOrderStack.isEmpty()) {
            String node = finishOrderStack.pop();
            if (!visited.contains(node)) {
                currentSCC = new ArrayList<>();
                dfs2(node, revAdj);
                Collections.sort(currentSCC); // Сортируем узлы внутри SCC лексикографически
                sccs.add(currentSCC);
            }
        }

        // 3. Вывод SCCs в топологическом порядке (т.е. SCC-истоки сначала)
        // Алгоритм Косарайю находит SCCs в обратном топологическом порядке,
        // поэтому выводим их в обратном порядке обнаружения.
      //  Collections.reverse(sccs); // Инвертируем порядок списка SCCs для вывода от истока к стоку
        StringBuilder resultBuilder = new StringBuilder();
        for (List<String> scc : sccs) {
            for (String node : scc) {
                resultBuilder.append(node);
            }
            resultBuilder.append("\n"); // Каждая SCC с новой строки
        }

        // Удаляем последнюю новую строку, если есть SCCs, чтобы вывод соответствовал "C\nABDHI\nE\nFGK"
        if (resultBuilder.length() > 0) {
            resultBuilder.setLength(resultBuilder.length() - 1);
        }

        System.out.println(resultBuilder);
    }

    // DFS для первого прохода (прямой граф)
    private static void dfs1(String u, Map<String, List<String>> adj) {
        visited.add(u);
        // adj.getOrDefault(u, Collections.emptyList()) - работает, но если все узлы гарантированно
        // присутствуют в map (как теперь благодаря computeIfAbsent), то можно просто get(u).
        for (String v : adj.get(u)) {
            if (!visited.contains(v)) {
                dfs1(v, adj);
            }
        }
        finishOrderStack.push(u); // Добавляем узел в стек после завершения его обхода
    }

    // DFS для второго прохода (транспонированный граф)
    private static void dfs2(String u, Map<String, List<String>> revAdj) {
        visited.add(u);
        currentSCC.add(u);
        // revAdj.getOrDefault(u, Collections.emptyList()) - аналогично
        for (String v : revAdj.get(u)) {
            if (!visited.contains(v)) {
                dfs2(v, revAdj);
            }
        }
    }
}
