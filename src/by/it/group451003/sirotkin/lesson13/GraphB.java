package by.it.group451003.sirotkin.lesson13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class GraphB {
    public GraphB() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Map<String, List<String>> graph = new HashMap();
        Set<String> allNodes = new HashSet();
        String[] edges = input.split(", ");
        String[] var6 = edges;
        int var7 = edges.length;

        String node;
        for(int var8 = 0; var8 < var7; ++var8) {
            node = var6[var8];
            String[] parts = node.split(" -> ");
            String from = parts[0];
            String to = parts[1];
            graph.putIfAbsent(from, new ArrayList());
            ((List)graph.get(from)).add(to);
            allNodes.add(from);
            allNodes.add(to);
        }

        Map<String, Integer> visited = new HashMap();
        Iterator var14 = allNodes.iterator();

        while(var14.hasNext()) {
            node = (String)var14.next();
            visited.put(node, 0);
        }

        boolean hasCycle = false;
        Iterator var17 = allNodes.iterator();

        while(var17.hasNext()) {
            node = (String)var17.next();
            if ((Integer)visited.get(node) == 0 && hasCycleDFS(node, graph, visited)) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycleDFS(String node, Map<String, List<String>> graph, Map<String, Integer> visited) {
        visited.put(node, 1);
        if (graph.containsKey(node)) {
            Iterator var3 = ((List)graph.get(node)).iterator();

            while(var3.hasNext()) {
                String neighbor = (String)var3.next();
                if ((Integer)visited.get(neighbor) == 0) {
                    if (hasCycleDFS(neighbor, graph, visited)) {
                        return true;
                    }
                } else if ((Integer)visited.get(neighbor) == 1) {
                    return true;
                }
            }
        }

        visited.put(node, 2);
        return false;
    }
}
