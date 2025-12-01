package by.it.group410902.derzhavskaya_ludmila.lesson13;
import java.util.*;
//Затем в консоль выводится фраза о наличии циклов.
public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        String[] edges = input.split(", ");

        Set<String> vertices = new HashSet<>();
        List<String[]> edgeList = new ArrayList<>();

        // Обрабатываем каждое ребро в формате "A -> B"
        for (String edge : edges) {
            // Разбиваем ребро на начальную и конечную вершину
            String[] parts = edge.split(" -> ");
            String from = parts[0];
            String to = parts[1];

            vertices.add(from);
            vertices.add(to);

            edgeList.add(new String[]{from, to});
        }

        // Преобразуем множество вершин в список
        List<String> verticesList = new ArrayList<>(vertices);

        // Создаем список смежности
        Map<String, List<String>> graph = new HashMap<>();
        for (String vertex : verticesList) {
            graph.put(vertex, new ArrayList<>());
        }

        // Заполняем список смежности ребрами
        for (String[] edge : edgeList) {
            String from = edge[0];
            String to = edge[1];
            graph.get(from).add(to);
        }

        // для отслеживания состояния вершин при обходе в глубину
        Set<String> visited = new HashSet<>(); // полностью обработанные вершины
        Set<String> recursionStack = new HashSet<>(); // вершины, которые находятся в текущем пути обхода (стеке рекурсии)

        // Флаг для определения наличия цикла
        boolean hasCycle = false;

        // Проверяем каждую вершину на наличие циклов с помощью DFS
        for (String vertex : verticesList) {
            // Если вершина еще не была обработана, запускаем DFS из нее
            if (!visited.contains(vertex)) {
                // Вызываем рекурсивную функцию проверки на циклы
                if (hasCycleDFS(vertex, visited, recursionStack, graph)) {
                    hasCycle = true;
                    break; // Если нашли хотя бы один цикл, можно прервать поиск
                }
            }
        }

        // Выводим результат в зависимости от наличия циклов
        if (hasCycle) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    // Рекурсивная функция для поиска циклов с помощью обхода в глубину (DFS)
    private static boolean hasCycleDFS(String currentVertex, Set<String> visited,
                                       Set<String> recursionStack, Map<String, List<String>> graph) {
        // Помечаем текущую вершину как находящуюся в текущем пути обхода
        recursionStack.add(currentVertex);

        // Проверяем всех соседей текущей вершины
        for (String neighbor : graph.get(currentVertex)) {
            // Если соседняя вершина еще не была обработана
            if (!visited.contains(neighbor)) {
                // Если соседняя вершина уже находится в текущем пути обхода - это цикл!
                if (recursionStack.contains(neighbor)) {
                    return true; // Найден цикл
                }
                // Рекурсивно проверяем соседнюю вершину
                if (hasCycleDFS(neighbor, visited, recursionStack, graph)) {
                    return true; // Если в поддереве найден цикл
                }
            }
        }

        // Когда все соседи обработаны, убираем вершину из текущего пути
        recursionStack.remove(currentVertex);
        // Помечаем вершину как полностью обработанную
        visited.add(currentVertex);

        return false; // Цикл не найден
    }
}