package by.it.group410901.skachkova.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        boolean hasCycle = hasCycle(input);

        System.out.println(hasCycle ? "yes" : "no");
    }

    //метод для проверки наличия циклов
    public static boolean hasCycle(String input)
    {
        // hashmap для представления графа, hashset для хранения вершин
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        if (!input.isEmpty())
        {
            String[] edges = input.split(",\\s*");
            for (String edge : edges)// Обрабатываем каждое ребро в списке рёбер
            {// Разбиваем ребро по "->" для получения начальной и конечной вершины
                String[] parts = edge.split("\\s*->\\s*");
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // запоминаем пройденные вершины
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String vertex : allVertices)  // Проверяем все вершины графа на наличие циклов
        {
            if (!visited.contains(vertex))// Если вершина еще не посещена, запускаем DFS из нее
            {// Если DFS обнаружил цикл, возвращаем true
                if (dfsHasCycle(vertex, graph, visited, recursionStack))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfsHasCycle(String vertex, Map<String, List<String>> graph,
                                       Set<String> visited, Set<String> recursionStack)
    {
        // Помечаем вершину как посещенную и добавляем в стек рекурсии
        visited.add(vertex);
        recursionStack.add(vertex);

        // Проверяем есть ли у текушщей вершины соседи
        if (graph.containsKey(vertex))
        {//проверяем всех соседей
            for (String neighbor : graph.get(vertex))
            {
                if (!visited.contains(neighbor)) //если последняя вершина не посещена
                {
                    if (dfsHasCycle(neighbor, graph, visited, recursionStack))
                    {
                        return true;
                    }
                } else if (recursionStack.contains(neighbor))
                {
                    // Если сосед уже в стеке рекурсии - найден цикл
                    return true;
                }
            }
        }

        // Убираем вершину из стека рекурсии  т.к. эту вершину уже обошли
        recursionStack.remove(vertex);
        return false;
    }
}