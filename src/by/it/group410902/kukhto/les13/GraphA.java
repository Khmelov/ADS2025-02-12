package by.it.group410902.kukhto.les13;
import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();


        Set<String> vertices = new TreeSet<>();//вершины
        List<String[]> edges = new ArrayList<>();//ребра


        String[] pairs = input.split(", ");
        for (String pair : pairs) {
            String[] parts = pair.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            edges.add(new String[]{from, to});
            vertices.add(from);
            vertices.add(to);
        }

        // подсчет входящих ребер
        Map<String, Integer> incomingEdges = new HashMap<>();
        for (String vertex : vertices) {
            incomingEdges.put(vertex, 0);
        }
        for (String[] edge : edges) {
            String to = edge[1];
            incomingEdges.put(to, incomingEdges.get(to) + 1);
        }

        //тут сортировка
        List<String> result = new ArrayList<>();
        PriorityQueue<String> queue = new PriorityQueue<>();


        for (String vertex : vertices) {//для нулевых вершин
            if (incomingEdges.get(vertex) == 0) {
                queue.add(vertex);
            }
        }


        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);
            for (String[] edge : edges) {
                if (edge[0].equals(current)) {
                    String to = edge[1];
                    incomingEdges.put(to, incomingEdges.get(to) - 1);
                    if (incomingEdges.get(to) == 0) {
                        queue.add(to);
                    }
                }
            }
        }


        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }
}