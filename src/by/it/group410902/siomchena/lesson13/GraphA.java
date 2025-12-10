package by.it.group410902.siomchena.lesson13;
import java.util.*;
public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>()); //если списка смежности нет создаем
            graph.get(from).add(to); //получаем список смежности и лобавляем вершину

            inDegree.putIfAbsent(from, 0); //если нет ключа то добавляем пару
            inDegree.putIfAbsent(to, 0);
        }

       for (List<String> neighbors : graph.values()) { //для каждой вершины считаем сколько ребер заходит
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1); //если вершина есть в очередном списке смеэности то + ей в счетчик
            }
       }

       List<String> result = topologicalSort(graph, inDegree);

       for (int i = 0; i < result.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(result.get(i));
       }
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>();

        PriorityQueue<String> queue = new PriorityQueue<>(); //для лексиграф.порядка

        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) { //добавляем в очередь если нет входящих веришин
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll(); //берем вершину из начала очереди
            result.add(current); //добавляем ее в окончательныц список

            if (graph.containsKey(current)) { //уменьшаем счечик соседей у кот. в списке есть эта вершина
                List<String> neighbors = new ArrayList<>(graph.get(current));
                Collections.sort(neighbors); //копируем и сортируем

                for (String neighbor : neighbors) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) { //когда счетчик обнулится добавляем в очередь с 0
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return result;
    }
}