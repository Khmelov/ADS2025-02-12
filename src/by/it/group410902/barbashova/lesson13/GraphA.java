package by.it.group410902.barbashova.lesson13;
import java.util.*;

public class GraphA {
    public static void main(String[] args){
        GraphA graphA = new GraphA();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        graphA.parseGraph(input);
        System.out.print(graphA.topologicalSort());
    }
    HashMap<String, List<String>> graph = new HashMap<>();
    HashMap<String, Integer> inDegree = new HashMap<>();

    public void parseGraph(String input){
        String[] edges = input.split(", ");
        for(String edge : edges){
            String[] vertices = edge.split("->");
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }
    }

    public String topologicalSort() {
        PriorityQueue<String> queue = new PriorityQueue<>();
        ArrayList<String> result = new ArrayList<>();

        for(String vertex : inDegree.keySet()){
            if(inDegree.get(vertex) == 0){
                queue.add(vertex);
            }
        }

        while(!queue.isEmpty()){
            String current = queue.poll();
            result.add(current);

            for(String neighbor : graph.getOrDefault(current, new ArrayList<>())){
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if(inDegree.get(neighbor) == 0){
                    queue.add(neighbor);
                }
            }
        }

        return String.join(" ", result);
    }
}