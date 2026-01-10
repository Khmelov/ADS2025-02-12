package by.it.group451001.shymko.lesson13;

import java.util.*;

public class GraphB {


    static Map<String, List<String>> graph = new HashMap<>();
    static Map<String, Boolean> checked = new HashMap<>();

    static boolean checkCycles(String v){
        boolean cycle = false;
        checked.put(v, true);
        for(String u: graph.get(v)){
            if(checked.get(u)){
                return true;
            }
            else {
                cycle |= checkCycles(u);
            }
        }
        checked.put(v, false);
        return cycle;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();
        graph.clear();

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            if(!graph.containsKey(from)) {
                graph.put(from, new ArrayList<>());
            }
            if(!graph.containsKey(to)) {
                graph.put(to, new ArrayList<>());
            }
            graph.get(from).add(to);
        }
        for(var v: graph.keySet()){
            checked.put(v, false);
        }
        boolean ans = false;
        for(var v: graph.keySet()){
            if(!checked.get(v)) {
                ans |= checkCycles(v);
            }
        }
        System.out.println(ans ? "yes" : "no");
    }
}
