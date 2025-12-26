package by.it.group410902.barbashova.lesson13;
import java.util.*;

public class GraphB {
    HashMap<String, List<String>> graph = new HashMap<>();
    int White = 0;
    int Gray = 1;
    int Black = 2;

    public static void main(String[] args) {
        GraphB graphB = new GraphB();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        graphB.parseGraph(input);
        System.out.print(graphB.hasCycle() ? "yes" : "no");
    }

    public void parseGraph(String input) {
        String[] edges = input.split(", ");
        for(String edge : edges) {
            String[] vertices = edge.split("->");
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
        }
    }

    public boolean hasCycle() {
        Map<String, Integer> colors = new HashMap<>();

        for (String from : graph.keySet()) {
            colors.putIfAbsent(from, White);
            for (String to : graph.get(from)) {
                colors.putIfAbsent(to, White);
            }
        }

        for (String vertex : colors.keySet()) {
            if (colors.get(vertex) == White) {
                if (hasCycle(vertex, colors)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasCycle(String vertex, Map<String, Integer> colors) {
        if(colors.get(vertex) == Gray) {
            return true;
        }
        if(colors.get(vertex) == Black) {
            return false;
        }
        colors.put(vertex, Gray);
        for(String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if(hasCycle(neighbor, colors)) {
                return true;
            }
        }
        colors.put(vertex, Black);
        return false;
    }
}