package by.it.group410901.skachkova.lesson13;

import java.util.*;

public class GraphA {

    //степень входа
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); //для чтения ввода с консоли
        String input = scanner.nextLine(); //для чтения всей строки ввода

        List<String> topologicalOrder = topologicalSort(input);//вызов топологической сортировкицф

        // Вывод результата
        for (int i = 0; i < topologicalOrder.size(); i++)
        {
            if (i > 0)
            {
                System.out.print(" ");
            }
            System.out.print(topologicalOrder.get(i));//вывод текущего элементац
        }
        System.out.println();
    }

    public static List<String> topologicalSort(String input) {
        // для представления графа
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Разбиваем по запятым для получения отдельных рёбер
        String[] edges = input.split(",\\s*");

        for (String edge : edges) {
            // Разбиваем ребро по "->" для получения начальной и конечной вершины
            String[] parts = edge.split("\\s*->\\s*");
            // Извлекаем начальную вершину и убираем лишние пробелы
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Инициализируем степени для обеих вершин
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);
        }

        // Вычисляем полустепени захода
        for (List<String> neighbors : graph.values()) {
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // Очередь с приоритетом для лексикографической сортировки
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем вершины с нулевой полустепенью захода
        for (Map.Entry<String, Integer> entry : inDegree.entrySet())
        {
            if (entry.getValue() == 0) //если вершина не имеет входящих рёбер (полустепень захода = 0)
            {
                queue.offer(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>(); //для результатт сортировки

        // Алгоритм Кана для топологической сортировки
        while (!queue.isEmpty())
        {
            // Извлекаем вершину с наивысшим приоритетом и добавляем в результат
            String current = queue.poll();
            result.add(current);

            if (graph.containsKey(current))// Если текущая вершина имеет исходящие рёбра
            {
                for (String neighbor : graph.get(current))
                {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);//полустепень захода -1
                    if (inDegree.get(neighbor) == 0) //добавляем соседа в очередь для обработки
                    {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return result;
    }
}